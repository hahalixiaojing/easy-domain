package cn.easylib.domain.visual.rule;

import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.rules.EntityRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleParser {


    private final Map<Class<?>, IRuleFinder> ruleFinderMap = new HashMap<>();

    public <T extends EntityBase<?>> void registerDomainRule(Class<T> entityClass, IRuleFinder finder) {
        this.ruleFinderMap.put(entityClass,finder);
    }

    public <T extends EntityBase<?>> List<RuleDescriptor> parse(Class<T> cls) {

        List<Class<?>> entityRuleClsList = this.ruleFinderMap.get(cls).findList(cls);
//        entityRuleClsList.stream().map(s->{
//            //todo 构造函数实例化处理
//            //s.getConstructors();
//
//        }).collect(Collectors.toList());

        return new ArrayList<>();
    }

}
