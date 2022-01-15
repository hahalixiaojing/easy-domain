package easy.domainevent.rocketmq;

import com.alibaba.fastjson.JSON;
import easy.domain.application.subscriber.*;
import easy.domain.event.DefaultExecuteCondition;
import easy.domain.event.EventName;
import easy.domain.event.IDomainEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * @author lixiaojing
 * @date 2021/3/16 6:27 下午
 */
public class RocketMqDomainEventManager implements IDomainEventManager, MessageListenerConcurrently {


    private final MQProducer mqProducer;
    private final MQPushConsumer consumer;
    private final String environmentName;
    //Map key=EventName use Topic

    private final Set<String> eventNameUseTopicList = new HashSet<>();
    private final Set<String> sharedTopicList = new HashSet<>();

    //Outer Map key= EventName;  Inner Map Key=SubscriberName
    private final Map<String, Map<String, SubscriberInfo>> subscribers = new HashMap<>();
    private final IExecuteCondition condition = new DefaultExecuteCondition();

    private IOrderedPerformManager performManager;

    public RocketMqDomainEventManager(IProducerCreator producerCreator, IConsumerCreator consumerCreator, String environmentName) {
        this(producerCreator, consumerCreator, environmentName, null);
    }

    public RocketMqDomainEventManager(IProducerCreator producerCreator, IConsumerCreator consumerCreator, String environmentName, IOrderedPerformManager performManager) {
        this.performManager = performManager;
        this.mqProducer = producerCreator.create();
        this.consumer = consumerCreator.create();
        this.environmentName = environmentName == null ? "" : environmentName;
        this.initConsumer();
    }

    private void initConsumer() {
        try {
            this.consumer.registerMessageListener(this);
            this.consumer.start();
        } catch (Exception ex) {
            throw new RegisterDomainEventException("start consumer error", ex);
        }
    }


    @Override
    public void registerDomainEvent(Class<?> domainEventType) {
        EventNameInfo eventNameInfo = this.getEventName(domainEventType);

        if (eventNameInfo.useEventName()) {

            if (!this.eventNameUseTopicList.add(eventNameInfo.eventName)) {
                throw new RegisterDomainEventException(eventNameInfo.eventName);
            } else {
                try {
                    this.consumer.subscribe(eventNameInfo.eventName, "*");
                } catch (Exception ex) {
                    throw new RegisterDomainEventException(eventNameInfo.eventName, ex);
                }
            }
        } else {
            if (this.sharedTopicList.add(eventNameInfo.shareTopicName)) {
                try {
                    this.consumer.subscribe(eventNameInfo.shareTopicName, "*");
                } catch (Exception ex) {
                    throw new RegisterDomainEventException(eventNameInfo.shareTopicName, ex);
                }
            }
        }
    }

    private void registerSubscriber(ISubscriber subscriber, String event, String alias, IExecuteCondition iExecuteCondition) {
        if (!this.subscribers.containsKey(event)) {

            Map<String, SubscriberInfo> subscriberMap = new HashMap<>();
            subscriberMap.put(alias, new SubscriberInfo(subscriber, alias, iExecuteCondition));

            this.subscribers.put(event, subscriberMap);
        } else {
            Map<String, SubscriberInfo> stringISubscriberMap = this.subscribers.get(event);
            if (stringISubscriberMap.containsKey(alias)) {
                throw new IllegalArgumentException(alias + " is duplication");
            }

            this.subscribers.get(event).put(alias, new SubscriberInfo(subscriber, alias, iExecuteCondition));
        }
    }

    private String getTopicName(EventNameInfo eventNameInfo) {
        String topic;

        if (eventNameInfo.shareTopicName == null || eventNameInfo.shareTopicName.equals("")) {
            topic = eventNameInfo.eventName;
        } else {
            topic = eventNameInfo.shareTopicName;
        }
        return topic;
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, String alias) {
        this.registerSubscriber(subscriber, alias, "");
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, String alias, String dependSubscriber) {

        EventNameInfo event = getEventName(subscriber.subscribedToEventType());
        this.registerSubscriber(subscriber, event.eventName, alias, condition);
        if (this.performManager != null) {
            this.performManager.registerSubscriber(event.eventName, alias, dependSubscriber);
        }
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, String alias, IExecuteCondition condition) {
        EventNameInfo event = getEventName(subscriber.subscribedToEventType());
        this.registerSubscriber(subscriber, event.eventName, alias, condition);
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, String alias, IExecuteCondition condition, String dependSubscriber) {
        EventNameInfo event = getEventName(subscriber.subscribedToEventType());
        this.registerSubscriber(subscriber, event.eventName, alias, condition);
        if (this.performManager != null) {
            this.performManager.registerSubscriber(event.eventName, alias, dependSubscriber);
        }
    }

    private EventNameInfo getEventName(Class<?> eventType) {
        EventName alias = eventType.getAnnotation(EventName.class);

        String evtName;
        String shareTopicName;
        if (alias == null) {
            evtName = eventType.getSimpleName();
            shareTopicName = "";
        } else {
            evtName = alias.value();
            shareTopicName = alias.shareTopicName();
        }
        if (StringUtils.isNotBlank(this.environmentName)) {
            evtName = this.environmentName + "_" + evtName;
            if (StringUtils.isNotBlank(shareTopicName)) {
                shareTopicName = this.environmentName + "_" + shareTopicName;
            }
        }
        return new EventNameInfo(evtName, shareTopicName);
    }

    @SuppressWarnings("unchecked")
    private <T extends IDomainEvent> List<Message> getSendMessages(final T obj) {

        EventNameInfo eventNameInfo = this.getEventName(obj.getClass());

        Map<String, SubscriberInfo> subscriberMap = this.subscribers.get(eventNameInfo.eventName);
        if (subscriberMap != null) {
            String topic = this.getTopicName(eventNameInfo);

            if (this.performManager != null) {
                List<String> rootSubscribers = this.performManager.selectRootSubscribers(eventNameInfo.eventName);

                //如果有执行顺序管理，先查找到根
                subscriberMap = subscriberMap.entrySet().stream()
                        .filter(s -> rootSubscribers.contains(s.getKey()))
                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
            }

            //为每一个事件的订阅创建一个MQ消息
            List<Message> messages = new ArrayList<>(subscriberMap.size());
            for (Map.Entry<String, SubscriberInfo> entry : subscriberMap.entrySet()) {

                if (entry.getValue().getCondition().isExecute(obj)) {

                    SubscribeData subscribeData = this.createSubscribeData(obj, eventNameInfo, entry.getKey(), false);
                    String text = JSON.toJSONString(subscribeData);
                    byte[] bytes = this.stringToByte(text);

                    Message message = new Message(topic, null, obj.getBusinessId(), bytes);
                    messages.add(message);
                }
            }
            return messages;
        }
        return new ArrayList<>(0);
    }

    private byte[] stringToByte(String text) {
        try {
            return text.getBytes(StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new StringToByteException();
        }
    }

    private <T extends IDomainEvent> SubscribeData createSubscribeData(final T obj, EventNameInfo eventNameInfo, String subscriber, Boolean onlyThis) {
        final String realEventName;
        if (eventNameInfo.shareTopicName != null && !eventNameInfo.shareTopicName.equals("")) {
            realEventName = eventNameInfo.eventName;
        } else {
            realEventName = "";
        }
        String jsonData = JSON.toJSONString(obj);
        return new SubscribeData(subscriber, jsonData, realEventName, onlyThis);
    }

    private <T extends IDomainEvent> Message getSendMessage(final T obj, String subscriber, Boolean onlyThis) {

        EventNameInfo eventNameInfo = getEventName(obj.getClass());

        Map<String, SubscriberInfo> subscriberMap = this.subscribers.get(eventNameInfo.eventName);
        if (subscriberMap == null) {
            return null;
        }
        SubscriberInfo subscriberInfo = subscriberMap.get(subscriber);
        if (subscriberInfo == null) {
            return null;
        }
        IExecuteCondition condition = subscriberInfo.getCondition();
        if (!condition.isExecute(obj)) {
            return null;
        }

        final SubscribeData subscribeData = this.createSubscribeData(obj, eventNameInfo, subscriber, onlyThis);
        final String topic = this.getTopicName(eventNameInfo);

        String text = JSON.toJSONString(subscribeData);
        byte[] bytes = this.stringToByte(text);

        return new Message(topic, this.environmentName, obj.getBusinessId(), bytes);
    }

    @Override
    public <T extends IDomainEvent> void publishEvent(T obj) {
        List<Message> sendMessages = this.getSendMessages(obj);
        try {
            if (!sendMessages.isEmpty()) {
                this.mqProducer.send(sendMessages);
            }
        } catch (Exception e) {
            throw new PublishEventException(obj.getBusinessId(), e);
        }
    }

    @Override
    public <T extends IDomainEvent> void publishEvent(T obj, String subscriber) {

        this.publishEvent(obj, subscriber, false);
    }

    @Override
    public <T extends IDomainEvent> void publishEvent(T obj, String subscriber, boolean onlyThis) {

        final Message sendMessage = this.getSendMessage(obj, subscriber, onlyThis);
        try {
            if (sendMessage != null) {
                this.mqProducer.send(sendMessage);
            }
        } catch (Exception e) {
            throw new PublishEventException(obj.getBusinessId(), e);
        }
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgList, ConsumeConcurrentlyContext context) {
        if (msgList == null || msgList.isEmpty()) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        for (MessageExt msg : msgList) {

            String data = new String(msg.getBody(), StandardCharsets.UTF_8);
            SubscribeData subscribeData = JSON.parseObject(data, SubscribeData.class);
            String event = msg.getTopic();
            if (subscribeData.getRealEventName() != null && !subscribeData.getRealEventName().equals("")) {
                event = subscribeData.getRealEventName();
            }
            Map<String, SubscriberInfo> subscriberList = this.subscribers.get(event);
            AbstractDomainEventSubscriber subscriber = (AbstractDomainEventSubscriber) subscriberList.get(subscribeData.getName()).getSubscriber();
            if (subscriber != null) {
                subscriber.handleEvent(subscribeData.getEventData());

                if (this.performManager != null && !subscribeData.getOnlyThis()) {

                    List<String> nextSubscribers = this.performManager.selectNextSubscribers(event, subscribeData.getName());
                    IDomainEvent iDomainEvent = subscriber.parseEvent(subscribeData.getEventData());
                    nextSubscribers.forEach(s -> this.publishEvent(iDomainEvent, s, subscribeData.getOnlyThis()));

                }
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
