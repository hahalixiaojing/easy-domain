package easy.domain.application.subscriber;

import easy.domain.event.IDomainEvent;

/**
 * 事件订阅接口
 * @author lixiaojing
 * @param <T>
 */
public interface IDomainEventSubscriber<T extends IDomainEvent> extends ISubscriber {
    /**
     * 事件处理
     */
	void handleEvent(T aDomainEvent);
}
