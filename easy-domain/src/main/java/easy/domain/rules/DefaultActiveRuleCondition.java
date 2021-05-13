package easy.domain.rules;

import easy.domain.base.BrokenRuleObject;

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
