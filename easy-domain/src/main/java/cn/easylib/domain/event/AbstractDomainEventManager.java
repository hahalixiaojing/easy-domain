package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDomainEventManager implements IDomainEventManager {

    private final String environmentName;
    private final Map<String, Map<String, SubscriberInfo>> subscribers = new HashMap<>();
    private final IExecuteCondition<IDomainEvent> condition = new DefaultExecuteCondition<>();

    private final IOrderedPerformManager performManager;

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

    protected String getTopicName(EventNameInfo eventNameInfo){
        String topic;
        if (eventNameInfo.shareTopicName == null || eventNameInfo.shareTopicName.equals("")) {
            topic = eventNameInfo.eventName;
        } else {
            topic = eventNameInfo.shareTopicName;
        }
        return topic;
    }

    protected void registerSubscriber(ISubscriber subscriber, String event, String alias, IExecuteCondition iExecuteCondition){
        if (!this.subscribers.containsKey(event)) {

            Map<String, SubscriberInfo> subscriberMap = new HashMap<>();
            subscriberMap.put(alias, new SubscriberInfo(subscriber, alias, iExecuteCondition));

            this.subscribers.put(event, subscriberMap);
        } else {
            Map<String, SubscriberInfo> stringISubscriberMap = this.subscribers.get(event);
            if (stringISubscriberMap.containsKey(alias)) {
                throw new IllegalArgumentException(alias + " is duplication");
            }

            this.subscribers.get(event).put(alias, new SubscriberInfo(subscriber, alias, iExecuteCondition));
        }
    }
}
