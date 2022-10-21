package cn.easylib.domain.base;


import cn.easylib.domain.event.BaseDomainEvent;

import java.util.*;

public abstract class EntityBase<T> extends BrokenRuleObject implements
        IEntity<T>, IEntityAction {

    private T id;

    private boolean entityDelete;


    @Override
    protected abstract BrokenRuleMessage getBrokenRuleMessages();


    @Override
    protected String takeEntityInfo() {
        return Optional.ofNullable(this.getId()).map(Objects::toString).orElse("");
    }

    protected final DomainEventCollector eventCollector = new DomainEventCollector();
    protected final EntityActionCollector actionCollector = new EntityActionCollector(this.entityActions());

    @Override
    public T getId() {
        return this.id;
    }

    protected void setId(T id) {
        this.id = id;
    }

    public EntityActionCollector allActions() {
        return this.actionCollector;
    }

    public List<BaseDomainEvent> allEvents() {
        return this.eventCollector.getEventList();
    }

    public boolean getEntityDelete() {
        return this.entityDelete;
    }

    protected void setEntityDelete(boolean entityDelete) {
        this.entityDelete = entityDelete;
    }
}
