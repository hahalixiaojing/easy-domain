package cn.easylib.domain.rules;

public interface IParamRuleBuilder<T> {

    IParamRule<T> rule();

    default IActiveRuleCondition<T> ruleCondition() {
        return null;
    }
}
