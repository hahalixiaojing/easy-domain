package easy.domain.base;


public abstract class EntityBase<T> extends BrokenRuleObject implements
        IEntity<T> {

    private T id;

    @Override
    protected abstract BrokenRuleMessage getBrokenRuleMessages();

    @Override
    public T getId() {
        return this.id;
    }

    protected void setId(T id) {
        this.id = id;
    }
}
