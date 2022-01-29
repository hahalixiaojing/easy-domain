package easy.domainevent.rocketmq;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;

/**
 * @author lixiaojing
 * @date 2021/3/16 10:07 下午
 */
public class ConsumerCreator implements IConsumerCreator {

    private final String nameServer;
    private final String consumerGroup;
    private final String instanceName;

    public ConsumerCreator(String nameServer, String consumerGroup) {
        this(nameServer, consumerGroup, "");
    }

    public ConsumerCreator(String nameServer, String consumerGroup, String instanceName) {
        this.nameServer = nameServer;
        this.consumerGroup = consumerGroup;
        this.instanceName = instanceName;
    }

    @Override
    public MQPushConsumer create() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.consumerGroup);
        if (StringUtils.isNotEmpty(this.instanceName)) {
            consumer.setInstanceName(this.instanceName);
        }
        consumer.setNamesrvAddr(this.nameServer);
        return consumer;
    }
}
