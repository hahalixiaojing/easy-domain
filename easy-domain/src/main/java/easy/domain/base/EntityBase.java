package easy.domain.base;


import easy.domain.event.BaseDomainEvent;

import java.util.List;

public abstract class EntityBase<T> extends BrokenRuleObject implements
        IEntity<T> {

    private T id;

    @Override
    protected abstract BrokenRuleMessage getBrokenRuleMessages();

    protected final DomainEventCollector eventCollector = new DomainEventCollector();

    @Override
    public T getId() {
        return this.id;
    }

    protected void setId(T id) {
        this.id = id;
    }

    public List<BaseDomainEvent> getEvents() {
        return this.eventCollector.getEventList();
    }
}
