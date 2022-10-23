package cn.easylib.domainevent.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;

/**
 * @author lixiaojing
 * @date 2021/3/16 6:49 下午
 */
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
