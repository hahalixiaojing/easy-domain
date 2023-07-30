package cn.easylib.domain.base;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class EntityActionCollectorTest {


    private EntityActionCollector entityActionCollector;

    @Before
    public void init() {
        entityActionCollector = new EntityActionCollector(new TestAction());
    }


    @Test
    public void testContainActions() {
        entityActionCollector.clear();
        entityActionCollector.put(TestAction.actionA);
        boolean result = entityActionCollector.containActions(TestAction.actionA);
        Assert.assertTrue(result);
    }

    @Test
    public void testContainAnyAction() throws Exception {

        entityActionCollector.clear();
        entityActionCollector.put(TestAction.actionA);
        entityActionCollector.put(TestAction.actionB);

        boolean result = entityActionCollector.containAnyAction(TestAction.actionA);
        Assert.assertTrue(result);
        boolean resultFalse = entityActionCollector.containAnyAction(TestAction.actionC);
        Assert.assertFalse(resultFalse);
    }

    @Test
    public void testContainAction() {

        entityActionCollector.clear();
        entityActionCollector.put(TestAction.actionA);

        boolean result = entityActionCollector.containAction(TestAction.actionA);
        Assert.assertTrue(result);
    }

    @Test
    public void testNotContainAction() {
        entityActionCollector.clear();
        entityActionCollector.put(TestAction.actionA);
        boolean result = entityActionCollector.notContainAction(TestAction.actionB);
        Assert.assertTrue(result);
    }
}

class TestAction extends EntityAction {

    public static final Action actionA = Action.build("actionA");
    public static final Action actionB = Action.build("actionB");
    public static final Action actionC = Action.build("actionC");

    @Override
    protected void populateAction() {
        this.populateActions().put(actionA.getActionCode(), actionA);
        this.populateActions().put(actionB.getActionCode(), actionB);
        this.populateActions().put(actionC.getActionCode(), actionC);

    }
}