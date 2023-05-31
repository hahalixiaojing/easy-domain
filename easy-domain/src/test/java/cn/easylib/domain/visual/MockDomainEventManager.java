package cn.easylib.domain.visual;

import cn.easylib.domain.application.subscriber.IDomainEventManager;
import cn.easylib.domain.event.SubscriberFactory;
import cn.easylib.domain.event.ThreadPoolTaskDomainEventManager;

public class MockDomainEventManager {

    public static IDomainEventManager mockIDomainEventManager() {

        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager();
        manager.registerDomainEvent(TestEvent.class);

        manager.registerSubscriber(SubscriberFactory.build(TestEvent.class, s -> {
        }), TestEventSubscriberKey.SUB1, TestEventSubscriberKey.SUB2);

        manager.registerSubscriber(SubscriberFactory.build(TestEvent.class, s -> {
        }), TestEventSubscriberKey.SUB2, TestEventSubscriberKey.SUB3);

        manager.registerSubscriber(SubscriberFactory.build(TestEvent.class, s -> {
        }), TestEventSubscriberKey.SUB3);

        return manager;

    }
}
