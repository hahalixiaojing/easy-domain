package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.IExecuteCondition;

/**
 * 默认事件订阅执行条件
 *
 * @author lixiaojing
 */
public class DefaultExecuteCondition<T extends IDomainEvent> implements IExecuteCondition<T> {
    @Override
    public boolean isExecute(T t1) {
        return true;
    }
}
