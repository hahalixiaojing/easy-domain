package easy.domainevent.rocketmq;

import cn.easylib.domain.application.BaseApplication;
import cn.easylib.domain.application.subscriber.IDomainEventManager;
import cn.easylib.domain.application.subscriber.ISubscriberFactory;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * 应用服务层，使用rocketmq 实现发布订阅的能力
 * @author lixiaojing
 * @date 2021/3/18 4:37 下午
 */
public class MyApplicationTest {

    private final RocketMqDomainEventManager rocketMqDomainEventManager;

    public MyApplicationTest() {
        this.rocketMqDomainEventManager = new RocketMqDomainEventManager(new ProducerCreator("localhost:9876"), new ConsumerCreator("localhost:9876"), "prod");

    }

    @Test
    public void publish() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //基于rocketmq的发布订阅组件和基于线程池的发布订阅组件，可以无缝切换
        MyApplication myApplication = new MyApplication(this.rocketMqDomainEventManager, new RocketmqSubscriberFactory(), countDownLatch);
//        MyApplication myApplication = new MyApplication(new ThreadPoolSubscriberFactory(), countDownLatch);
        myApplication.create();
        countDownLatch.await();
        //需要等待mq 更新消费位点
        Thread.sleep(30000);
    }
}


class MyApplication extends BaseApplication {
    private final ISubscriberFactory factory;
    private final CountDownLatch countDownLatch;

    public MyApplication(IDomainEventManager manager, ISubscriberFactory factory, CountDownLatch countDownLatch) {
        super(manager);
        this.factory = factory;
        this.countDownLatch = countDownLatch;
        this.initSubscriber();
    }

    public MyApplication(ISubscriberFactory factory, CountDownLatch countDownLatch) {
        this.factory = factory;
        this.countDownLatch = countDownLatch;
        this.initSubscriber();
    }


    public void create() {
        //对领域模型进行操作，操作完成持久化后，发布事件
        this.publishEvent(new MyDomainEvent("123"));
    }

    private void initSubscriber() {

        this.registerDomainEvent(MyDomainEvent.class);

        this.registerSubscriber(factory.build(MyDomainEvent.class, s -> {
            this.countDownLatch.countDown();

            System.out.println("执行相应的操作");

        }), "test1");
    }
}
