package cn.easylib.domain.rules;

public abstract class BaseRuleValidator<T> {

    protected abstract boolean validate(T model);


    public IRule<T> rule() {
        return this::validate;
    }

    public IActiveRuleCondition<T> ruleCondition() {
        return model -> true;
    }
}
