package cn.easylib.domain.afull.domain.order.model;

import cn.easylib.domain.afull.domain.order.event.OrderCreatedEvent;
import cn.easylib.domain.afull.domain.order.event.OrderPayedEvent;
import cn.easylib.domain.base.BrokenRuleMessage;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.base.ICustomValidator;
import cn.easylib.domain.rules.EntityRule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lixiaojing
 */
public class Order extends EntityBase<Long> implements ICustomValidator<Order> {

    private final BigDecimal totalPrice;
    private final String comment;
    private final String pin;
    private int status;
    private final List<OrderItem> orderItemList;
    private final LocalDateTime created;


    public Order(long orderId, BigDecimal totalPrice, String comment, String pin, List<OrderItem> orderItemList) {
        this.setId(orderId);
        this.totalPrice = totalPrice;
        this.comment = comment;
        this.pin = pin;
        this.orderItemList = orderItemList;
        this.created = LocalDateTime.now();
        //事件收集
        this.eventCollector.pushDelayGenerateEvent(()-> new OrderCreatedEvent(this.getId()));

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



    /**
     * 订单支持业务操作
     *
     * @return 返回订单已支持事件
     */
    public void payment() {
        this.status = 3;
        //事件收集
        this.eventCollector.pushEvent(new OrderPayedEvent(this.getId()));

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

    @Override
    public Boolean validate(EntityRule<Order> rule) {
        return null;
    }
}

