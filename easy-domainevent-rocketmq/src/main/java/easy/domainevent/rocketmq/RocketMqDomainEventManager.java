package easy.domainevent.rocketmq;

import com.alibaba.fastjson.JSON;
import easy.domain.application.subscriber.*;
import easy.domain.event.DefaultExecuteCondition;
import easy.domain.event.EventName;
import easy.domain.event.IDomainEvent;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author lixiaojing10
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


    public RocketMqDomainEventManager(IProducerCreator producerCreator, IConsumerCreator consumerCreator, String environmentName) {
        this.mqProducer = producerCreator.create();
        this.consumer = consumerCreator.create();
        this.environmentName = (environmentName == null || environmentName.equals("")) ? "prod" : environmentName;

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
                    this.consumer.subscribe(eventNameInfo.eventName, this.environmentName);
                } catch (Exception ex) {
                    throw new RegisterDomainEventException(eventNameInfo.eventName, ex);
                }
            }
        } else {
            if (this.sharedTopicList.add(eventNameInfo.shareTopicName)) {
                try {
                    this.consumer.subscribe(eventNameInfo.shareTopicName, this.environmentName);
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
        EventNameInfo event = getEventName(subscriber.subscribedToEventType());
        this.registerSubscriber(subscriber, event.eventName, alias, condition);
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, String alias, IExecuteCondition condition) {
        EventNameInfo event = getEventName(subscriber.subscribedToEventType());
        this.registerSubscriber(subscriber, event.eventName, alias, condition);
    }

    private EventNameInfo getEventName(Class<?> eventType) {
        EventName alias = eventType.getAnnotation(EventName.class);

        final String evtName;
        final String shareTopicName;
        if (alias == null) {
            evtName = eventType.getSimpleName();
            shareTopicName = "";
        } else {
            evtName = alias.value();
            shareTopicName = alias.shareTopicName();
        }
//        if (StringUtils.isNotBlank(this.environmentName)) {
//            evtName = this.environmentName + "_" + evtName;
//            if (StringUtils.isNotBlank(shareTopicName)) {
//                shareTopicName = this.environmentName + "_" + shareTopicName;
//            }
//        }
        return new EventNameInfo(evtName, shareTopicName);
    }

    private <T extends IDomainEvent> List<Message> getSendMessages(final T obj) {

        EventNameInfo eventNameInfo = this.getEventName(obj.getClass());

        Map<String, SubscriberInfo> subscriberMap = this.subscribers.get(eventNameInfo.eventName);
        if (subscriberMap != null) {
            String topic = this.getTopicName(eventNameInfo);

            //为每一个事件的订阅创建一个MQ消息
            List<Message> messages = new ArrayList<>(subscriberMap.size());
            for (Map.Entry<String, SubscriberInfo> entry : subscriberMap.entrySet()) {

                if (entry.getValue().getCondition().isExecute(obj)) {

                    SubscribeData subscribeData = this.createSubscribeData(obj, eventNameInfo, entry.getKey());
                    String text = JSON.toJSONString(subscribeData);
                    byte[] bytes = this.stringToByte(text);

                    Message message = new Message(topic, this.environmentName, obj.getBusinessId(), bytes);
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

    private <T extends IDomainEvent> SubscribeData createSubscribeData(final T obj, EventNameInfo eventNameInfo, String subscriber) {
        final String realEventName;
        if (eventNameInfo.shareTopicName != null && !eventNameInfo.shareTopicName.equals("")) {
            realEventName = eventNameInfo.eventName;
        } else {
            realEventName = "";
        }
        String jsonData = JSON.toJSONString(obj);
        return new SubscribeData(subscriber, jsonData, realEventName);
    }

    private <T extends IDomainEvent> Message getSendMessage(final T obj, String subscriber) {

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

        final SubscribeData subscribeData = this.createSubscribeData(obj, eventNameInfo, subscriber);
        final String topic = this.getTopicName(eventNameInfo);

        String text = JSON.toJSONString(subscribeData);
        byte[] bytes = this.stringToByte(text);

        return new Message(topic, this.environmentName, obj.getBusinessId(), bytes);
    }

    @Override
    public <T extends IDomainEvent> void publishEvent(T obj) {
        List<Message> sendMessages = this.getSendMessages(obj);
        try {
            this.mqProducer.send(sendMessages);
        } catch (Exception e) {
            throw new PublishEventException(obj.getBusinessId(), e);
        }
    }

    @Override
    public <T extends IDomainEvent> void publishEvent(T obj, String subscriber) {
        final Message sendMessage = this.getSendMessage(obj, subscriber);
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
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
