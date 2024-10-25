package cn.easylib.domain.visual.entity;

import cn.easylib.domain.base.*;
import cn.easylib.domain.rules.EntityRule;

public class EntityTest2 extends EntityBase<Long> implements ICustomValidator<EntityTest2>, ICopyData<EntityTest2CopyData> {

    private String name;

    public EntityTest2(Long id, String name) {
        this.setNewEntity(true);
        this.setId(id);
        this.setName(name);
    }

    public void updateForUse(String newName) {
        this.actionCollector.put(EntityTest2Action.testAction);
        this.copyDataCollector.set(this).putExtraParam("xxx", "xxxx1");
        this.setName(newName);
    }

    @Override
    protected BrokenRuleMessage getBrokenRuleMessages() {
        return new EntityTest2BrokenRuleMessage();
    }

    @Override
    public Boolean validate(EntityRule<EntityTest2> rule) {
        return rule.isSatisfy(this);
    }

    @Override
    public EntityAction entityActions() {
        return new EntityTest2Action();
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public EntityTest2CopyData copy() {
        EntityTest2CopyData entityTest2CopyData = new EntityTest2CopyData();
        entityTest2CopyData.name = this.getName();
        return entityTest2CopyData;
    }
}
