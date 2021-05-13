package easy.domainevent.rocketmq;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 事件消息环境隔离测试
 *
 * @author lixiaojing
 * @date 2021/3/25 5:12 下午
 */
public class MultiEnvironmentIsolationTest {

    @Test
    public void emptyEnvironment() throws InterruptedException {
        this.build("", "send prod0 message");
    }

    @Test
    public void notEmptyEnvironment() throws InterruptedException {
        this.build("prod", "send prod1 message");
    }

    private void build(String environmentName, String message) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        RocketmqSubscriberFactory factory = new RocketmqSubscriberFactory();

        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876", "QQ"), new ConsumerCreator("localhost:9876", "QQ"), environmentName);
        rocketMqDomainEventManager.registerDomainEvent(MyDomainEvent.class);
        rocketMqDomainEventManager.registerSubscriber(factory.build(MyDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println(s.name + " = " + message);
        }), "test1");

        Thread.sleep(8000);
        System.out.println("send " + message);

        //发送一个事件，分别被订阅test1和test2 处理
        for (int x = 0; x < 100; x++) {
            rocketMqDomainEventManager.publishEvent(new MyDomainEvent(message));
        }


        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }

}
