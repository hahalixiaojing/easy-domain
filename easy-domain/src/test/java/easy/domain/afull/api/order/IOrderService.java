package easy.domain.afull.api.order;

/**
 * 对外接口，可以dubbo形式的，也可以是rest api形式的
 *
 * @author lixiaojing10
 * @date 2021/3/4 2:19 下午
 */
public interface IOrderService {

    long createOrder(OrderDto dto);
}
