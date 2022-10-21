package cn.easylib.domain.application.subscriber;

import cn.easylib.domain.event.IDomainEvent;


public interface ISubscriberFactory {

    interface Handle<T extends IDomainEvent> {
        void handleEvent(T t);
    }
    <T extends IDomainEvent> ISubscriber build(Class<T> cls, Handle<T> handle);
}


