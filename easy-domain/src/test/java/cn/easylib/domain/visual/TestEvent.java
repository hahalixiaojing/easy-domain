package cn.easylib.domain.visual;

import cn.easylib.domain.event.BaseDomainEvent;
import cn.easylib.domain.event.EventName;
import cn.easylib.domain.visual.event.DomainEventVisual;

@EventName(value = "TestEvent")
@DomainEventVisual(description = "测试事件描述")
public class TestEvent extends BaseDomainEvent {

}
