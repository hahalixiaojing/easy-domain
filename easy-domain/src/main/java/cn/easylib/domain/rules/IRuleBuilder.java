package cn.easylib.domain.rules;

public interface IRuleBuilder<T> {

     IRule<T> rule();

    default IActiveRuleCondition<T> ruleCondition() {
        return null;
    }
}
