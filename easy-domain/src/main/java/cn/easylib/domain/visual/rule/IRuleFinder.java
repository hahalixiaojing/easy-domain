package cn.easylib.domain.visual.rule;

import cn.easylib.domain.base.BrokenRuleMessage;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.rules.EntityRule;

import java.util.List;

public interface IRuleFinder {

    <T extends EntityBase<?>> RuleFinderObject findEntityRuleList(Class<T> cls);


    class RuleFinderObject{
        private final List<EntityRule<?>> entityRuleCls;
        private final BrokenRuleMessage brokenRuleMessage;

        public List<EntityRule<?>> getEntityRuleCls() {

            return entityRuleCls;
        }

        public BrokenRuleMessage getBrokenRuleMessage() {
            return brokenRuleMessage;
        }



        public RuleFinderObject(List<EntityRule<?>> entityRuleCls, BrokenRuleMessage brokenRuleMessage) {
            this.entityRuleCls = entityRuleCls;
            this.brokenRuleMessage = brokenRuleMessage;
        }
    }
}
