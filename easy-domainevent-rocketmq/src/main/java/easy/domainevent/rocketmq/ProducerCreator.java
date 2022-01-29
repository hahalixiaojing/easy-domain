package easy.domainevent.rocketmq;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;

/**
 * @author lixiaojing
 * @date 2021/3/16 6:49 下午
 */
public class ProducerCreator implements IProducerCreator {

    private final String nameServer;
    private final String producerGroup;
    private final String instanceName;

    public ProducerCreator(String nameServer, String producerGroup) {
        this(nameServer, producerGroup, "");
    }

    public ProducerCreator(String nameServer, String producerGroup, String instanceName) {
        this.nameServer = nameServer;
        this.producerGroup = producerGroup;
        this.instanceName = instanceName;
    }

    @Override
    public MQProducer create() {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(this.producerGroup);
        defaultMQProducer.setNamesrvAddr(this.nameServer);
        if (StringUtils.isNotEmpty(instanceName)) {
            defaultMQProducer.setInstanceName(instanceName);
        }
        try {
            defaultMQProducer.start();
        } catch (Exception ex) {
            throw new RegisterDomainEventException("start producer", ex);
        }
        return defaultMQProducer;
    }
}
