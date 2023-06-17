package cn.easylib.domain.rules;

public abstract class AbstractRuleBuilder<T> {

    public abstract IRule<T> rule();

    public IActiveRuleCondition<T> ruleCondition() {
        return null;
    }
}
