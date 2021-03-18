package easy.domainevent.rocketmq;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 测试基于rocketmq的领域事件发布订阅机制
 *
 * @author lixiaojing10
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

        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876", "QQ"), new ConsumerCreator("localhost:9876", "QQ"), "prod");


        rocketMqDomainEventManager.registerDomainEvent(MyDomainEvent.class);
        rocketMqDomainEventManager.registerSubscriber(new AbstractDomainEventSubscriber<MyDomainEvent>() {

            @Override
            public Class<MyDomainEvent> subscribedToEventType() {
                return MyDomainEvent.class;
            }

            @Override
            public void handleEvent(MyDomainEvent aDomainEvent) {
                countDownLatch.countDown();
                System.out.println(aDomainEvent.name + "test1");
            }
        }, "test1");

        rocketMqDomainEventManager.registerSubscriber(new AbstractDomainEventSubscriber<MyDomainEvent>() {

            @Override
            public Class<MyDomainEvent> subscribedToEventType() {
                return MyDomainEvent.class;
            }

            @Override
            public void handleEvent(MyDomainEvent aDomainEvent) {
                countDownLatch.countDown();
                System.out.println(aDomainEvent.name + "test2");
            }
        }, "test2");
        //发送一个事件，分别被订阅test1和test2 处理
        rocketMqDomainEventManager.publishEvent(new MyDomainEvent("abc"));


        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }

    @Test
    public void useShareTopicTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        RocketMqDomainEventManager rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876", "QQ"), new ConsumerCreator("localhost:9876", "QQ"), "prod");

        rocketMqDomainEventManager.registerDomainEvent(ShareDomainEvent.class);
        //ShareDomainEvent事件订阅
        rocketMqDomainEventManager.registerSubscriber(new AbstractDomainEventSubscriber<ShareDomainEvent>() {
            @Override
            public Class<ShareDomainEvent> subscribedToEventType() {
                return ShareDomainEvent.class;
            }

            @Override
            public void handleEvent(ShareDomainEvent aDomainEvent) {
                countDownLatch.countDown();
                System.out.println(aDomainEvent.name + "shareTest1");
            }
        }, "shareTest1");
        rocketMqDomainEventManager.registerSubscriber(new AbstractDomainEventSubscriber<ShareDomainEvent>() {
            @Override
            public Class<ShareDomainEvent> subscribedToEventType() {
                return ShareDomainEvent.class;
            }

            @Override
            public void handleEvent(ShareDomainEvent aDomainEvent) {
                countDownLatch.countDown();
                System.out.println(aDomainEvent.name + "shareTest2");
            }
        }, "shareTest2");

        rocketMqDomainEventManager.publishEvent(new ShareDomainEvent("100", "share"));

        countDownLatch.await(30000, TimeUnit.SECONDS);
        //需要等待mq 更新消费位点
        Thread.sleep(30000);

        Assert.assertEquals(0L, countDownLatch.getCount());
    }
}
