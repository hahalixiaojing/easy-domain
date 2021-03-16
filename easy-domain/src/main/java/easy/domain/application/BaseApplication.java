package easy.domain.application;

import easy.domain.application.subscriber.IDomainEventManager;
import easy.domain.application.subscriber.IExecuteCondition;
import easy.domain.application.subscriber.ISubscriber;
import easy.domain.event.IDomainEvent;
import easy.domain.event.ThreadPoolTaskDomainEventManager;

/**
 * 应用服务层事件基类
 */
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
        this.manager.registerSubscriber(subscriber, subscriberName);
    }

    /**
     * 注册事件订阅
     *
     * @param subscriber     订阅实现
     * @param subscriberName 订阅名称
     * @param condition      执行条件
     */
    public void registerSubscriber(ISubscriber subscriber, String subscriberName, IExecuteCondition condition) {
        this.manager.registerSubscriber(subscriber, subscriberName, condition);

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
}