package cn.easylib.domain.visual.event;

import cn.easylib.domain.application.subscriber.IDomainEventManager;
import cn.easylib.domain.application.subscriber.ISubscriberKey;
import cn.easylib.domain.application.subscriber.OrderedPerformManager;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.event.EventName;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventParser {


    private final IEventFinder eventFinder;
    private final IDomainEventManager iDomainEventManager;

    public EventParser(IEventFinder eventFinder, IDomainEventManager iDomainEventManager) {
        this.eventFinder = eventFinder;
        this.iDomainEventManager = iDomainEventManager;
    }

    public <T extends EntityBase<?>> List<EventDescriptor> parse(Class<T> cls, String packageName) {

        List<Class<?>> baseDomainEvents = this.eventFinder.findersList(cls, packageName);

        return baseDomainEvents.stream().map(evt -> {

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
