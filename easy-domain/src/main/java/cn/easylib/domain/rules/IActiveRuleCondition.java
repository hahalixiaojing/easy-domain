package cn.easylib.domain.rules;

/**
 * @author lixiaojing
 */
public interface IActiveRuleCondition<T> {
    boolean isActive(T model);
}
