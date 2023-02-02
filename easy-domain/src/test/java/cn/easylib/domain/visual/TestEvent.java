package cn.easylib.domain.visual;

import cn.easylib.domain.event.BaseDomainEvent;
import cn.easylib.domain.event.EventName;

@EventName(value = "TestEvent", description = "测试验证")
public class TestEvent extends BaseDomainEvent {

}
