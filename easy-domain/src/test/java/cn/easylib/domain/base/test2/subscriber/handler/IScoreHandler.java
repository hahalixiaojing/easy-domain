package cn.easylib.domain.base.test2.subscriber.handler;

import cn.easylib.domain.base.test2.event.PersonInitEvent;
import cn.easylib.domain.base.test2.event.PersonUpdateEvent;
import cn.easylib.domain.base.test2.event.PersonUpdateStatusEvent;

public interface IScoreHandler {

    void eventHandler(PersonInitEvent event);
    void eventHandler(PersonUpdateEvent event);
    void eventHandler(PersonUpdateStatusEvent event);
}
