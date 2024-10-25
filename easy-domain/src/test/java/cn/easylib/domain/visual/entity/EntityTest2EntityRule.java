package cn.easylib.domain.visual.entity;

import cn.easylib.domain.rules.EntityRule;

public class EntityTest2EntityRule extends EntityRule<EntityTest2> {

    public EntityTest2EntityRule() {

        this.addRule(s -> {


            EntityTest2CopyData entityTest2CopyData = s.obtainCopyData(EntityTest2CopyData.class);

            if (entityTest2CopyData.name.equals("张三")) {
                return true;
            }
            return false;


        }, EntityTest2BrokenRuleMessage.testError);

    }
}
