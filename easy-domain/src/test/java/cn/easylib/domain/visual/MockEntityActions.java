package cn.easylib.domain.visual;

import cn.easylib.domain.base.Action;
import cn.easylib.domain.base.EntityAction;

public class MockEntityActions extends EntityAction {

    public static final Action showNameAction = Action.build("showNameAction", "测试Action");

    public static final EntityAction  action = new MockEntityActions();

    @Override
    protected void populateAction() {

        this.populateActions().put(showNameAction.getActionCode(), showNameAction);

    }
}
