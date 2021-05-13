package easy.domain.afull.domain.order.model;

import easy.domain.rules.EntityRule;

import java.math.BigDecimal;

/**
 * @author lixiaojing
 * @date 2021/3/1 5:30 下午
 */
class OrderEntityRule extends EntityRule<Order> {
    public OrderEntityRule() {
        this.isBlank("pin", OrderBrokenRuleMessages.PIN_IS_EMPTY, "");
        this.addRule("totalPrice", model -> model.getTotalPrice().compareTo(BigDecimal.ZERO) > 0, OrderBrokenRuleMessages.TOTAL_PRICE_ERROR, "");
        this.addRule(model -> model.getOrderItemList().size() > 0 && model.getOrderItemList().size() < 100, OrderBrokenRuleMessages.ORDER_ITEM_ERROR, "", model -> model.getStatus() == 1);
    }
}
