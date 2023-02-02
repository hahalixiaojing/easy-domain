package cn.easylib.domain.visual;

import cn.easylib.domain.application.subscriber.IDomainEventManager;
import cn.easylib.domain.application.subscriber.ISubscriberFactory;
import cn.easylib.domain.event.ThreadPoolSubscriberFactory;
import cn.easylib.domain.event.ThreadPoolTaskDomainEventManager;

public class MockDomainEventManager {

    public static IDomainEventManager mockIDomainEventManager() {
        ISubscriberFactory factory = new ThreadPoolSubscriberFactory();

        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager();
        manager.registerDomainEvent(TestEvent.class);

        manager.registerSubscriber(factory.build(TestEvent.class, s -> {
        }), TestEventSubscriberKey.SUB1, TestEventSubscriberKey.SUB2);

        manager.registerSubscriber(factory.build(TestEvent.class, s -> {
        }), TestEventSubscriberKey.SUB2, TestEventSubscriberKey.SUB3);

        manager.registerSubscriber(factory.build(TestEvent.class, s -> {
        }), TestEventSubscriberKey.SUB3);

        return manager;

    }
}
