package cn.easylib.domain.rules;

import cn.easylib.domain.base.BrokenRuleObject;

/**
 * @author lixiaojing
 */
public class DefaultActiveRuleCondition<T extends BrokenRuleObject> implements IActiveRuleCondition<T> {
    @Override
    public boolean isActive(T model) {
        return true;
    }
}
