package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.IExecuteCondition;
import cn.easylib.domain.application.subscriber.ISubscriber;

public abstract class AbstractSubscriberHandler<T extends IDomainEvent> {

    private final Class<T> cls;

    public AbstractSubscriberHandler(Class<T> cls) {
        this.cls = cls;
    }

    protected abstract void handle(T event);

    public boolean runCondition(T event) {
        return true;
    }

    public ISubscriber handle() {
        return SubscriberFactory.build(cls, this::handle);
    }

    public IExecuteCondition<T> runCondition() {
        return SubscriberFactory.buildCondition(cls, this::runCondition);

    }
}
