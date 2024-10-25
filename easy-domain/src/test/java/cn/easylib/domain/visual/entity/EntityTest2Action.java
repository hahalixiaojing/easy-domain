package cn.easylib.domain.visual.entity;

import cn.easylib.domain.base.Action;
import cn.easylib.domain.base.EntityAction;

public class EntityTest2Action extends EntityAction {

    public static Action testAction = Action.build("TestAction");

    @Override
    protected void populateAction() {
        this.populateActions().put(testAction.getActionCode(), testAction);
    }
}
