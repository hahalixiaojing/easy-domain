package easy.domain.base;

import easy.domain.rules.EntityRule;
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

    public void alter() {
        this.actionCollector.put(MyEntityAction.alter);
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
                    return true;
                }, "", "",
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
