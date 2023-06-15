package cn.easylib.domain.application.subscriber;

import cn.easylib.domain.event.IDomainEvent;

public interface IHandle<T extends IDomainEvent> {
    void handleEvent(T t);
}
