package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.IExecuteCondition;
import cn.easylib.domain.application.subscriber.ISubscriber;

public abstract class AbstractSubscriberRunner<T extends IDomainEvent> {

    private final Class<T> cls;

    public AbstractSubscriberRunner(Class<T> cls) {
        this.cls = cls;
    }

    protected abstract void run(T event);

    public boolean runCondition(T event) {
        return true;
    }

    public ISubscriber run() {
        return SubscriberFactory.build(cls, this::run);
    }

    public IExecuteCondition<T> runCondition() {
        return SubscriberFactory.buildCondition(cls, this::runCondition);

    }
}
