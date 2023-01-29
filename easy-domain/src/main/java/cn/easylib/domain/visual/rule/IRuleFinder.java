package cn.easylib.domain.visual.rule;

import cn.easylib.domain.base.BrokenRuleMessage;
import cn.easylib.domain.base.EntityBase;

import java.util.List;

public interface IRuleFinder {

    <T extends EntityBase<?>> RuleFinderObject findEntityRuleList(Class<T> cls);


    public static class RuleFinderObject{
        public List<Class<?>> getEntityRuleCls() {
            return entityRuleCls;
        }

        public BrokenRuleMessage getBrokenRuleMessage() {
            return brokenRuleMessage;
        }

        private final List<Class<?>> entityRuleCls;
        private final BrokenRuleMessage brokenRuleMessage;

        public RuleFinderObject(List<Class<?>> entityRuleCls, BrokenRuleMessage brokenRuleMessage) {
            this.entityRuleCls = entityRuleCls;
            this.brokenRuleMessage = brokenRuleMessage;
        }
    }
}
