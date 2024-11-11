package cn.easylib.domain.base.test2.action;

import cn.easylib.domain.base.Action;
import cn.easylib.domain.base.EntityAction;

public class PersonAction extends EntityAction {

    public static final Action START_ACTION = Action.build("startAction", "启动");
    public static final Action END_ACTION = Action.build("endAction", "停止");
    public static final Action UPDATE_ACTION = Action.build("updateAction", "更新");
    public static final Action UPDATE_STATUS_ACTION = Action.build("updateAction", "更新状态");


    public static final EntityAction INSTANCE = new PersonAction();

    @Override
    protected void populateAction() {

        this.put(START_ACTION);
        this.put(END_ACTION);
        this.put(UPDATE_ACTION);
        this.put(UPDATE_STATUS_ACTION);

    }
}
