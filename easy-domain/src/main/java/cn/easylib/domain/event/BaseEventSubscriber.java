package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.IDomainEventManager;

public abstract class BaseEventSubscriber<T extends IDomainEvent> {
    protected final IDomainEventManager evtManager;

    protected BaseEventSubscriber(IDomainEventManager evtManager,
                                  Class<T> eventClass) {
        this.evtManager = evtManager;
        this.initDomainEvent(eventClass);
    }

    protected void initDomainEvent(Class<T> eventClass) {
        evtManager.registerDomainEvent(eventClass);
    }

    protected void initEventHandler() {
    }
}

