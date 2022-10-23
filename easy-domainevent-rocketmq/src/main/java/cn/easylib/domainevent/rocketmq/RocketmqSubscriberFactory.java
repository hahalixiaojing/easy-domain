package cn.easylib.domainevent.rocketmq;

import cn.easylib.domain.application.subscriber.ISubscriber;
import cn.easylib.domain.application.subscriber.ISubscriberFactory;
import cn.easylib.domain.event.IDomainEvent;

public class RocketmqSubscriberFactory implements ISubscriberFactory {
    @Override
    public <T extends IDomainEvent> ISubscriber build(Class<T> cls, Handle<T> handle) {
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
