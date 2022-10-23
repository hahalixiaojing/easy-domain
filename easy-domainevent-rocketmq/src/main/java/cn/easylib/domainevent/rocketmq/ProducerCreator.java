package cn.easylib.domainevent.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;


public class ProducerCreator implements IProducerCreator {

    private final String nameServer;

    public ProducerCreator(String nameServer) {
        this.nameServer = nameServer;
    }


    @Override
    public MQProducer create(String producerGroupName) {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(producerGroupName);
        defaultMQProducer.setNamesrvAddr(this.nameServer);
        return defaultMQProducer;
    }
}
