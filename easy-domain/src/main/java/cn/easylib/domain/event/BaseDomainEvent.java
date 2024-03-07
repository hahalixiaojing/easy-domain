package cn.easylib.domain.event;

public abstract class BaseDomainEvent implements IDomainEvent {

    private String businessId;
    private String actionName;

    public String getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
