package easy.domain.afull.api;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiaojing10
 * @date 2021/3/1 5:51 下午
 */
public class OrderDto {
    public String pin;
    public String comment;
    public List<OrderItemDto> orderItemDtoList = new ArrayList<>();
}
