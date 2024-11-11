package cn.easylib.domain.base.test2.subscriber.handler;

import cn.easylib.domain.base.test2.event.PersonInitEvent;
import cn.easylib.domain.base.test2.event.PersonUpdateEvent;

public interface IGradeHandler {

    void eventHandler(PersonInitEvent event);
    void eventHandler(PersonUpdateEvent event);
}
