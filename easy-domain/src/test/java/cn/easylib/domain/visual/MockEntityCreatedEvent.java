package cn.easylib.domain.visual;

import cn.easylib.domain.event.BaseDomainEvent;
import cn.easylib.domain.event.EventName;
import cn.easylib.domain.visual.event.DomainEventVisual;


@EventName(value = "MockEntityCreatedEvent")
@DomainEventVisual(description = "创建事件描述")
public class MockEntityCreatedEvent extends BaseDomainEvent {


    public static MockEntityCreatedEvent buildEvent(){
        return new MockEntityCreatedEvent();
    }
}


