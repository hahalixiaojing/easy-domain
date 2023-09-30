package cn.easylib.domain.visual.event;

import java.util.List;

public interface IEventSubscriberVisualOutput {

    String output(List<EventDescriptor> eventDescriptorList);
}
