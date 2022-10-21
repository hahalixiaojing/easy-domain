package cn.easylib.domain.event;

public abstract class BaseDomainEvent implements IDomainEvent {

    private String businessId;

    public String getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
