package cn.easylib.domainevent.rocketmq;

import com.alibaba.fastjson.JSON;
import cn.easylib.domain.application.subscriber.IDomainEventSubscriber;
import cn.easylib.domain.event.IDomainEvent;

/**
 * @author lixiaojing
 * @date 2021/3/16 7:50 下午
 */
public abstract class AbstractDomainEventSubscriber<T extends IDomainEvent> implements IDomainEventSubscriber<T> {

    @SuppressWarnings("unchecked")
    public void handleEvent(String aDomainEvent) {
        Object object = JSON.parseObject(aDomainEvent, this.subscribedToEventType());
        this.handleEvent((T) object);
    }
    @SuppressWarnings("unchecked")
    public T parseEvent(String aDomainEvent){
        return (T)JSON.parseObject(aDomainEvent, this.subscribedToEventType());
    }
}

