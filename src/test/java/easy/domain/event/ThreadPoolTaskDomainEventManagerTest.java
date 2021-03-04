package easy.domain.event;

import easy.domain.application.subscriber.IDomainEventSubscriber;
import easy.domain.application.subscriber.IExecuteCondition;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于线程池的领域事件发布订阅管理器
 *
 * @author lixiaojing10
 * @date 2020/9/13 12:41 下午
 */
public class ThreadPoolTaskDomainEventManagerTest {

    @Test
    public void oneEventTwoSubscriberTest() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(2);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager();
        manager.registerDomainEvent(TestDomainEvent.class);
        manager.registerSubscriber(new IDomainEventSubscriber<TestDomainEvent>() {
            @Override
            public Class<TestDomainEvent> subscribedToEventType() {
                return TestDomainEvent.class;
            }

            @Override
            public void handleEvent(TestDomainEvent aDomainEvent) {

                countDownLatch.countDown();
                atomicInteger.incrementAndGet();

            }
        }, "sub1");
        manager.registerSubscriber(new IDomainEventSubscriber<TestDomainEvent>() {
            @Override
            public Class<TestDomainEvent> subscribedToEventType() {
                return TestDomainEvent.class;
            }

            @Override
            public void handleEvent(TestDomainEvent aDomainEvent) {
                countDownLatch.countDown();
                atomicInteger.incrementAndGet();
            }
        }, "sub2");

        manager.publishEvent(new TestDomainEvent(""));

        countDownLatch.await();

        Assert.assertEquals(2, atomicInteger.get());
    }

    @Test
    public void oneEventTwoSubscriberWithConditionTest() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager();
        manager.registerDomainEvent(TestDomainEvent.class);
        manager.registerSubscriber(new IDomainEventSubscriber<TestDomainEvent>() {
            @Override
            public Class<TestDomainEvent> subscribedToEventType() {
                return TestDomainEvent.class;
            }

            @Override
            public void handleEvent(TestDomainEvent aDomainEvent) {

                countDownLatch.countDown();
                atomicInteger.incrementAndGet();

            }
            //满足条件时执行
        }, "sub1", (IExecuteCondition<TestDomainEvent>) iDomainEvent -> iDomainEvent.getBusinessId().equals("1"));

        manager.registerSubscriber(new IDomainEventSubscriber<TestDomainEvent>() {
            @Override
            public Class<TestDomainEvent> subscribedToEventType() {
                return TestDomainEvent.class;
            }

            @Override
            public void handleEvent(TestDomainEvent aDomainEvent) {
                countDownLatch.countDown();
                atomicInteger.incrementAndGet();
            }
        }, "sub2");

        TestDomainEvent testDomainEvent = new TestDomainEvent("2");
        manager.publishEvent(testDomainEvent);

        countDownLatch.await();

        Assert.assertEquals(1, atomicInteger.get());

    }

    @Test
    public void oneEventOneSubscriberUseRetry() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(4);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        //最大重试次数，不算首次调用
        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager(1, 3, 200);
        manager.registerDomainEvent(TestDomainEvent.class);

        manager.registerSubscriber(new IDomainEventSubscriber<TestDomainEvent>() {
            @Override
            public Class<TestDomainEvent> subscribedToEventType() {
                return TestDomainEvent.class;
            }

            @Override
            public void handleEvent(TestDomainEvent aDomainEvent) {
                countDownLatch.countDown();
                atomicInteger.incrementAndGet();
                //模拟异常触发重试
                throw new RuntimeException();
            }
        }, "sub2");

        manager.publishEvent(new TestDomainEvent(""));

        countDownLatch.await();

        Assert.assertEquals(4, atomicInteger.get());

    }
}