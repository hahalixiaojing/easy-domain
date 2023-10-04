package cn.easylib.domain.visual.event;

import cn.easylib.domain.application.subscriber.AbstractSubscriberKey;
import cn.easylib.domain.application.subscriber.IDomainEventManager;
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


        IEventFinder iEventFinder = this.eventFinderMap.get(cls);
        if (iEventFinder == null) {
            return Collections.emptyList();
        }


        List<Class<?>> baseDomainEvents = Optional.ofNullable(iEventFinder.findersList(cls))
                .orElse(Collections.emptyList());

        return baseDomainEvents
                .stream().map(evt -> {

                    DomainEventVisual domainEventDescriptor = evt.getAnnotation(DomainEventVisual.class);
                    EventName eventName = evt.getAnnotation(EventName.class);

                    List<OrderedPerformManager.OrderData> eventSubscriberInfoList =
                            iDomainEventManager.findEventSubscriberInfo(eventName.value());


                    List<EventSubscriberDescriptor> subscriberDescriptorList = eventSubscriberInfoList.stream()
                            .map(s -> {
                                OrderedPerformManager.OrderData parent = this.findParent(eventSubscriberInfoList,
                                        s.childSubscriberAlias);

                                String dependOn = Optional.ofNullable(parent)
                                        .map(t -> t.currentSubscriberAlias)
                                        .orElse("");

                                return new EventSubscriberDescriptor(s.childSubscriberAlias,
                                        Optional.ofNullable(iEventFinder.eventSubscribeKey().getKeyInfo(s.childSubscriberAlias))
                                                .map(AbstractSubscriberKey.KeySetting::getDescription).orElse(""),
                                        dependOn);


                            }).collect(Collectors.toList());


                    return new EventDescriptor(eventName.value(),
                            Optional.ofNullable(domainEventDescriptor)
                                    .map(DomainEventVisual::description)
                                    .orElse(eventName.value()),
                            subscriberDescriptorList);


                }).collect(Collectors.toList());

    }

    /**
     * 找父节点
     */
    private OrderedPerformManager.OrderData findParent(List<OrderedPerformManager.OrderData> orderDataList,
                                                       String childKey) {

        return orderDataList.stream().filter(s -> s.childSubscriberAlias.equals(childKey))
                .findFirst()
                .orElse(null);

    }
}
