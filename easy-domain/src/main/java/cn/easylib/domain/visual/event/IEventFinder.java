package cn.easylib.domain.visual.event;

import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.event.BaseDomainEvent;

import java.util.List;

public interface IEventFinder<T extends EntityBase<?>> {

    List<BaseDomainEvent> findersList(Class<T> cls, String packageName);
}
