package cn.easylib.domain.visual.event;

import cn.easylib.domain.base.EntityBase;
import java.util.List;

public interface IEventFinder {

    <T extends EntityBase<?>> List<Class<?>> findersList(Class<T> cls);
}
