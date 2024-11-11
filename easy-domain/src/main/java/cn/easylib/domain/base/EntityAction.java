package cn.easylib.domain.base;

import java.util.HashMap;

/**
 * @author lixiaojing10
 */
public abstract class EntityAction {

    public static final Action NEW = Action.build("NEW", "新建");
    public static final Action DELETE = Action.build("DELETE", "删除");

    private final HashMap<String, Action> actionHashMap = new HashMap<>();

    public EntityAction() {
        this.basePopulateAction();
    }

    private void basePopulateAction() {
        this.actionHashMap.put(NEW.getActionCode(), NEW);
        this.actionHashMap.put(DELETE.getActionCode(), DELETE);

        this.populateAction();
    }

    protected abstract void populateAction();

    public final HashMap<String, Action> populateActions() {
        return this.actionHashMap;
    }

    public final void put(Action action) {
        this.populateActions().put(action.getActionCode(), action);
    }
}
