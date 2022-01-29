package easy.domainevent.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;

/**
 * @author lixiaojing
 * @date 2021/3/16 10:07 下午
 */
public class ConsumerCreator implements IConsumerCreator {

    private final String nameServer;

    public ConsumerCreator(String nameServer) {
        this.nameServer = nameServer;
    }


    @Override
    public MQPushConsumer create(String consumerGroupName) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroupName);
        consumer.setNamesrvAddr(this.nameServer);
        return consumer;
    }
}
