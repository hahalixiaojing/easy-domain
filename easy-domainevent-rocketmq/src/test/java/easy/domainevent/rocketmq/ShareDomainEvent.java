package easy.domainevent.rocketmq;

import easy.domain.event.BaseDomainEvent;
import easy.domain.event.EventName;

/**
 * @author lixiaojing
 * @date 2021/3/18 3:02 下午
 */
@EventName(value = "ShareDomainEvent",shareTopicName = "SharedTopic")
public class ShareDomainEvent extends BaseDomainEvent {


    public ShareDomainEvent() {
    }

    public ShareDomainEvent(String id, String name) {
        this.setBusinessId(id);
        this.name = name;
    }

    public String name;
}
