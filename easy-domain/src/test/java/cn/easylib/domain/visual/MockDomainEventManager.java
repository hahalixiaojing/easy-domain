package cn.easylib.domain.visual;

import cn.easylib.domain.application.subscriber.AbstractSubscriberKey;
import cn.easylib.domain.application.subscriber.IDomainEventManager;
import cn.easylib.domain.event.SubscriberFactory;
import cn.easylib.domain.event.ThreadPoolTaskDomainEventManager;

public class MockDomainEventManager {

    public static IDomainEventManager mockIDomainEventManager() {

        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager();
        manager.registerDomainEvent(TestEvent.class);

        manager.registerSubscriber(TestEventSubscriberKey.SUB1, SubscriberFactory.build(TestEvent.class, s -> {
        }), TestEventSubscriberKey.SUB2);

        manager.registerSubscriber(TestEventSubscriberKey.SUB2, SubscriberFactory.build(TestEvent.class, s -> {
        }), TestEventSubscriberKey.SUB3);

        manager.registerSubscriber(TestEventSubscriberKey.SUB3, SubscriberFactory.build(TestEvent.class, s -> {
        }));

        return manager;

    }

    public static AbstractSubscriberKey mockAbstractSubscriberKey() {
        return new AbstractSubscriberKey() {
            @Override
            protected void populateKeys() {

            }
        };
    }
}
