package easy.domainevent.rocketmq;

import easy.domain.application.subscriber.DefaultOrderedPerformManager;
import easy.domain.application.subscriber.ISubscriberFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author lixiaojing10
 * @date 2021/12/24 10:08 下午
 */
public class RocketMqDomainEventOrderedManagerTest {

    /**
     * 验证按顺序执行 test3->test2-> test1  ，输出 3 -> 2 -> 1
     * @throws InterruptedException
     */
    @Test
    public void topicUseClassName() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(3);

        ISubscriberFactory factory = new RocketmqSubscriberFactory();


        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876", "QQ"), new ConsumerCreator("localhost:9876", "QQ"), "",new DefaultOrderedPerformManager());


        rocketMqDomainEventManager.registerDomainEvent(MyDomainEvent.class);
        rocketMqDomainEventManager.registerSubscriber(factory.build(MyDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println(1);
        }), "test1");

        rocketMqDomainEventManager.registerSubscriber(factory.build(MyDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(2);

        }), "test2");

        rocketMqDomainEventManager.registerSubscriber(factory.build(MyDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(3);

        }), "test3");

        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("abc"));

        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }

    /**
     * 验证按顺序执行订阅 shareTest2 -> shareTest1 ->shareTest3
     * @throws InterruptedException
     */
    @Test
    public void useShareTopicTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        RocketmqSubscriberFactory factory = new RocketmqSubscriberFactory();

        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876", "QQ"), new ConsumerCreator("localhost:9876", "QQ"), "",new DefaultOrderedPerformManager());

        rocketMqDomainEventManager.registerDomainEvent(ShareDomainEvent.class);
        //ShareDomainEvent事件订阅
        rocketMqDomainEventManager.registerSubscriber(factory.build(ShareDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(s.name + "shareTest1");

        }), "shareTest1","shareTest2");

        //ShareDomainEvent事件订阅
        rocketMqDomainEventManager.registerSubscriber(factory.build(ShareDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(s.name + "shareTest2");

        }), "shareTest2");

        rocketMqDomainEventManager.registerSubscriber(factory.build(ShareDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(s.name + "shareTest3");

        }), "shareTest3","shareTest1");

        rocketMqDomainEventManager.publishEvent(new ShareDomainEvent("100", "share"));

        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }
}
