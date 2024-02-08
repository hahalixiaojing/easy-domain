package cn.easylib.domainevent.rocketmq;

import cn.easylib.domain.application.subscriber.IExecuteCondition;
import cn.easylib.domain.application.subscriber.OrderedPerformManager;
import cn.easylib.domain.application.subscriber.SubscriberDelayLevel;
import cn.easylib.domain.event.SubscriberFactory;
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
     * 使用类名做为Topic的发布订阅实现,输出abctest1和abctest2
     *
     * @throws InterruptedException
     */
    @Test
    public void topicUseClassName() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(2);

        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876"), new ConsumerCreator("localhost:9876"), "");


        rocketMqDomainEventManager.registerDomainEvent(MyDomainEvent.class);
        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println(s.name + "test1");
        }), "test1");

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {

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
     * 多个领域事件共享一个topic 输出shareshareTest1和shareshareTest2
     *
     * @throws InterruptedException
     */
    @Test
    public void useShareTopicTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);


        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876"), new ConsumerCreator("localhost:9876"), "");

        rocketMqDomainEventManager.registerDomainEvent(ShareDomainEvent.class);
        //ShareDomainEvent事件订阅
        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(ShareDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(s.name + "shareTest1");

        }), "shareTest1");

        //ShareDomainEvent事件订阅
        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(ShareDomainEvent.class, s -> {

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
     * 带条件的订阅测试输出 test2和test1
     *
     * @throws InterruptedException
     */
    @Test
    public void useConditionTest() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(2);


        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(
                new ProducerCreator("localhost:9876"),
                new ConsumerCreator("localhost:9876"),
                "prod");

        rocketMqDomainEventManager.registerDomainEvent(ShareDomainEvent.class);
        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(ShareDomainEvent.class, s -> {

            System.out.println("test1");
            countDownLatch.countDown();

        }), "test1", (IExecuteCondition<ShareDomainEvent>) iDomainEvent -> iDomainEvent.name.equals("test1"));

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(ShareDomainEvent.class, s -> {

            System.out.println("test2");
            countDownLatch.countDown();

        }), "test2", (IExecuteCondition<ShareDomainEvent>) iDomainEvent -> iDomainEvent.name.equals("test2"));

        rocketMqDomainEventManager.publishEvent(new ShareDomainEvent("100", "test1"));
        rocketMqDomainEventManager.publishEvent(new ShareDomainEvent("100", "test2"));

        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());

    }

    /**
     * 延时订阅
     */
    @Test
    public void delayTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(4);


        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(
                new ProducerCreator("localhost:9876"),
                new ConsumerCreator("localhost:9876"), "",
                new OrderedPerformManager());

        rocketMqDomainEventManager.registerDomainEvent(MyDomainEvent.class);
        rocketMqDomainEventManager.registerDelaySubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(0);

        }), "sub0", null, null, SubscriberDelayLevel.None);

        rocketMqDomainEventManager.registerDelaySubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(1);

        }), "sub1", null, null, SubscriberDelayLevel.Delay1);

        rocketMqDomainEventManager.registerDelaySubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(2);


        }), "sub2", null, null, SubscriberDelayLevel.Delay2);

        rocketMqDomainEventManager.registerDelaySubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(3);


        }), "sub3", null, null, SubscriberDelayLevel.Delay3);


        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("100"));


        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(60000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }

    /**
     * 测试重试执行
     *
     * @throws InterruptedException
     */
    @Test
    public void retryTest() throws InterruptedException {
        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(
                new ProducerCreator("localhost:9876"),
                new ConsumerCreator("localhost:9876"), "",
                new OrderedPerformManager());


        rocketMqDomainEventManager.registerDomainEvent(MyDomainEvent.class);

        CountDownLatch countDownLatch = new CountDownLatch(3);

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {

            if (countDownLatch.getCount() > 0) {
                countDownLatch.countDown();
                System.out.println(s.name + "run error " + countDownLatch.getCount());
                throw new RuntimeException("test exception");
            }
            System.out.println("run ok");

        }), "sub1");

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
            System.out.println("run ok sub2");

        }), "sub2");

        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("100"));


        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(60000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }
}
