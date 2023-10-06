package cn.easylib.domain.base;


import cn.easylib.domain.event.BaseDomainEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class EntityBase<T> extends BrokenRuleObject implements
        IEntity<T>, IEntityAction {

    private T id;

    private boolean entityDelete;


    @Override
    protected abstract BrokenRuleMessage getBrokenRuleMessages();

    protected <VALUE> VALUE setAndReturnOld(Consumer<VALUE> set, Supplier<VALUE> getOld, VALUE newValue) {
        VALUE old = getOld.get();
        set.accept(newValue);

        return old;
    }


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
