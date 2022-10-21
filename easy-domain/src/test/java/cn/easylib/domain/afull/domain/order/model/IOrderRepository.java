package cn.easylib.domain.afull.domain.order.model;

/**
 * @author lixiaojing
 * @date 2021/3/1 5:30 下午
 */
public interface IOrderRepository {

    Order findByOrderId(long orderId);

    void update(Order order);

    void create(Order order);
}
