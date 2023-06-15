package cn.easylib.domain.event;

/**
 * 领域事件对象
 * @author lixiaojing
 * @date 2021/2/2 11:27 上午
 */
public class TestDomainEvent implements IDomainEvent {

    private String businessId;

    private String name;

    public TestDomainEvent(String businessId) {
        this.businessId = businessId;
        this.name = businessId;
    }

    @Override
    public String getBusinessId() {
        return this.businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
