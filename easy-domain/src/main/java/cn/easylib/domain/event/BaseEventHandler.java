package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.IExecuteCondition;
import cn.easylib.domain.application.subscriber.ISubscriber;

public abstract class BaseEventHandler<T extends IDomainEvent> {

    private final Class<T> cls;

    public BaseEventHandler(Class<T> cls) {
        this.cls = cls;
    }

    protected abstract void handle(T event);

    protected boolean runCondition(T event) {
        return true;
    }

    public ISubscriber handle() {
        return SubscriberFactory.build(cls, this::handle);
    }

    public IExecuteCondition<T> runCondition() {
        return SubscriberFactory.buildCondition(cls, this::runCondition);

    }
}
