package cn.easylib.domain.application.subscriber;

import cn.easylib.domain.event.IDomainEvent;

/**
 * @author lixiaojing
 * @date 2019/3/29 2:45 PM
 */
public interface IExecuteCondition<T extends IDomainEvent> {
    /**
     * 判断订阅是否可以执行
     *
     * @param t 事件对象
     * @return true 表示可以执行， false 表示不可以执行
     */
    boolean isExecute(T t);
}
