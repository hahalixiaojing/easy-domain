package cn.easylib.domain.visual.event;

import cn.easylib.domain.application.subscriber.IDomainEventManager;
import cn.easylib.domain.application.subscriber.ISubscriberKey;
import cn.easylib.domain.application.subscriber.OrderedPerformManager;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.event.EventName;

import java.util.*;
import java.util.stream.Collectors;

public class EventParser {

    private final IDomainEventManager iDomainEventManager;

    private final Map<Class<?>, IEventFinder> eventFinderMap = new HashMap<>();

    public EventParser(IDomainEventManager iDomainEventManager) {
        this.iDomainEventManager = iDomainEventManager;
    }

    public <T extends EntityBase<?>> void registerDomainEvent(Class<T> entityClass,
                                                              IEventFinder finder) {
        eventFinderMap.put(entityClass, finder);
    }

    public <T extends EntityBase<?>> List<EventDescriptor> parse(Class<T> cls) {


        List<Class<?>> baseDomainEvents = Optional.ofNullable(this.eventFinderMap.get(cls))
                .map(t -> t.findersList(cls)).orElse(null);

        return Optional.ofNullable(baseDomainEvents)
                .orElse(Collections.emptyList())
                .stream().map(evt -> {

                    EventName eventName = evt.getAnnotation(EventName.class);

                    List<OrderedPerformManager.OrderData> eventSubscriberInfoList =
                            iDomainEventManager.findEventSubscriberInfo(evt.getName());


                    List<EventSubscriberDescriptor> subscriberDescriptorList = eventSubscriberInfoList.stream().map(s -> {

                        OrderedPerformManager.OrderData parent = this.findParent(eventSubscriberInfoList,
                                s.getChildSubscriberKey().keyName());

                        String dependOn = Optional.ofNullable(parent)
                                .map(OrderedPerformManager.OrderData::getCurrentSubscriberKey)
                                .map(ISubscriberKey::keyName).orElse("");

                        return new EventSubscriberDescriptor(s.getChildSubscriberKey().keyName(),
                                s.getChildSubscriberKey().description(),
                                dependOn);


                    }).collect(Collectors.toList());

                    return new EventDescriptor(eventName.value(), eventName.description(),
                            subscriberDescriptorList);


                }).collect(Collectors.toList());

    }

    /**
     * 找父节点
     */
    private OrderedPerformManager.OrderData findParent(List<OrderedPerformManager.OrderData> orderDataList,
                                                       String childKey) {

        return orderDataList.stream().filter(s -> s.getChildSubscriberKey().keyName().equals(childKey))
                .findFirst()
                .orElse(null);

    }
}
