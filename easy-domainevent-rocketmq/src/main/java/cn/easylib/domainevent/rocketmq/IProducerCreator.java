package cn.easylib.domainevent.rocketmq;

import org.apache.rocketmq.client.producer.MQProducer;

public interface IProducerCreator {

    MQProducer create(String producerGroupName);

}
