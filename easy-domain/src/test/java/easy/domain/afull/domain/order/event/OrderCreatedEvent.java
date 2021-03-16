package easy.domain.afull.domain.order.event;

import easy.domain.event.BaseDomainEvent;
import easy.domain.event.EventName;

/**
 * @author lixiaojing10
 * @date 2021/3/1 6:17 下午
 */
@EventName(value = "OrderCreatedEvent", shareTopicName = "")
public class OrderCreatedEvent extends BaseDomainEvent {

    private final long orderId;

    public OrderCreatedEvent(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }
}
