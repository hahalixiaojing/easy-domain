package cn.easylib.domain.visual.event;

import cn.easylib.domain.application.subscriber.IDomainEventManager;
import cn.easylib.domain.application.subscriber.ISubscriberFactory;
import cn.easylib.domain.application.subscriber.ISubscriberKey;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.event.*;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventParserTest {


    @Test
    public void eventParserTest() {

        EventParser eventParser = new EventParser(this.mockEventFinder(), this.mockIDomainEventManager());

        List<EventDescriptor> parse = eventParser.parse(null, null);

        String s = JSON.toJSONString(parse);

        System.out.println(s);

    }

    private IEventFinder mockEventFinder() {

        return new IEventFinder() {

            @Override
            public <T extends EntityBase<?>> List<Class<?>> findersList(Class<T> cls, String packageName) {
                return Stream.of(TestEvent.class).collect(Collectors.toList());
            }
        };
    }

    private IDomainEventManager mockIDomainEventManager() {
        ISubscriberFactory factory = new ThreadPoolSubscriberFactory();

        ThreadPoolTaskDomainEventManager manager = new ThreadPoolTaskDomainEventManager();
        manager.registerDomainEvent(TestEvent.class);

        manager.registerSubscriber(factory.build(TestEvent.class, s -> {
        }), TestEventSubscriberKey.SUB1, TestEventSubscriberKey.SUB2);

        manager.registerSubscriber(factory.build(TestEvent.class, s -> {
        }), TestEventSubscriberKey.SUB2, TestEventSubscriberKey.SUB3);

        manager.registerSubscriber(factory.build(TestEvent.class, s -> {
        }), TestEventSubscriberKey.SUB3);

        return manager;

    }
}

enum TestEventSubscriberKey implements ISubscriberKey {
    SUB1("sub1", "sub1描述"),
    SUB2("sub2", "sub2描述"),
    SUB3("sub3", "sub3描述");

    TestEventSubscriberKey(String keyName, String description) {
        this.keyName = keyName;
        this.description = description;
    }

    private final String keyName;
    private final String description;

    @Override
    public String keyName() {
        return this.keyName;
    }

    @Override
    public String description() {
        return this.description;
    }
}

@EventName(value = "TestEvent", description = "测试验证")
class TestEvent extends BaseDomainEvent {

}