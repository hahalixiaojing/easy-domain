package easy.domain.rules;

/**
 * @author lixiaojing10
 * @date 2021/1/31 2:38 下午
 */
public interface IActiveRuleCondition<T> {
    boolean isActive(T model);
}
