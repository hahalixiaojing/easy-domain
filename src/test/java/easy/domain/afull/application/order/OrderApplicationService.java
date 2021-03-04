package easy.domain.afull.application.order;

import easy.domain.afull.api.order.OrderDto;
import easy.domain.afull.domain.order.event.OrderCreatedEvent;
import easy.domain.afull.domain.order.event.OrderPayedEvent;
import easy.domain.afull.domain.order.model.IOrderRepository;
import easy.domain.afull.domain.order.model.Order;
import easy.domain.afull.domain.order.model.OrderItem;
import easy.domain.afull.domain.order.service.OrderIdGenerateService;
import easy.domain.afull.domain.order.service.OrderTotalPriceCalculateService;
import easy.domain.application.BaseApplication;
import easy.domain.application.subscriber.IDomainEventSubscriber;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单应用服务层类，用于对订单领域对象的各种业务操作
 * 凡是要对订单领域对象进行操作，多需要通过该类，没有其他入口
 */
public class OrderApplicationService extends BaseApplication {

    private final IOrderRepository orderRepository;
    private final OrderIdGenerateService orderIdGenerateService = new OrderIdGenerateService();
    private final OrderTotalPriceCalculateService orderTotalPriceCalculateService = new OrderTotalPriceCalculateService();

    public OrderApplicationService(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        //初始化领域事件订阅
        this.initSubscriber();
    }

    public long createOrder(OrderDto orderDto) {
        List<OrderItem> orderItemList = orderDto.orderItemDtoList.stream()
                .map(s -> new OrderItem(s.skuId, s.number, s.price))
                .collect(Collectors.toList());
        //计算订单总价
        BigDecimal totalPrice = this.orderTotalPriceCalculateService.totalPrice(orderItemList, orderDto.pin);
        //生成订单编号
        long newOrderId = this.orderIdGenerateService.genOrderId();
        //构建订单领域对象
        Order order = new Order(newOrderId, totalPrice, orderDto.comment, orderDto.pin, orderItemList);
        //订单业务规则验证
        Boolean validate = order.validate();
        if (validate) {
            //验证通过，持久化到数据库
            this.orderRepository.create(order);
            //发布订单已创建事件
            this.publishEvent(order.createdEvent());
        } else {
            order.throwBrokenRuleException();
        }

        return newOrderId;
    }


    public void payment(long orderId) {

        Order order = this.orderRepository.findByOrderId(orderId);
        if (order != null) {
            OrderPayedEvent orderPayedEvent = order.payment();
            if (order.validate()) {
                this.orderRepository.update(order);
                this.publishEvent(orderPayedEvent);
            } else {
                throw order.exceptionCause();
            }
        }
    }

    private void initSubscriber() {
        //注册订单已支付事件
        this.registerDomainEvent(OrderPayedEvent.class);
        //注册订单已创建事件
        this.registerDomainEvent(OrderCreatedEvent.class);

        //订单已创建事件订阅
        this.registerSubscriber(new IDomainEventSubscriber<OrderCreatedEvent>() {
            @Override
            public Class<OrderCreatedEvent> subscribedToEventType() {
                return OrderCreatedEvent.class;
            }

            @Override
            public void handleEvent(OrderCreatedEvent aDomainEvent) {

                System.out.println("sendSMS");
                //调用发送通知用户成功的消息
            }
        }, "sendSMS");
        //订单已创建事件订阅
        this.registerSubscriber(new IDomainEventSubscriber<OrderCreatedEvent>() {
            @Override
            public Class<OrderCreatedEvent> subscribedToEventType() {
                return OrderCreatedEvent.class;
            }

            @Override
            public void handleEvent(OrderCreatedEvent aDomainEvent) {

                System.out.println("noticeWarehouse");
                //通知库房准备生产
            }
        }, "noticeWarehouse");

        //////////////////////////

        //订单已支付事件订阅
        this.registerSubscriber(new IDomainEventSubscriber<OrderPayedEvent>() {
            @Override
            public Class<OrderPayedEvent> subscribedToEventType() {
                return OrderPayedEvent.class;
            }

            @Override
            public void handleEvent(OrderPayedEvent aDomainEvent) {
                //调用发送通知用户支付成功的消息
            }
        }, "sendSMS");
        //订单已支付事件订阅
        this.registerSubscriber(new IDomainEventSubscriber<OrderPayedEvent>() {
            @Override
            public Class<OrderPayedEvent> subscribedToEventType() {
                return OrderPayedEvent.class;
            }

            @Override
            public void handleEvent(OrderPayedEvent aDomainEvent) {
                //通知库房准备生产
            }
        }, "noticeWarehouse");
    }
}
