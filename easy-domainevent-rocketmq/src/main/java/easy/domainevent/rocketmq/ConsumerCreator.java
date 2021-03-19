package easy.domainevent.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;

/**
 * @author lixiaojing10
 * @date 2021/3/16 10:07 下午
 */
public class ConsumerCreator implements IConsumerCreator {

    private final String nameServer;
    private final String group;

    public ConsumerCreator(String nameServer, String group) {
        this.nameServer = nameServer;
        this.group = group;
    }

    @Override
    public MQPushConsumer create() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.group);
        consumer.setInstanceName("domainEvent" + System.nanoTime());
        consumer.setNamesrvAddr(this.nameServer);
        return consumer;
    }
}
