package easy.domain.application.subscriber;

import easy.domain.event.IDomainEvent;

/**
 * @author lixiaojing10
 * @date 2021/3/18 3:43 下午
 */
public interface ISubscriberFactory {

    interface Handle<T extends IDomainEvent> {
        void handleEvent(T t);
    }
    <T extends IDomainEvent> ISubscriber build(Class<T> cls, Handle<T> handle);
}


