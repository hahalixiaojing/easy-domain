package cn.easylib.domain.base.test1;

import cn.easylib.domain.base.DomainEventCollector;
import cn.easylib.domain.event.BaseDomainEvent;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author lixiaojing10
 * @date 2022/3/12 4:29 下午
 */
public class DomainEventCollectorTest {

    @Test
    public void eventCollectorTest() {

        DomainEventCollector domainEventCollector = new DomainEventCollector();

        domainEventCollector.pushEvent(new Demo1Event());
        domainEventCollector.pushEvent(new Demo2Event());

        final long id = System.currentTimeMillis();

        domainEventCollector.pushDelayGenerateEvent(() -> {

            Demo3Event demo3Event = new Demo3Event();
            demo3Event.id = id;

            return demo3Event;
        });

        Assert.assertEquals(3, domainEventCollector.getEventList().size());

        domainEventCollector.removeEvent(Demo1Event.class);

        Assert.assertEquals(2, domainEventCollector.getEventList().size());

    }


}

class Demo1Event extends BaseDomainEvent {
    public int id;
}

class Demo2Event extends BaseDomainEvent {
    public int id;
}

class Demo3Event extends BaseDomainEvent {
    public long id;
}