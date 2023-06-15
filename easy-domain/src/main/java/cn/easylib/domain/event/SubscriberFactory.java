package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.IExecuteCondition;
import cn.easylib.domain.application.subscriber.IHandle;
import cn.easylib.domain.application.subscriber.ISubscriber;

import java.util.function.Predicate;

public class SubscriberFactory {

    private SubscriberFactory() {
    }

    public static <T extends IDomainEvent> ISubscriber build(Class<T> cls, IHandle<T> handle) {
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

    public static <T extends IDomainEvent> IExecuteCondition<T> buildCondition(Class<T> cls,
                                                                               Predicate<T> predicate) {

        return predicate::test;


    }
}
