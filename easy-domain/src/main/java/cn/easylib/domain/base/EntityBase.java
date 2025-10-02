package cn.easylib.domain.base;


import cn.easylib.domain.event.BaseDomainEvent;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class EntityBase<T> extends BrokenRuleObject implements
        IEntity<T>, IEntityAction {

    private T id;

    private boolean entityDelete;
    protected boolean isNewEntity = false;

    @Override
    protected abstract BrokenRuleMessage getBrokenRuleMessages();

    @Override
    public Boolean validate() {
        return false;
    }

    protected <V> V setAndReturnOld(Consumer<V> set, Supplier<V> getOld, V newValue) {
        V old = getOld.get();
        set.accept(newValue);

        return old;
    }

    protected <V> CompareAndSetInfo<V> compareAndSet(V newValue, V oldValue, Consumer<V> set) {
        boolean equals = Objects.equals(newValue, oldValue);
        if (!equals) {
            set.accept(newValue);
        }
        return new CompareAndSetInfo<>(equals, newValue, oldValue);
    }

    @Override
    protected String takeEntityInfo() {
        return Optional.ofNullable(this.getId()).map(Objects::toString).orElse("");
    }

    protected final DomainEventCollector eventCollector = new DomainEventCollector();
    protected final EntityActionCollector actionCollector = new EntityActionCollector(this.entityActions());
    protected final EntityCopyDataCollector copyDataCollector = new EntityCopyDataCollector();

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

    public boolean isNewEntity() {
        return isNewEntity;
    }

    protected void setNewEntity(boolean newEntity) {
        isNewEntity = newEntity;
    }
}
