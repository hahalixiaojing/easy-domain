package cn.easylib.domain.afull.domain.order.event;

import cn.easylib.domain.event.BaseDomainEvent;
import cn.easylib.domain.event.EventName;

/**
 * @author lixiaojing
 * @date 2021/3/1 5:27 下午
 */
@EventName(value = "OrderPayedEvent", shareTopicName = "")
public class OrderPayedEvent extends BaseDomainEvent {

    //还可以有其他的字段

    public OrderPayedEvent(long orderId) {

        this.setBusinessId(String.valueOf(orderId));
    }
}
