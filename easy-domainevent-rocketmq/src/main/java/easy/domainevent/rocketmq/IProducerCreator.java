package easy.domainevent.rocketmq;

import org.apache.rocketmq.client.producer.MQProducer;

/**
 * @author lixiaojing10
 * @date 2021/3/16 6:43 下午
 */
public interface IProducerCreator {

    MQProducer create();
}
