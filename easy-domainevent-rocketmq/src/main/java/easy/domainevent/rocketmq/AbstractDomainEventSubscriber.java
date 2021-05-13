package easy.domainevent.rocketmq;

import com.alibaba.fastjson.JSON;
import easy.domain.application.subscriber.IDomainEventSubscriber;
import easy.domain.event.IDomainEvent;

/**
 * @author lixiaojing
 * @date 2021/3/16 7:50 下午
 */
public abstract class AbstractDomainEventSubscriber<T extends IDomainEvent> implements IDomainEventSubscriber<T> {
    public void handleEvent(String aDomainEvent) {
        Object object = JSON.parseObject(aDomainEvent, this.subscribedToEventType());
        this.handleEvent((T) object);
    }
}

