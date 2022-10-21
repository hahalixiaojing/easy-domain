package cn.easylib.domain.rules;

public class BooleanRule<T> extends PropertyRule<T, Boolean> {

    private final boolean value;

    public BooleanRule(String property, boolean value) {
        super(property);
        this.value = value;

    }

    @Override
    public boolean isSatisfy(T model) {
        boolean v = this.getObjectAttrValue(model);

        return v == this.value;
    }
}
