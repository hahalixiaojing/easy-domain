package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.IDomainEventSubscriber;
import cn.easylib.domain.application.subscriber.ISubscriber;
import cn.easylib.domain.application.subscriber.ISubscriberFactory;

/**
 * @author lixiaojing
 */
@Deprecated
public class ThreadPoolSubscriberFactory implements ISubscriberFactory {
    @Override
    public <T extends IDomainEvent> ISubscriber build(Class<T> cls, Handle<T> handle) {
        return new IDomainEventSubscriber<T>() {

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
