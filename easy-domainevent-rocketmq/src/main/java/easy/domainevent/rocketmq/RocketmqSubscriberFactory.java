package easy.domainevent.rocketmq;

import easy.domain.application.subscriber.ISubscriber;
import easy.domain.application.subscriber.ISubscriberFactory;
import easy.domain.event.IDomainEvent;

/**
 * @author lixiaojing10
 * @date 2021/3/18 4:08 下午
 */
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
