package cn.easylib.domain.application.subscriber;

import cn.easylib.domain.event.IDomainEvent;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.Map;

public interface IDomainEventManager {

    default Map<String, List<String>> allEvents() {
        throw new NotImplementedException("NotImplemented");
    }


    default List<OrderedPerformManager.OrderData> findEventSubscriberInfo(String eventName) {
        throw new NotImplementedException("NotImplemented");
    }


    /**
     * 注册领域事件
     *
     * @param domainEventType 事件类型
     */
    void registerDomainEvent(Class<?> domainEventType);

    void registerSubscriber(String alias, ISubscriber subscriber);

    default void registerSubscriber(String alias, ISubscriber subscriber,
                                    String dependSubscriber) {
    }


    void registerSubscriber(String alias, ISubscriber subscriber,
                            IExecuteCondition condition);

    default void registerSubscriber(String alias, ISubscriber subscriber,
                                    IExecuteCondition condition,
                                    String dependSubscriber) {
    }

    default void registerDelaySubscriber(String alias, ISubscriber subscriber,
                                         IExecuteCondition condition,
                                         SubscriberDelayLevel delayLevel,
                                         String dependSubscriber

                                         ) {
    }


    <T extends IDomainEvent> void publishEvent(T obj);


    <T extends IDomainEvent> void publishEvent(T obj, String subscriber);

    default <T extends IDomainEvent> void publishEvent(T obj, String subscriber, boolean onlyThis) {
    }

    default <T extends IDomainEvent> void publishEventList(List<T> evtObjectList) {

    }
}
