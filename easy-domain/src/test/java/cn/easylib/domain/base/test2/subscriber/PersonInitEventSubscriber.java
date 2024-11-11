package cn.easylib.domain.base.test2.subscriber;

import cn.easylib.domain.application.subscriber.IDomainEventManager;
import cn.easylib.domain.base.test2.event.PersonInitEvent;
import cn.easylib.domain.base.test2.subscriber.handler.IGradeHandler;
import cn.easylib.domain.base.test2.subscriber.handler.IScoreHandler;
import cn.easylib.domain.event.BaseEventSubscriber;
import cn.easylib.domain.event.SubscriberFactory;

public class PersonInitEventSubscriber extends BaseEventSubscriber<PersonInitEvent> {
    protected PersonInitEventSubscriber(IDomainEventManager evtManager,
                                        IScoreHandler iScoreHandler,
                                        IGradeHandler gradeHandler) {
        super(evtManager, PersonInitEvent.class);

        evtManager.registerSubscriber(PersonSubscriberKey.updateScore,
                SubscriberFactory.build(PersonInitEvent.class,
                        iScoreHandler::eventHandler)
        );
        evtManager.registerSubscriber(PersonSubscriberKey.updateGrade,
                SubscriberFactory.build(PersonInitEvent.class,
                        gradeHandler::eventHandler)
        );

    }
}
