package cn.easylib.domain.rules;

import org.apache.commons.lang3.StringUtils;

public class IsBlankRule<T> extends PropertyRule<T, String> {

    public IsBlankRule(String property) {
        super(property);
    }
    @Override
    public boolean isSatisfy(T model) {

        String v = this.getObjectAttrValue(model);
        return !StringUtils.isBlank(v);
    }
}
