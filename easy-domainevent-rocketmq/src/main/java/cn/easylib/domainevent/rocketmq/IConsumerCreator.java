package cn.easylib.domainevent.rocketmq;

import org.apache.rocketmq.client.consumer.MQPushConsumer;


public interface IConsumerCreator {
    MQPushConsumer create(String consumerGroupName);
}
