package easy.domain.event;

/**
 * 领域事件对象
 * @author lixiaojing10
 * @date 2021/2/2 11:27 上午
 */
public class TestDomainEvent implements IDomainEvent {

    private String businessId;

    public TestDomainEvent(String businessId) {
        this.businessId = businessId;
    }

    @Override
    public String getBusinessId() {
        return this.businessId;
    }
}
