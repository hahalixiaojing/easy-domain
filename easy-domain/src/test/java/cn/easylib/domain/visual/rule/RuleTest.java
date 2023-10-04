package cn.easylib.domain.visual.rule;

import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.rules.EntityRule;
import cn.easylib.domain.visual.MockEntity;
import cn.easylib.domain.visual.MockEntityBrokenRuleMessage;
import cn.easylib.domain.visual.MockEntityRule;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RuleTest {

    @Test
    public void parse() {


        RuleParser ruleParser = new RuleParser();

        ruleParser.registerDomainRule(MockEntity.class, new IRuleFinder() {
            @Override
            public <T extends EntityBase<?>> RuleFinderObject findEntityRuleList(Class<T> cls) {

                ArrayList<EntityRule<?>> classes = new ArrayList<>();
                classes.add(new MockEntityRule());
                return new RuleFinderObject(classes, MockEntityBrokenRuleMessage.message);
            }
        });


        List<RuleDescriptorGroup> parse = ruleParser.parse(MockEntity.class);

        System.out.println(JSON.toJSONString(parse));


    }
}




