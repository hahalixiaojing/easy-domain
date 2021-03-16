package easy.domain.application.subscriber;

import easy.domain.event.IDomainEvent;

/**
 * 发布和注册事件接口定义
 *
 * @author lixiaojing3
 * @date 2020-02-10 23:42
 */
public interface IDomainEventManager {
    /**
     * 注册领域事件
     *
     * @param domainEventType
     */
    void registerDomainEvent(Class<?> domainEventType);

    /**
     * 注册事件订阅
     *
     * @param subscriber 订阅处理器
     * @param alias      订阅处理器别名
     */
    void registerSubscriber(ISubscriber subscriber, String alias);

    /**
     * 注册事件订阅
     *
     * @param subscriber 订阅处理器
     * @param alias      订阅处理器别名
     * @param condition  订阅触发的条件
     */
    void registerSubscriber(ISubscriber subscriber, String alias, IExecuteCondition condition);

    /**
     * 发布领域事件
     *
     * @param obj 领域事件对象
     * @param <T> 领域事件类型
     */
    <T extends IDomainEvent> void publishEvent(T obj);

    /**
     * 向指定的订阅发布领域事件
     *
     * @param obj        领域事件对象
     * @param subscriber 订阅者的名称
     * @param <T>        领域事件类型
     */
    <T extends IDomainEvent> void publishEvent(T obj, String subscriber);
}
