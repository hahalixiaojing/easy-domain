package easy.domainevent.rocketmq;

import org.apache.rocketmq.client.consumer.MQPushConsumer;

/**
 * @author lixiaojing10
 * @date 2021/3/16 8:18 下午
 */
public interface IConsumerCreator {
    MQPushConsumer create();
}
