package cn.easylib.domain.application.subscriber;

import cn.easylib.domain.event.IDomainEvent;


public interface IDomainEventSubscriber<T extends IDomainEvent> extends ISubscriber {

	void handleEvent(T aDomainEvent);
}
