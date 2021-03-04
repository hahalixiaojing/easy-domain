package easy.domain.afull;


import easy.domain.afull.api.order.IOrderService;
import easy.domain.afull.api.order.OrderDto;
import easy.domain.afull.api.order.OrderItemDto;
import easy.domain.afull.application.order.OrderApplicationService;
import easy.domain.afull.domain.order.model.IOrderRepository;
import easy.domain.afull.domain.order.model.Order;
import easy.domain.afull.infrastructure.repository.memory.order.OrderRepository;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务接口测试
 *
 * @author lixiaojing10
 * @date 2021/2/4 4:50 下午
 */
public class MainEntryTestClass implements IOrderService {

    private final OrderApplicationService orderApplicationService;
    private final IOrderRepository repository;

    public MainEntryTestClass() {
        this.repository = new OrderRepository();
        this.orderApplicationService = new OrderApplicationService(this.repository);
    }

    @Test
    public void createOrder() {

        List<OrderItemDto> orderItems = new ArrayList<>();

        OrderItemDto orderItem = new OrderItemDto();
        orderItem.number = 1;
        orderItem.price = BigDecimal.valueOf(20.0);
        orderItem.number = 2;
        orderItems.add(orderItem);

        OrderDto orderDto = new OrderDto();
        orderDto.pin = "zs";
        orderDto.comment = "dd";
        orderDto.orderItemDtoList = orderItems;

        long orderId = this.createOrder(orderDto);

        Order order = this.repository.findByOrderId(orderId);

        Assert.assertEquals(orderId, order.getId().longValue());
    }

    @Override
    public long createOrder(OrderDto dto) {
        return this.orderApplicationService.createOrder(dto);
    }
}
