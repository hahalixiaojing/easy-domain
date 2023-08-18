package cn.easylib.domain.visual.event;

import cn.easylib.domain.application.subscriber.AbstractSubscriberKey;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.visual.MockDomainEventManager;
import cn.easylib.domain.visual.MockEntity;
import cn.easylib.domain.visual.TestEvent;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventParserTest {


    @Test
    public void eventParserTest() {

        EventParser eventParser = new EventParser(MockDomainEventManager.mockIDomainEventManager(), new AbstractSubscriberKey() {
            @Override
            protected void populateKeys() {

            }
        });
        eventParser.registerDomainEvent(MockEntity.class, mockEventFinder());
        List<EventDescriptor> parse = eventParser.parse(MockEntity.class);

        String s = JSON.toJSONString(parse);

        System.out.println(s);

    }

    private IEventFinder mockEventFinder() {

        return new IEventFinder() {

            @Override
            public <T extends EntityBase<?>> List<Class<?>> findersList(Class<T> cls) {
                return Stream.of(TestEvent.class).collect(Collectors.toList());
            }
        };
    }


}

