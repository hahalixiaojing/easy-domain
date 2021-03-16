package easy.domainevent.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;

/**
 * @author lixiaojing10
 * @date 2021/3/16 6:49 下午
 */
public class ProducerCreator implements IProducerCreator {

    private final String nameServer;
    private final String group;

    public ProducerCreator(String nameServer, String group) {
        this.nameServer = nameServer;
        this.group = group;
    }

    @Override
    public MQProducer create() {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(this.group);
        defaultMQProducer.setNamesrvAddr(this.nameServer);
        return defaultMQProducer;
    }
}
