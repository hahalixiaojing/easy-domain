package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public abstract class AbstractDomainEventManager implements IDomainEventManager {

    protected final String environmentName;
    protected final ConcurrentHashMap<String, Map<String, SubscriberInfo>> subscribers = new ConcurrentHashMap<>();
    private final IExecuteCondition<IDomainEvent> defaultCondition = new DefaultExecuteCondition<>();

    protected final IOrderedPerformManager performManager;

    protected AbstractDomainEventManager(String environmentName, IOrderedPerformManager performManager) {
        this.environmentName = environmentName;
        this.performManager = performManager;
    }

    protected Map<String, SubscriberInfo> filterSubscriberInfoMap(EventNameInfo eventNameInfo) {

        Map<String, SubscriberInfo> subscriberMap = this.subscribers.get(eventNameInfo.eventName);

        if (subscriberMap != null && this.performManager != null) {
            List<String> rootSubscribers = this.performManager.selectRootSubscribers(eventNameInfo.eventName);
            //如果有执行顺序管理，先查找到根
            return subscriberMap.entrySet().stream()
                    .filter(s -> rootSubscribers.contains(s.getKey()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        return Optional.ofNullable(subscriberMap).orElse(new HashMap<>());
    }

    protected <T extends IDomainEvent> SubscriberInfo findSubscriberInfo(T obj, String subscriber, EventNameInfo eventName) {
        Map<String, SubscriberInfo> subscriberMap = this.subscribers.get(eventName.eventName);
        if (subscriberMap == null) {
            return null;
        }
        SubscriberInfo subscriberInfo = subscriberMap.get(subscriber);
        if (subscriberInfo == null) {
            return null;
        }
        IExecuteCondition condition = subscriberInfo.getCondition();
        if (!this.executeCheck(obj, condition)) {
            return null;
        }
        return subscriberInfo;
    }

    protected EventNameInfo getEventName(Class<?> eventType) {
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

    protected boolean executeCheck(final IDomainEvent t, IExecuteCondition iExecuteCondition) {
        try {

            return Optional.ofNullable(iExecuteCondition).orElse(defaultCondition).isExecute(t);

        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<OrderedPerformManager.OrderData> findEventSubscriberInfo(String eventName) {
        return this.performManager.selectEventSubscriberInfo(eventName);
    }

    @Override
    public Map<String, List<String>> allEvents() {

        return this.subscribers.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        v -> v.getValue().values().stream().map(SubscriberInfo::getAlias)
                                .collect(toList()))
                );
    }

    @Override
    public void registerSubscriber(String alias, ISubscriber subscriber) {
        this.registerSubscriber(alias, subscriber, "");
    }

    @Override
    public void registerSubscriber(String alias, ISubscriber subscriber, String dependSubscriber) {
        this.registerSubscriber(alias, subscriber, defaultCondition, dependSubscriber);

    }

    @Override
    public void registerSubscriber(String alias, ISubscriber subscriber, IExecuteCondition condition) {
        this.registerSubscriber(alias, subscriber, condition, "");
    }

    @Override
    public void registerSubscriber(String alias, ISubscriber subscriber,
                                   IExecuteCondition condition,
                                   String dependSubscriber) {

        this.registerDelaySubscriber(alias, subscriber, condition, SubscriberDelayLevel.None, dependSubscriber);

    }

    @Override
    public void registerDelaySubscriber(String alias, ISubscriber subscriber,
                                        IExecuteCondition condition,
                                        SubscriberDelayLevel delayLevel,
                                        String dependSubscriber
    ) {


        EventNameInfo event = getEventName(subscriber.subscribedToEventType());
        if (this.subscribers.containsKey(event.eventName)) {

            Map<String, SubscriberInfo> stringISubscriberMap = this.subscribers.get(event.eventName);
            if (stringISubscriberMap.containsKey(alias)) {
                throw new IllegalArgumentException(alias + " is duplication");
            }

            this.subscribers.get(event.eventName).put(alias,
                    new SubscriberInfo(subscriber, alias, condition, delayLevel));

        } else {

            Map<String, SubscriberInfo> subscriberMap = new HashMap<>();
            subscriberMap.put(alias, new SubscriberInfo(subscriber, alias, condition, delayLevel));
            this.subscribers.put(event.eventName, subscriberMap);

        }
        if (this.performManager != null) {
            this.performManager.registerSubscriber(event.eventName,
                    alias,
                    dependSubscriber);
        }

    }
}
