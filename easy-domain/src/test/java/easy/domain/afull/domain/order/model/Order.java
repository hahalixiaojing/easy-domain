package easy.domain.afull.domain.order.model;

import easy.domain.afull.domain.order.event.OrderCreatedEvent;
import easy.domain.afull.domain.order.event.OrderPayedEvent;
import easy.domain.base.BrokenRuleMessage;
import easy.domain.base.EntityBase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lixiaojing10
 * @date 2021/3/1 5:26 下午
 */
public class Order extends EntityBase<Long> {

    private final BigDecimal totalPrice;
    private final String comment;
    private final String pin;
    private int status;
    private final List<OrderItem> orderItemList;
    private final LocalDateTime created;


    public Order(long orderId,BigDecimal totalPrice, String comment, String pin, List<OrderItem> orderItemList) {
        this.setId(orderId);
        this.totalPrice = totalPrice;
        this.comment = comment;
        this.pin = pin;
        this.orderItemList = orderItemList;
        this.created = LocalDateTime.now();

    }


    @Override
    public Boolean validate() {
        return new OrderEntityRule().isSatisfy(this);
    }

    @Override
    protected BrokenRuleMessage getBrokenRuleMessages() {
        return new OrderBrokenRuleMessages();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public int getStatus() {
        return status;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public OrderCreatedEvent createdEvent() {
        return new OrderCreatedEvent(this.getId());
    }


    /**
     * 订单支持业务操作
     *
     * @return 返回订单已支持事件
     */
    public OrderPayedEvent payment() {
        this.status = 3;
        return new OrderPayedEvent(this.getId());
    }

    public String getComment() {
        return comment;
    }

    public String getPin() {
        return pin;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}

