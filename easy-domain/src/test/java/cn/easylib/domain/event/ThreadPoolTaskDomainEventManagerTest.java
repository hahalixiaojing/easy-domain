package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.OrderedPerformManager;
import cn.easylib.domain.application.subscriber.IExecuteCondition;
import cn.easylib.domain.application.subscriber.ISubscriberFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于线程池的领域事件发布订阅管理器
 *
 * @author lixiaojing
 * @date 2020/9/13 12:41 下午
 */
public class ThreadPoolTaskDomainEventManagerTest {

    /**
     * 验证发布事件，执行指定订阅，并执行指定订阅的依赖的订阅
     * 执行 sub2 -> sub1
     *
     * @throws InterruptedException
     */
    @Test
    public void publishEventOneTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ISubscriberFactory factory = new ThreadPoolSubscriberFactory();

        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager();
        manager.registerDomainEvent(TestDomainEvent.class);

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(1);

        }), "sub1", "sub2");

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(2);
        }), "sub2", "sub3");

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(3);
        }), "sub3");
        //发布事件，执行sub2的订阅，并执行依赖sub2的订阅sub1
        manager.publishEvent(new TestDomainEvent(""), "sub2");

        countDownLatch.await();

        Assert.assertEquals(2, atomicInteger.get());
    }

    /**
     * 验证执行指定的订阅，但只是指行当前指定的，不执行依赖的
     * 执行 sub2
     *
     * @throws InterruptedException
     */
    @Test
    public void publishEventOneOnlyTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ISubscriberFactory factory = new ThreadPoolSubscriberFactory();

        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager();
        manager.registerDomainEvent(TestDomainEvent.class);

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(1);

        }), "sub1", "sub2");

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(2);
        }), "sub2", "sub3");

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(3);
        }), "sub3");
        //发布事件，但只执行sub2的订阅，不执行依赖sub2的sub1的订阅
        manager.publishEvent(new TestDomainEvent(""), "sub2", true);

        countDownLatch.await();

        Assert.assertEquals(1, atomicInteger.get());
    }

    /**
     * 验证订阅按依赖顺序执行 sub3->sub2->sub1 输出 3 2 1
     *
     * @throws InterruptedException
     */
    @Test
    public void oneEventTwoSubscriberOrderedTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ISubscriberFactory factory = new ThreadPoolSubscriberFactory();

        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager();
        manager.registerDomainEvent(TestDomainEvent.class);

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(1);

        }), "sub1", "sub2");

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(2);
        }), "sub2", "sub3");

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(3);
        }), "sub3");

        manager.publishEvent(new TestDomainEvent(""));

        countDownLatch.await();

        Assert.assertEquals(3, atomicInteger.get());
    }

    /**
     * 验证订阅没有依赖无特定顺序执行 输出  1 2 3
     *
     * @throws InterruptedException
     */
    @Test
    public void oneEventTwoSubscriberTest() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(3);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ISubscriberFactory factory = new ThreadPoolSubscriberFactory();

        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager();
        manager.registerDomainEvent(TestDomainEvent.class);

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(1);
            System.out.println(Thread.currentThread().getName());

        }), "sub1");

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(2);
            System.out.println(Thread.currentThread().getName());


        }), "sub2");

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(3);
            System.out.println(Thread.currentThread().getName());


        }), "sub3");

        manager.publishEvent(new TestDomainEvent(""));

        countDownLatch.await();

        Assert.assertEquals(3, atomicInteger.get());
    }

    /**
     * 验证订阅按满足特定条件执行，输出 2
     *
     * @throws InterruptedException
     */
    @Test
    public void oneEventTwoSubscriberWithConditionTest() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ISubscriberFactory factory = new ThreadPoolSubscriberFactory();


        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager();
        manager.registerDomainEvent(TestDomainEvent.class);
        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {

            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(1);

            //需要满足IExecuteCondition 条件才能执行
        }), "sub1", (IExecuteCondition<TestDomainEvent>) iDomainEvent -> iDomainEvent.getBusinessId().equals("1"));

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {
            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(2);


        }), "sub2");

        TestDomainEvent testDomainEvent = new TestDomainEvent("2");
        manager.publishEvent(testDomainEvent);

        countDownLatch.await();

        Assert.assertEquals(1, atomicInteger.get());

    }

    @Test
    public void orderExecuteWithConditionTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        ISubscriberFactory factory = new ThreadPoolSubscriberFactory();

        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager(1, 3, 200,new OrderedPerformManager());

        manager.registerDomainEvent(TestDomainEvent.class);

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {
                    countDownLatch.countDown();
                    System.out.println("sub1");
                }), "sub1", "sub2"
        );

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {
                    countDownLatch.countDown();
                    System.out.println("sub2");
                }), "sub2", (IExecuteCondition<TestDomainEvent>) evt -> evt.getBusinessId().equals("100")
        );
        //发布事件 name=100 执行 sub2 ,sub1
        manager.publishEvent(new TestDomainEvent("100"));
        //发布事件 name=200 不执行 sub2 和 sub1
        manager.publishEvent(new TestDomainEvent("200"));

        Thread.sleep(5000);

        Assert.assertEquals(0L, countDownLatch.getCount());

    }

    /**
     * 验证订阅执行重试 输出 1 1 1 1
     *
     * @throws InterruptedException
     */
    @Test
    public void oneEventOneSubscriberUseRetry() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(4);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ISubscriberFactory factory = new ThreadPoolSubscriberFactory();


        //最大重试次数，不算首次调用
        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager(1, 3, 200,new OrderedPerformManager());
        manager.registerDomainEvent(TestDomainEvent.class);

        manager.registerSubscriber(factory.build(TestDomainEvent.class, s -> {
            countDownLatch.countDown();
            atomicInteger.incrementAndGet();
            System.out.println(1);
            //模拟异常触发重试
            throw new RuntimeException();

        }), "sub2");

        manager.publishEvent(new TestDomainEvent(""));

        countDownLatch.await();

        Assert.assertEquals(4, atomicInteger.get());

    }
}