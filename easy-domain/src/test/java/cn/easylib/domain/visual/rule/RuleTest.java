package cn.easylib.domain.visual.rule;

import cn.easylib.domain.base.BrokenRuleMessage;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.rules.EntityRule;
import cn.easylib.domain.rules.EntityRuleDescriptor;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RuleTest {

    @Test
    public void parse() {


        RuleParser ruleParser = new RuleParser();

        ruleParser.registerDomainRule(TestClass.class, new IRuleFinder() {
            @Override
            public <T extends EntityBase<?>> RuleFinderObject findEntityRuleList(Class<T> cls) {

                ArrayList<Class<?>> classes = new ArrayList<>();
                classes.add(TestClassEntityRule.class);
                return new RuleFinderObject(classes,TestClassBrokenRuleMessage.message);
            }
        });


        List<RuleDescriptorGroup> parse = ruleParser.parse(TestClass.class);

        System.out.println(JSON.toJSONString(parse));


    }
}

@EntityRuleDescriptor(description = "场景1")
class TestClassEntityRule extends EntityRule<TestClass> {
    public TestClassEntityRule() {
        super();
    }

    @Override
    public void init() {

        this.addRule(t -> {

            return t.showName() != null;

        }, TestClassBrokenRuleMessage.Name_Error, "");
    }
}

class TestClassBrokenRuleMessage extends BrokenRuleMessage {

    public static final String Name_Error = "Name_Error";

    public static final BrokenRuleMessage message = new TestClassBrokenRuleMessage();

    @Override
    protected void populateMessage() {
        this.getMessages().put(Name_Error, "名字错误");
    }
}

class TestClass extends EntityBase<Long> {
    private final String name;

    public TestClass(String name) {
        this.name = name;
    }

    public TestClass() {
        this.name = "test";
    }

    @Override
    public Boolean validate() {
        return null;
    }

    public String showName() {
        return this.name;
    }

    @Override
    protected BrokenRuleMessage getBrokenRuleMessages() {
        return null;
    }
}
