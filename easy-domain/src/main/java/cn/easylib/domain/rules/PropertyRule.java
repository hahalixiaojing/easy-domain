package cn.easylib.domain.rules;

import java.lang.reflect.Method;

public abstract class PropertyRule<T, V> implements IRule<T> {

    private final String property;
    private String[] properties;

    protected PropertyRule(String property) {
        this.property = property;
        this.parseProperty();

    }

    protected String getProperty() {
        return this.property;
    }

    private void parseProperty() {
        this.properties = this.property.split("\\.");
    }

    @SuppressWarnings("unchecked")
    protected V getObjectAttrValue(T model) {
        Object value = this.getObjectValue(model);

        if (value == null) {
            return null;
        }
        return (V) value;
    }

    private Object getObjectValue(T model) {
        Object objectValue = model;
        for (String p : this.properties) {

            String pro = p.replaceFirst(p.substring(0, 1), p.substring(0, 1)
                    .toUpperCase());

            if (objectValue == null) {
                return null;
            }
            try {

                Method m = getMethod(objectValue.getClass(), "get" + pro);
                m.setAccessible(true);
                objectValue = m.invoke(objectValue);
            } catch (Exception e) {
                return null;
            }
        }
        return objectValue;
    }

    private Method getMethod(Class<?> c, String method) {
        Method m = null;
        try {
            m = c.getDeclaredMethod(method);
        } catch (NoSuchMethodException e) {

            try {
                m = c.getMethod(method);
            } catch (NoSuchMethodException ex) {
                if (c.getSuperclass() == null) {
                    return m;
                } else {
                    m = getMethod(c.getSuperclass(), method);
                }
            }
        }
        return m;
    }
}
