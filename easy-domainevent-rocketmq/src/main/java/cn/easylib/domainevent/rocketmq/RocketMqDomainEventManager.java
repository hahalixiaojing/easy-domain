package cn.easylib.domainevent.rocketmq;

import cn.easylib.domain.application.subscriber.EventNameInfo;
import cn.easylib.domain.application.subscriber.IOrderedPerformManager;
import cn.easylib.domain.application.subscriber.OrderedPerformManager;
import cn.easylib.domain.application.subscriber.SubscribeData;
import cn.easylib.domain.event.AbstractMQDomainEventManager;
import cn.easylib.domain.event.IDomainEvent;
import cn.easylib.domain.event.PublishEventException;
import cn.easylib.domain.event.RegisterDomainEventException;
import com.alibaba.fastjson.JSON;
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


public class RocketMqDomainEventManager extends AbstractMQDomainEventManager implements MessageListenerConcurrently {

    private final Set<String> eventNameUseTopicList = new HashSet<>();
    private final Set<String> sharedTopicList = new HashSet<>();
    private final IConsumerCreator consumerCreator;
    private final IProducerCreator producerCreator;
    private final List<MQPushConsumer> mqPushConsumerList = new ArrayList<>();
    private final Map<String, MQProducer> mqProducerMap = new HashMap<>();

    public RocketMqDomainEventManager(IProducerCreator producerCreator, IConsumerCreator consumerCreator, String environmentName) {
        this(producerCreator, consumerCreator, environmentName, new OrderedPerformManager());
    }

    public RocketMqDomainEventManager(IProducerCreator producerCreator,
                                      IConsumerCreator consumerCreator,
                                      String environmentName,
                                      IOrderedPerformManager performManager) {
        super(environmentName, performManager);
        this.consumerCreator = consumerCreator;
        this.producerCreator = producerCreator;
    }

    @Override
    public void registerDomainEvent(Class<?> domainEventType) {
        EventNameInfo eventNameInfo = this.getEventName(domainEventType);

        if (eventNameInfo.useEventName()) {

            if (!this.eventNameUseTopicList.add(eventNameInfo.eventName)) {
                throw new RegisterDomainEventException(eventNameInfo.eventName);
            } else {
                try {

                    MQProducer mqProducer = this.producerCreator.create(eventNameInfo.eventName);
                    mqProducer.start();
                    this.mqProducerMap.put(eventNameInfo.eventName, mqProducer);

                    MQPushConsumer mqPushConsumer = this.consumerCreator.create(eventNameInfo.eventName);
                    mqPushConsumer.subscribe(eventNameInfo.eventName, "*");
                    mqPushConsumer.registerMessageListener(this);
                    mqPushConsumer.start();
                    this.mqPushConsumerList.add(mqPushConsumer);
                } catch (Exception ex) {
                    throw new RegisterDomainEventException(eventNameInfo.eventName, ex);
                }
            }
        } else {
            if (this.sharedTopicList.add(eventNameInfo.shareTopicName)) {
                try {

                    MQProducer mqProducer = this.producerCreator.create(eventNameInfo.shareTopicName);
                    mqProducer.start();
                    this.mqProducerMap.put(eventNameInfo.shareTopicName, mqProducer);

                    MQPushConsumer mqPushConsumer = this.consumerCreator.create(eventNameInfo.shareTopicName);
                    mqPushConsumer.subscribe(eventNameInfo.shareTopicName, "*");
                    mqPushConsumer.registerMessageListener(this);
                    mqPushConsumer.start();
                    this.mqPushConsumerList.add(mqPushConsumer);
                } catch (Exception ex) {
                    throw new RegisterDomainEventException(eventNameInfo.shareTopicName, ex);
                }
            }
        }
    }

    private byte[] stringToByte(String text) {
        try {
            return text.getBytes(StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new StringToByteException();
        }
    }

    @Override
    public <T extends IDomainEvent> void publishEvent(T obj) {

        final String topic = this.getTopicName(obj.getClass());

        final List<Message> messages = this.buildSubscribeDataList(obj).stream().map(s -> {
            final String text = JSON.toJSONString(s);
            final byte[] bytes = this.stringToByte(text);
            return new Message(topic, this.environmentName, obj.getBusinessId(), bytes);
        }).collect(Collectors.toList());

        try {
            if (!messages.isEmpty()) {

                this.mqProducerMap.get(topic).send(messages);
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

        SubscribeData subscribeData = this.buildSubscribeData(obj, subscriber, onlyThis);
        if (subscribeData == null) {
            return;
        }
        final String topic = this.getTopicName(obj.getClass());
        final String text = JSON.toJSONString(subscribeData);
        final byte[] bytes = this.stringToByte(text);
        Message message = new Message(topic, this.environmentName, obj.getBusinessId(), bytes);
        try {
            this.mqProducerMap.get(topic).send(message);
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
            this.handleEvent(data, msg.getTopic());
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
