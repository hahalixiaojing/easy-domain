package cn.easylib.domain.rules;

public abstract class AbstractParamRuleBuilder<T> {

    public abstract IParamRule<T> rule();

    public IActiveRuleCondition<T> ruleCondition() {
        return null;
    }
}
