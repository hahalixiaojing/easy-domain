package cn.easylib.domain.base;

import cn.easylib.domain.rules.EntityRule;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author lixiaojing10
 */
public class ActionEntityTest {

    @Test
    public void action() {

        MyEntity myEntity = new MyEntity();
        myEntity.alter();

        myEntity.validate();


        boolean b = myEntity.actionCollector.containAction(MyEntityAction.alter);

        Assert.assertTrue(b);

    }

}

class MyEntity extends EntityBase<Long> {

    private String name = "zs";

    public void alter() {
        this.actionCollector.put(MyEntityAction.alter, name);
    }


    @Override
    public Boolean validate() {
        return new MyEntityRule().isSatisfy(this);
    }

    @Override
    protected BrokenRuleMessage getBrokenRuleMessages() {
        return null;
    }

    @Override
    public EntityAction entityActions() {
        return new MyEntityAction();

    }
}

class MyEntityRule extends EntityRule<MyEntity> {
    public MyEntityRule() {

        this.addRule(s -> {

                    String s1 = s.actionCollector.obtainActionParam(MyEntityAction.alter, String.class);
                    return s1.equals("zs");

                }, "",
                model -> model.actionCollector.containActions(MyEntityAction.alter));

    }
}

class MyEntityAction extends EntityAction {

    public static final Action alter = Action.build("alter");

    @Override
    protected void populateAction() {
        this.populateActions().put(alter.getActionCode(), alter);
    }
}
