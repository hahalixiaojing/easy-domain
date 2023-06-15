package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.IDomainEventManager;

public abstract class BaseEventInitSubscriber<T extends IDomainEvent> {
    protected final IDomainEventManager evtManager;

    protected BaseEventInitSubscriber(IDomainEventManager evtManager,
                                      Class<T> eventClass) {
        this.evtManager = evtManager;
        this.initDomainEvent(eventClass);
        this.initEventHandler(eventClass);
    }

    protected void initDomainEvent(Class<T> eventClass) {
        evtManager.registerDomainEvent(eventClass);
    }

    protected abstract void initEventHandler(Class<T> eventClass);
}

