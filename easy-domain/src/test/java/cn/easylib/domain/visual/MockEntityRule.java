package cn.easylib.domain.visual;

import cn.easylib.domain.rules.EntityRule;
import cn.easylib.domain.visual.rule.EntityRuleVisual;

@EntityRuleVisual(description = "场景1")
public class MockEntityRule extends EntityRule<MockEntity> {
    public MockEntityRule() {
        super();
    }

    @Override
    public void init() {

        this.addRule(t -> {

            return t.showName() != null;

        }, MockEntityBrokenRuleMessage.Name_Error, "");
    }
}
