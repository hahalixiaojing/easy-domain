package cn.easylib.domain.application;

import cn.easylib.domain.application.subscriber.IDomainEventManager;
import cn.easylib.domain.application.subscriber.IExecuteCondition;
import cn.easylib.domain.application.subscriber.ISubscriber;
import cn.easylib.domain.event.IDomainEvent;
import cn.easylib.domain.event.ThreadPoolTaskDomainEventManager;

/**
 * 应用服务层事件基类
 */
@Deprecated
public abstract class BaseApplication implements IApplication {

    private final IDomainEventManager manager;

    /**
     * 使用默认 事件处理器的构造函数
     */
    protected BaseApplication() {
        this.manager = new ThreadPoolTaskDomainEventManager();
    }

    /**
     * 带事件处理器的构造函数
     *
     * @param manager 事件处理器
     */
    protected BaseApplication(IDomainEventManager manager) {
        this.manager = manager;
    }

    /**
     * 注册事件
     *
     * @param domainEventType 事件类型
     */
    public void registerDomainEvent(Class<?> domainEventType) {
        this.manager.registerDomainEvent(domainEventType);
    }

    /**
     * 注册事件订阅
     *
     * @param subscriber     订阅实现
     * @param subscriberName 订阅名称
     */
    public void registerSubscriber(ISubscriber subscriber, String subscriberName) {
        this.manager.registerSubscriber(subscriberName, subscriber);
    }

    public void registerSubscriber(ISubscriber subscriber, String subscriberName, String dependSubscriber) {
        this.manager.registerSubscriber(subscriberName, subscriber, dependSubscriber);
    }


    /**
     * 注册事件订阅
     *
     * @param subscriber     订阅实现
     * @param subscriberName 订阅名称
     * @param condition      执行条件
     */
    public void registerSubscriber(ISubscriber subscriber, String subscriberName, IExecuteCondition condition) {
        this.manager.registerSubscriber(subscriberName, subscriber, condition);
    }

    /**
     * 发布事件
     *
     * @param obj 数据象
     * @param <T> 事件类型
     */
    protected <T extends IDomainEvent> void publishEvent(T obj) {
        this.manager.publishEvent(obj);
    }

    /**
     * 发布事件
     *
     * @param obj       数据对象
     * @param subscribe 指定订阅名称
     * @param <T>       事件类型
     */
    protected <T extends IDomainEvent> void publishEvent(T obj, String subscribe) {
        this.manager.publishEvent(obj, subscribe);

    }

    protected <T extends IDomainEvent> void publishEvent(T obj, String subscribe, boolean onlyThis) {
        this.manager.publishEvent(obj, subscribe, onlyThis);
    }
}