package easy.domain.rules;

public class NumberShouldLessThanRule<T, V extends Number> extends
        PropertyRule<T, V> {

    private final V value;

    public NumberShouldLessThanRule(String property, V value) {
        super(property);
        this.value = value;

    }


    @Override
    public boolean isSatisfy(T model) {
        V v = this.getObjectAttrValue(model);

        if (this.value == null) {
            return false;
        }
        String name = this.value.getClass().getName();

        if (Integer.class.getName().equals(name)) {
            return v.intValue() < value.intValue();
        } else if (Long.class.getName().equals(name)) {
            return v.doubleValue() < value.doubleValue();
        } else if (Short.class.getName().equals(name)) {
            return v.shortValue() < value.shortValue();
        } else if (Float.class.getName().equals(name)) {
            return v.floatValue() < value.floatValue();
        } else if (Double.class.getName().equals(name)) {
            return v.doubleValue() < value.doubleValue();
        } else if (Byte.class.getName().equals(name)) {
            return v.byteValue() < value.byteValue();
        }
        return false;
    }
}
