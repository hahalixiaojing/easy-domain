package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.ISubscriber;
import cn.easylib.domain.application.subscriber.ISubscriberFactory;

public class SubscriberFactory {

    private SubscriberFactory() {
    }

    public static <T extends IDomainEvent> ISubscriber build(Class<T> cls, ISubscriberFactory.Handle<T> handle) {
        return new AbstractDomainEventSubscriber<T>() {
            @Override
            public Class<T> subscribedToEventType() {
                return cls;
            }

            @Override
            public void handleEvent(T aDomainEvent) {
                handle.handleEvent(aDomainEvent);
            }
        };
    }
}
