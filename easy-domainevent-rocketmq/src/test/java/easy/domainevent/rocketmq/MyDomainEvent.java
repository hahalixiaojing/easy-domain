package easy.domainevent.rocketmq;

import cn.easylib.domain.event.BaseDomainEvent;
import cn.easylib.domain.event.EventName;

/**
 * @author lixiaojing
 * @date 2021/3/17 5:24 下午
 */
@EventName("MyDomainEvent")
public class MyDomainEvent extends BaseDomainEvent {

    public MyDomainEvent() {
    }

    public MyDomainEvent(String name) {
        this.setBusinessId(name);
        this.name = name;
    }

    public String name;
}
