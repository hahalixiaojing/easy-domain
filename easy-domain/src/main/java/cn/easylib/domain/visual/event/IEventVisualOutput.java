package cn.easylib.domain.visual.event;

import cn.easylib.domain.visual.entity.EntityDescriptor;

import java.util.List;

public interface IEventVisualOutput {
    String output(List<EventDescriptor> eventDescriptorList, EntityDescriptor entityDescriptor);
}
