package easy.domain.event;

import easy.domain.application.subscriber.IDomainEventSubscriber;
import easy.domain.application.subscriber.ISubscriber;
import easy.domain.application.subscriber.ISubscriberFactory;

/**
 * @author lixiaojing
 * @date 2021/3/19 5:34 下午
 */
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
