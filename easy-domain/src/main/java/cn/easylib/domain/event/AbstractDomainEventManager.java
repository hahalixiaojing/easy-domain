package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDomainEventManager implements IDomainEventManager {

    protected final String environmentName;
    protected final ConcurrentHashMap<String, Map<String, SubscriberInfo>> subscribers = new ConcurrentHashMap<>();
    private final IExecuteCondition<IDomainEvent> condition = new DefaultExecuteCondition<>();

    protected final IOrderedPerformManager performManager;

    public AbstractDomainEventManager(String environmentName, IOrderedPerformManager performManager) {
        this.environmentName = environmentName;
        this.performManager = performManager;
    }

    protected EventNameInfo getEventName(Class<?> eventType){
        EventName alias = eventType.getAnnotation(EventName.class);
        String evtName;
        String shareTopicName;
        if (alias == null) {
            evtName = eventType.getSimpleName();
            shareTopicName = "";
        } else {
            evtName = alias.value();
            shareTopicName = alias.shareTopicName();
        }
        if (StringUtils.isNotBlank(this.environmentName)) {
            evtName = this.environmentName + "_" + evtName;
            if (StringUtils.isNotBlank(shareTopicName)) {
                shareTopicName = this.environmentName + "_" + shareTopicName;
            }
        }
        return new EventNameInfo(evtName, shareTopicName);
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, String alias) {
        this.registerSubscriber(subscriber, alias, "");
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, String alias, String dependSubscriber) {
        this.registerSubscriber(subscriber, alias, condition, dependSubscriber);

    }

    @Override
    public void registerSubscriber(ISubscriber subscriber, String alias, IExecuteCondition condition) {
        this.registerSubscriber(subscriber, alias, condition, "");
    }

    @Override
    public void registerSubscriber(ISubscriber subscriber,
                                   String alias,
                                   IExecuteCondition condition,
                                   String dependSubscriber) {
        EventNameInfo event = getEventName(subscriber.subscribedToEventType());
        if (this.subscribers.containsKey(event.eventName)) {

            Map<String, SubscriberInfo> stringISubscriberMap = this.subscribers.get(event.eventName);
            if (stringISubscriberMap.containsKey(alias)) {
                throw new IllegalArgumentException(alias + " is duplication");
            }

            this.subscribers.get(event.eventName).put(alias,
                    new SubscriberInfo(subscriber, alias, condition));

        } else {

            Map<String, SubscriberInfo> subscriberMap = new HashMap<>();
            subscriberMap.put(alias, new SubscriberInfo(subscriber, alias, condition));
            this.subscribers.put(event.eventName, subscriberMap);

        }
        if (this.performManager != null) {
            this.performManager.registerSubscriber(event.eventName,
                    alias,
                    dependSubscriber);
        }
    }
}
