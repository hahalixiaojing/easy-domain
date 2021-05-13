package easy.domainevent.rocketmq;

import easy.domain.event.BaseDomainEvent;
import easy.domain.event.EventName;

/**
 * @author lixiaojing
 * @date 2021/3/17 5:24 下午
 */
@EventName("MyDomainEvent")
public class MyDomainEvent extends BaseDomainEvent {

    public MyDomainEvent() {
    }

    public MyDomainEvent(String id) {
        this.setBusinessId(id);
        this.name = id;
    }

    public String name;
}
