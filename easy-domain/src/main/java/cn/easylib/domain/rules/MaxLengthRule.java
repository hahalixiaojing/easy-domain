package cn.easylib.domain.rules;

import org.apache.commons.lang3.StringUtils;

public class MaxLengthRule<T> extends PropertyRule<T, String> {

    private final int maxLength;

    public MaxLengthRule(String property, int maxLength) {
        super(property);
        this.maxLength = maxLength;
    }

    @Override
    public boolean isSatisfy(T model) {
        String value = this.getObjectAttrValue(model);

        if (StringUtils.isEmpty(value)) {
            return true;
        }
        return value.length() <= this.maxLength;
    }
}
