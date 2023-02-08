package cn.easylib.domain.visual.rule;

import cn.easylib.domain.base.BrokenRuleMessage;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.rules.DefaultActiveRuleCondition;
import cn.easylib.domain.rules.EntityRule;
import cn.easylib.domain.rules.RuleItem;
import cn.easylib.domain.visual.VisualException;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class RuleParser {


    private final Map<Class<?>, IRuleFinder> ruleFinderMap = new HashMap<>();

    public <T extends EntityBase<?>> void registerDomainRule(Class<T> entityClass, IRuleFinder finder) {
        this.ruleFinderMap.put(entityClass, finder);
    }


    public <T extends EntityBase<?>> List<RuleDescriptorGroup> parse(Class<T> cls) {

        IRuleFinder.RuleFinderObject entityRuleInfo = Optional.ofNullable(this.ruleFinderMap.get(cls))
                .map(f -> f.findEntityRuleList(cls)).orElse(null);


        return Optional.ofNullable(entityRuleInfo).map(IRuleFinder.RuleFinderObject::getEntityRuleCls)
                .orElse(Collections.emptyList()).stream().map(s ->
                        Arrays.stream(s.getConstructors())
                                .filter(c -> c.getParameterTypes().length == 0)
                                .findFirst()
                                .map(c -> buildRuleDescriptor(entityRuleInfo.getBrokenRuleMessage(), c))
                                .orElse(null))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private <T extends EntityBase<?>> RuleDescriptorGroup buildRuleDescriptor(BrokenRuleMessage brokenRuleMessage,

                                                                              Constructor<?> constructor) {

        try {

            EntityRuleVisual annotation = constructor.getDeclaringClass().getAnnotation(EntityRuleVisual.class);

            String entityRuleDescription = "";
            if (annotation != null) {
                entityRuleDescription = annotation.description();
            }

            EntityRule<T> entityRule = (EntityRule<T>) constructor.newInstance();
            List<RuleItem<T>> ruleItems = entityRule.allRuleItems();

            List<RuleDescriptor> collect = ruleItems.stream().map(r -> {
                String ruleDescription = brokenRuleMessage.getRuleDescription(r.getMessageKey());
                return new RuleDescriptor(r.getMessageKey(),
                        ruleDescription,
                        !(r.getCondition() instanceof DefaultActiveRuleCondition));

            }).collect(Collectors.toList());

            return new RuleDescriptorGroup(entityRuleDescription, collect);


        } catch (Exception e) {
            throw new VisualException(e);
        }
    }

}
