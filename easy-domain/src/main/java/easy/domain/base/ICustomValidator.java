package easy.domain.base;

import easy.domain.rules.EntityRule;

/**
 * @author lixiaojing10
 * @date 2021/12/22 3:42 下午
 */
public interface ICustomValidator<R extends BrokenRuleObject> {
    Boolean validate(EntityRule<R> rule);
}
