package cn.easylib.domain.rules;

import cn.easylib.domain.base.BrokenRuleObject;

/**
 * @author lixiaojing
 * @date 2021/1/31 2:43 下午
 */
public class DefaultActiveRuleCondition<T extends BrokenRuleObject> implements IActiveRuleCondition<T> {
    @Override
    public boolean isActive(T model) {
        return true;
    }
}
