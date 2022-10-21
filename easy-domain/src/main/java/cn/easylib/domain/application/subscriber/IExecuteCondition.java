package cn.easylib.domain.application.subscriber;

import cn.easylib.domain.event.IDomainEvent;


public interface IExecuteCondition<T extends IDomainEvent> {
    boolean isExecute(T t);
}
