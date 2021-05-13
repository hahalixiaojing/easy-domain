package easy.domainevent.rocketmq;

import easy.domain.application.subscriber.IExecuteCondition;
import easy.domain.event.BaseDomainEvent;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 在运行该单元测试前需要，本地部署好 rocketmq,并修改对应的nameServer地址
 * 测试基于rocketmq的领域事件发布订阅机制
 *
 * @author lixiaojing
 * @date 2021/3/17 5:22 下午
 */
public class RocketMqDomainEventManagerTest {
    /**
     * 使用类名做为Topic的发布订阅实现
     *
     * @throws InterruptedException
     */
    @Test
    public void topicUseClassName() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(2);

        RocketmqSubscriberFactory factory = new RocketmqSubscriberFactory();


        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876", "QQ"), new ConsumerCreator("localhost:9876", "QQ"), "");


        rocketMqDomainEventManager.registerDomainEvent(MyDomainEvent.class);
        rocketMqDomainEventManager.registerSubscriber(factory.build(MyDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println(s.name + "test1");
        }), "test1");

        rocketMqDomainEventManager.registerSubscriber(factory.build(MyDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(s.name + "test2");

        }), "test2");
        //发送一个事件，分别被订阅test1和test2 处理
        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("abc"));


        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }

    /**
     * 使用共享topic的发布订阅测试，
     * 多个领域事件共享一个topic
     *
     * @throws InterruptedException
     */
    @Test
    public void useShareTopicTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        RocketmqSubscriberFactory factory = new RocketmqSubscriberFactory();

        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876", "QQ"), new ConsumerCreator("localhost:9876", "QQ"), "");

        rocketMqDomainEventManager.registerDomainEvent(ShareDomainEvent.class);
        //ShareDomainEvent事件订阅
        rocketMqDomainEventManager.registerSubscriber(factory.build(ShareDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(s.name + "shareTest1");

        }), "shareTest1");

        //ShareDomainEvent事件订阅
        rocketMqDomainEventManager.registerSubscriber(factory.build(ShareDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(s.name + "shareTest2");

        }), "shareTest2");

        rocketMqDomainEventManager.publishEvent(new ShareDomainEvent("100", "share"));

        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }

    /**
     * 带条件的订阅测试
     *
     * @throws InterruptedException
     */
    @Test
    public void useConditionTest() throws InterruptedException {

        RocketmqSubscriberFactory factory = new RocketmqSubscriberFactory();
        CountDownLatch countDownLatch = new CountDownLatch(2);


        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876", "QQ"), new ConsumerCreator("localhost:9876", "QQ"), "prod");

        rocketMqDomainEventManager.registerDomainEvent(ShareDomainEvent.class);
        rocketMqDomainEventManager.registerSubscriber(factory.build(ShareDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println("test1");

        }), "test1", (IExecuteCondition<ShareDomainEvent>) iDomainEvent -> iDomainEvent.name.equals("test1"));

        rocketMqDomainEventManager.registerSubscriber(factory.build(ShareDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println("test2");

        }), "test2", (IExecuteCondition<ShareDomainEvent>) iDomainEvent -> iDomainEvent.name.equals("test2"));

        rocketMqDomainEventManager.publishEvent(new ShareDomainEvent("100", "test1"));
        rocketMqDomainEventManager.publishEvent(new ShareDomainEvent("100", "test2"));

        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());

    }
}
