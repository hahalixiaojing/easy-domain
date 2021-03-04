package easy.domain.rules;

public class NumberShouldGreaterThanRule<T, V extends Number> extends
		PropertyRule<T, V> {
	private V value = null;

	public NumberShouldGreaterThanRule(String property, V value) {
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

		if (Integer.class.getName() == name) {
			return v.intValue() > value.intValue();
		}
		else if (Long.class.getName() == name) {
			return v.doubleValue() > value.doubleValue();
		}
		else if (Short.class.getName() == name) {
			return v.shortValue() > value.shortValue();
		}
		else if (Float.class.getName() == name) {
			return v.floatValue() > value.floatValue();
		}
		else if (Double.class.getName() == name) {
			return v.doubleValue() > value.doubleValue();
		}
		else if (Byte.class.getName() == name) {
			return v.byteValue() > value.byteValue();
		}
		return false;
	}


}