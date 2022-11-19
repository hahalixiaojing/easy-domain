package cn.easylib.domain.visual.event;

import cn.easylib.domain.base.EntityBase;

import java.util.ArrayList;
import java.util.List;

public class EventParser {

    public <T extends EntityBase<?>> List<EventDescriptor> parse(Class<?> cls, String packageName) {
        return  new ArrayList<>();
    }
}
