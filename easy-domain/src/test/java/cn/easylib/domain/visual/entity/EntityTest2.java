package cn.easylib.domain.visual.entity;

import cn.easylib.domain.base.BrokenRuleMessage;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.base.ICopyData;
import cn.easylib.domain.base.ICustomValidator;
import cn.easylib.domain.rules.EntityRule;

public class EntityTest2 extends EntityBase<Long> implements ICustomValidator<EntityTest2>, ICopyData<EntityTest2CopyData> {

    private String name;

    public EntityTest2() {
        this.setNewEntity(true);
    }
    public void updateForUse() {

        EntityTest2CopyData copy = this.copy();

        this.actionCollector.put(null, copy);
    }

    @Override
    protected BrokenRuleMessage getBrokenRuleMessages() {
        return null;
    }

    @Override
    public EntityTest2CopyData copy() {
        return new EntityTest2CopyData();
    }

    @Override
    public Boolean validate(EntityRule<EntityTest2> rule) {
        return rule.isSatisfy(this);
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }
}
