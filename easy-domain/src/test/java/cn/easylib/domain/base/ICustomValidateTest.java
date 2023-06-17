package cn.easylib.domain.base;

import cn.easylib.domain.rules.EntityRule;
import org.junit.Assert;
import org.junit.Test;

/**
 * 自定义验证测试
 *
 * @author lixiaojing10
 * @date 2021/12/22 3:45 下午
 */
public class ICustomValidateTest {

    @Test
    public void demoData() {
        DemoData demoData = new DemoData();

        Boolean validate = demoData.validate(new DemoEntityRule());
        Assert.assertFalse(validate);
    }

}


class DemoData extends EntityBase<Long> implements ICustomValidator<DemoData> {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public Boolean validate() {
        return null;
    }

    @Override
    protected BrokenRuleMessage getBrokenRuleMessages() {
        return new DemoDataBrokeRuleMessage();
    }

    @Override
    public Boolean validate(EntityRule<DemoData> rule) {
        return rule.isSatisfy(this);
    }
}

class DemoEntityRule extends EntityRule<DemoData> {

    public DemoEntityRule() {
        this.isBlank("name", DemoDataBrokeRuleMessage.Name_Error);
    }
}

class DemoDataBrokeRuleMessage extends BrokenRuleMessage {

    public static final String Name_Error = "Name_Error";

    @Override
    protected void populateMessage() {
        this.getMessages().put(Name_Error, "名字Error");
    }
}