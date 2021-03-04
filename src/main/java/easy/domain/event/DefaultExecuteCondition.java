package easy.domain.event;

import easy.domain.application.subscriber.IExecuteCondition;

/**
 * 默认事件订阅执行条件
 *
 * @author lixiaojing10
 * @date 2019/3/29 2:48 PM
 */
public class DefaultExecuteCondition<T extends IDomainEvent> implements IExecuteCondition<T> {
    @Override
    public boolean isExecute(T t1) {
        return true;
    }
}
