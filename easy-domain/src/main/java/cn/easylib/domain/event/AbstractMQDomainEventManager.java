package cn.easylib.domain.event;

import cn.easylib.domain.application.subscriber.*;
import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractMQDomainEventManager extends AbstractDomainEventManager {
    protected AbstractMQDomainEventManager(String environmentName, IOrderedPerformManager performManager) {
        super(environmentName, performManager);
    }

    @SuppressWarnings("unchecked")
    protected <T extends IDomainEvent> List<SubscribeData> buildSubscribeDataList(T obj) {

        EventNameInfo eventNameInfo = this.getEventName(obj.getClass());
        Map<String, SubscriberInfo> subscriberMap = this.filterSubscriberInfoMap(eventNameInfo);
        return subscriberMap.entrySet().stream().map(entry -> {
            if (entry.getValue().getCondition().isExecute(obj)) {
                return this.createSubscribeData(obj,
                        eventNameInfo,
                        entry.getKey(),
                        false);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected <T extends IDomainEvent> SubscribeData buildSubscribeData(final T obj,
                                                                        String subscriber,
                                                                        Boolean onlyThis) {
        EventNameInfo eventName = this.getEventName(obj.getClass());
        SubscriberInfo subscriberInfo = this.findSubscriberInfo(obj, subscriber, eventName);
        if (subscriberInfo == null) {
            return null;
        }
        return this.createSubscribeData(obj, eventName, subscriber, onlyThis);
    }

    private <T extends IDomainEvent> SubscribeData createSubscribeData(final T obj,
                                                                       EventNameInfo eventNameInfo,
                                                                       String subscriber,
                                                                       Boolean onlyThis) {
        final String realEventName;
        if (eventNameInfo.shareTopicName != null && !eventNameInfo.shareTopicName.equals("")) {
            realEventName = eventNameInfo.eventName;
        } else {
            realEventName = "";
        }
        String jsonData = JSON.toJSONString(obj);
        return new SubscribeData(subscriber, jsonData, realEventName, onlyThis);
    }

    protected String getTopicName(Class<?> eventType) {
        final EventNameInfo eventNameInfo = this.getEventName(eventType);
        String topic;
        if (eventNameInfo.shareTopicName == null || eventNameInfo.shareTopicName.equals("")) {
            topic = eventNameInfo.eventName;
        } else {
            topic = eventNameInfo.shareTopicName;
        }
        return topic;
    }



    protected void handleEvent(String data, String mqTopic) {

        SubscribeData subscribeData = JSON.parseObject(data, SubscribeData.class);
        String event = mqTopic;
        if (subscribeData.getRealEventName() != null && !subscribeData.getRealEventName().equals("")) {
            event = subscribeData.getRealEventName();
        }
        Map<String, SubscriberInfo> subscriberList = this.subscribers.get(event);
        AbstractDomainEventSubscriber subscriber = (AbstractDomainEventSubscriber) subscriberList.get(subscribeData.getName()).getSubscriber();
        if (subscriber != null) {
            subscriber.handleEvent(subscribeData.getEventData());

            if (this.performManager != null && !subscribeData.getOnlyThis()) {

                List<String> nextSubscribers = this.performManager.selectNextSubscribers(event, subscribeData.getName());
                IDomainEvent iDomainEvent = subscriber.parseEvent(subscribeData.getEventData());
                nextSubscribers.forEach(s -> this.publishEvent(iDomainEvent, s, subscribeData.getOnlyThis()));

            }
        }
    }
}
