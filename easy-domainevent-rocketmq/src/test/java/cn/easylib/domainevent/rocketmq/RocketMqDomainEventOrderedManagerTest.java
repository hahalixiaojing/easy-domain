package cn.easylib.domainevent.rocketmq;

import cn.easylib.domain.application.subscriber.IExecuteCondition;
import cn.easylib.domain.application.subscriber.ISubscriberFactory;
import cn.easylib.domain.application.subscriber.OrderedPerformManager;
import cn.easylib.domain.event.SubscriberFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author lixiaojing10
 * @date 2021/12/24 10:08 下午
 */
public class RocketMqDomainEventOrderedManagerTest {
    /**
     * 随机执行，订阅执行不分先后顺序
     * r1 ,r2 ,r3 的执行顺序不定
     */
    @Test
    public void randomExecuteTest() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(4);
        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(
                new ProducerCreator("localhost:9876"),
                new ConsumerCreator("localhost:9876"), "",
                new OrderedPerformManager());

        rocketMqDomainEventManager.registerDomainEvent(MyDomainEvent.class);
        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println("r1");
        }), "r1");

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println("r2");
        }), "r2");

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println("r3");
        }), "r3");
        //发布该事件 执行r1 r2 r3 全部订阅
        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("全部执行"));
        //发布带第二个参数的事件，中执行 r3
        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("指定执行r3"), "r3");

        //需要等待mq 更新消费位点
        Thread.sleep(30000);
        Assert.assertEquals(0L, countDownLatch.getCount());
    }

    /**
     * 验证按顺序执行 test3->test2-> test1  ，输出 3 -> 2 -> 1
     *
     * @throws InterruptedException
     */
    @Test
    public void orderExecuteTest1() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(
                new ProducerCreator("localhost:9876"),
                new ConsumerCreator("localhost:9876"), "",
                new OrderedPerformManager());
        rocketMqDomainEventManager.registerDomainEvent(MyDomainEvent.class);
        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println(1);
        }), "test1", "test2");

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println(2);
        }), "test2", "test3");

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println(3);
        }), "test3");
        //发布该事件，按顺序执行 test3->test2->test1
        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("执行全部事件订阅"));
        //只执行test2,不执行test1,最后一个参数true表示只执行当前
        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("执行指定的事件订阅，不执行依赖当前订阅的订阅"), "test2", true);
        //执行 test2，以及依赖test2的test1
        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("执行指定的事件订阅，同时执行依赖当前订阅的订阅"), "test2", false);

        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }

    /**
     * 验证按顺序执行订阅  shareTest1、shareTest3  依赖 shareTest2 执行完成之后再执行
     * shareTest2 ->  shareTest1
     * ->  shareTest3
     *
     * @throws InterruptedException
     */
    @Test
    public void orderExecuteTest2() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
//        RocketmqSubscriberFactory factory = new RocketmqSubscriberFactory();

        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876"), new ConsumerCreator("localhost:9876"), "", new OrderedPerformManager());

        rocketMqDomainEventManager.registerDomainEvent(ShareDomainEvent.class);
        //ShareDomainEvent事件订阅
        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(ShareDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(s.name + "shareTest1");

        }), "shareTest1", "shareTest2");

        //ShareDomainEvent事件订阅
        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(ShareDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(s.name + "shareTest2");

        }), "shareTest2");

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(ShareDomainEvent.class, s -> {

            countDownLatch.countDown();
            System.out.println(s.name + "shareTest3");

        }), "shareTest3", "shareTest2");

        rocketMqDomainEventManager.publishEvent(new ShareDomainEvent("100", "share"));

        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }

    /**
     * 验证两个时间执行指定订阅
     * MyDomainEvent 执行顺序
     * sub1 -> sub2
     * -> sub3
     * ShareDomainEvent 执行顺序
     * shareTest2-> shareTest1 -> shareTest3
     *
     * @throws InterruptedException
     */
    @Test
    public void towEventOrderExecute() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
//        RocketmqSubscriberFactory factory = new RocketmqSubscriberFactory();

        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876"), new ConsumerCreator("localhost:9876"), "", new OrderedPerformManager());

        rocketMqDomainEventManager.registerDomainEvent(ShareDomainEvent.class);
        rocketMqDomainEventManager.registerDomainEvent(MyDomainEvent.class);

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
            System.out.println("sub1");

        }), "sub1");
        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
            System.out.println("sub2");

        }), "sub2", "sub1");
        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
            System.out.println("sub3");

        }), "sub3", "sub1");

        //ShareDomainEvent事件订阅
        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(ShareDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println(s.name + "shareTest1");
        }), "shareTest1", "shareTest2");

        //ShareDomainEvent事件订阅
        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(ShareDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println(s.name + "shareTest2");
        }), "shareTest2");

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(ShareDomainEvent.class, s -> {
            countDownLatch.countDown();
            System.out.println(s.name + "shareTest3");

        }), "shareTest3", "shareTest1");

        rocketMqDomainEventManager.publishEvent(new ShareDomainEvent("100", "share"));
        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("100"));

        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }

    @Test
    public void orderExecuteWithConditionTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

//        RocketmqSubscriberFactory factory = new RocketmqSubscriberFactory();
        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876"), new ConsumerCreator("localhost:9876"), "", new OrderedPerformManager());

        rocketMqDomainEventManager.registerDomainEvent(MyDomainEvent.class);

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
                    countDownLatch.countDown();
                    System.out.println("sub1");
                }), "sub1", "sub2"
        );

        rocketMqDomainEventManager.registerSubscriber(SubscriberFactory.build(MyDomainEvent.class, s -> {
                    countDownLatch.countDown();
                    System.out.println("sub2");
                }), "sub2", (IExecuteCondition<MyDomainEvent>) evt -> evt.name.equals("100")
        );
        //发布事件 name=100 执行 sub2 ,sub1
        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("100"));
        //发布事件 name=200 不执行 sub2 和 sub1
        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("200"));

        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());

    }

    /**
     * 测试重试执行,
     * sub2 依赖 sub1
     * sub2执行重试，直到重试成功后 再执行sub1
     *
     * @throws InterruptedException
     */
    @Test
    public void retryOrderExecuteTest() throws InterruptedException {
//        RocketmqSubscriberFactory factory = new RocketmqSubscriberFactory();
        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876"), new ConsumerCreator("localhost:9876"), "", new OrderedPerformManager());


        rocketMqDomainEventManager.registerDomainEvent(MyDomainEvent.class);

        CountDownLatch countDownLatch = new CountDownLatch(2);

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

        }), "sub2", "sub1");

        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("100"));


        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(60000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }


}
