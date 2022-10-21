package cn.easylib.domain.base;

import cn.easylib.domain.rules.EntityRule;

/**
 * @author lixiaojing10
 */
public interface ICustomValidator<R extends BrokenRuleObject> {
    Boolean validate(EntityRule<R> rule);
}
