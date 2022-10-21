package cn.easylib.domain.rules;

import cn.easylib.domain.base.BrokenRuleObject;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntityRule<T extends BrokenRuleObject> implements IRule<T>, IRuleBuild {

    private final ConcurrentHashMap<String, List<RuleItem<T>>> rules;
    private final List<RuleItem<T>> classRules;
    private final DefaultActiveRuleCondition<T> defaultCondition = new DefaultActiveRuleCondition<>();

    private final boolean failFast;

    public EntityRule() {
        this(true);
    }

    public EntityRule(boolean failFast) {
        this.failFast = failFast;
        this.rules = new ConcurrentHashMap<>();
        this.classRules = new ArrayList<>();
        this.init();
    }

    public List<RuleItem<T>> allRuleItems() {
        return Stream.concat(this.rules.values().stream().flatMap(Collection::stream),
                        this.classRules.stream())
                .collect(Collectors.toList());
    }


    public IRule<T> findRuleByMessageKey(String messageKey) {
        return rules.values().stream().flatMap(List::stream)
                .filter(r -> r.getMessageKey().equals(messageKey))
                .findFirst().map(RuleItem::getRule).orElseGet(

                        () -> classRules.stream()
                                .filter(clsR -> clsR.getMessageKey().equals(messageKey))
                                .findFirst()
                                .map(RuleItem::getRule)
                                .orElse(null)
                );
    }

    public List<IRule<T>> findRulesByMessageKey(String... messageKeys) {
        //todo
        return new ArrayList<>();
    }

    public void appendRule(String property, IRule<T> rule,
                           String alias,
                           String appendMessageKey,
                           String relativeMessageKey,
                           int appendPosition,
                           IActiveRuleCondition<T> condition
    ) {
        //appendPosition 0= last 1= before 2=after

        List<RuleItem<T>> ruleItems = this.rules.get(property);
        RuleItem<T> tRuleItem = new RuleItem<>(rule, appendMessageKey, alias, condition);

        this.appendRule(ruleItems, tRuleItem, relativeMessageKey, appendPosition);
    }

    public void appendWithParamRule(String property, IParamRule<T> rule,
                                    String alias,
                                    String appendMessageKey,
                                    String relativeMessageKey,
                                    int appendPosition,
                                    IActiveRuleCondition<T> condition) {
        List<RuleItem<T>> ruleItems = this.rules.get(property);
        RuleItem<T> tRuleItem = new RuleItem<>(rule, appendMessageKey, alias, Optional.ofNullable(condition)
                .orElse(this.defaultCondition));
        this.appendRule(ruleItems, tRuleItem, relativeMessageKey, appendPosition);

    }

    public void appendRule(IRule<T> rule,
                           String alias,
                           String appendMessageKey,
                           String relativeMessageKey,
                           int appendPosition,
                           IActiveRuleCondition<T> condition) {

        RuleItem<T> tRuleItem = new RuleItem<>(rule, appendMessageKey, alias, Optional.ofNullable(condition)
                .orElse(this.defaultCondition));
        this.appendRule(classRules, tRuleItem, relativeMessageKey, appendPosition);
    }

    public void appendWithParamRule(IParamRule<T> rule,
                                    String alias,
                                    String appendMessageKey,
                                    String relativeMessageKey,
                                    int appendPosition,
                                    IActiveRuleCondition<T> condition) {

        RuleItem<T> tRuleItem = new RuleItem<>(rule, appendMessageKey, alias, Optional.ofNullable(condition)
                .orElse(this.defaultCondition));
        this.appendRule(classRules, tRuleItem, relativeMessageKey, appendPosition);

    }


    private void appendRule(List<RuleItem<T>> rules, RuleItem<T> rule,
                            String relativeMessageKey, int appendPosition) {

        if (rules == null) {
            return;
        }

        if (appendPosition == 0) {
            rules.add(rule);
        } else {
            Integer index = null;
            for (int i = 0; i < rules.size(); i++) {
                if (rules.get(i).getMessageKey().equals(relativeMessageKey)) {
                    index = i;
                    break;
                }
            }
            if (index != null) {
                if (appendPosition == 1) {
                    rules.add(index, rule);
                } else if (appendPosition == 2) {
                    rules.add(index + 1, rule);
                }
            }
        }
    }

    public void replaceRule(String property, IRule<T> rule, String replaceMessageKey, String newMessageKey,
                            String alias,
                            IActiveRuleCondition<T> condition) {

        List<RuleItem<T>> ruleItems = this.rules.get(property);
        if (ruleItems == null) {
            return;
        }
        for (int i = 0; i < ruleItems.size(); i++) {
            if (ruleItems.get(i).getMessageKey().equals(replaceMessageKey)) {
                ruleItems.set(i, new RuleItem<>(rule, newMessageKey, alias, condition));
                break;
            }
        }
    }

    public void replaceRule(String property, IRule<T> rule, String replaceMessageKey,
                            String newMessageKey,
                            String alias) {
        this.replaceRule(property, rule, replaceMessageKey, newMessageKey, alias, defaultCondition);
    }

    public void replaceRule(IRule<T> rule, String replaceMessageKey,
                            String newMessageKey, String alias) {
        this.replaceRule(rule, replaceMessageKey, newMessageKey, alias, this.defaultCondition);
    }

    public void replaceRule(IRule<T> rule, String replaceMessageKey,
                            String newMessageKey, String alias, IActiveRuleCondition<T> condition) {

        this.replaceClassRule(replaceMessageKey, new RuleItem<>(rule, newMessageKey, alias, condition));

    }

    public void replaceWithParamRule(String property, IParamRule<T> paramRule, String replaceMessageKey,
                                     String newMessageKey, String alias,
                                     IActiveRuleCondition<T> condition) {


        List<RuleItem<T>> ruleItems = this.rules.get(property);
        if (ruleItems == null) {
            return;
        }
        for (int i = 0; i < ruleItems.size(); i++) {
            if (ruleItems.get(i).getMessageKey().equals(replaceMessageKey)) {
                ruleItems.set(i, new RuleItem<>(paramRule, newMessageKey, alias, condition));
            }
        }
    }

    public void replaceWithParamRule(String property, IParamRule<T> paramRule, String replaceMessageKey,
                                     String newMessageKey, String alias) {
        this.replaceWithParamRule(property, paramRule, replaceMessageKey, newMessageKey, alias, this.defaultCondition);
    }

    public void replaceWithParamRule(IParamRule<T> paramRule, String replaceMessageKey, String newMessageKey,
                                     String alias,
                                     IActiveRuleCondition<T> condition) {

        this.replaceClassRule(replaceMessageKey, new RuleItem<>(paramRule, newMessageKey, alias, condition));
    }

    public void replaceWithParamRule(IParamRule<T> paramRule, String replaceMessageKey, String newMessageKey,
                                     String alias) {


        this.replaceClassRule(replaceMessageKey, new RuleItem<>(paramRule, newMessageKey, alias,
                this.defaultCondition));

    }


    private void replaceClassRule(String replaceMessageKey, RuleItem<T> ruleItem) {
        for (int i = 0; i < this.classRules.size(); i++) {
            if (this.classRules.get(i).getMessageKey().equals(replaceMessageKey)) {
                this.classRules.set(i, ruleItem);
                break;
            }
        }

    }

    public void removeRule(String messageKey) {
        rules.forEach((k, v) -> v.stream()
                .filter(r -> r.getMessageKey().equals(messageKey))
                .findFirst()
                .ifPresent(v::remove)
        );

        classRules.stream().filter(r -> r.getMessageKey().equals(messageKey))
                .findFirst()
                .ifPresent(classRules::remove);
    }

    public void isBlank(String property, String messageKey, String alias) {
        IsBlankRule<T> rule = new IsBlankRule<>(property);
        this.addRule(property, rule, messageKey, alias, defaultCondition);
    }

    public void email(String property, String messageKey, String alias) {
        EmailRule<T> rule = new EmailRule<>(property);
        this.addRule(property, rule, messageKey, alias, defaultCondition);
    }

    public <V extends Number> void numberShouldGreaterThan(String property,
                                                           V v, String messageKey, String alias) {
        NumberShouldGreaterThanRule<T, V> rule = new NumberShouldGreaterThanRule<>(
                property, v);
        this.addRule(property, rule, messageKey, alias, defaultCondition);
    }
    public <V extends Number> void numberShouldLessThan(String property,
                                                        V v, String messageKey, String alias) {
        NumberShouldLessThanRule<T, V> rule = new NumberShouldLessThanRule<>(
                property, v);

        this.addRule(property, rule, messageKey, alias, defaultCondition);
    }

    public void dateShouldGreaterThan(String property, Date date,
                                      String messageKey, String alias) {
        DateShouldGreaterThanRule<T> rule = new DateShouldGreaterThanRule<>(
                property, date);

        this.addRule(property, rule, messageKey, alias, defaultCondition);
    }
    public void dateShouldLessThan(String property, Date date, String messageKey, String alias) {
        DateShouldLessThanRule<T> rule = new DateShouldLessThanRule<>(
                property, date);

        this.addRule(property, rule, messageKey, alias, defaultCondition);

    }

    public void booleanShouldEqual(String property, boolean bool,
                                   String messageKey, String alias) {
        BooleanRule<T> rule = new BooleanRule<>(property, bool);
        this.addRule(property, rule, messageKey, alias, this.defaultCondition);
    }

    public <V extends Number> void numberShouldEqual(String property,
                                                     V value, String messageKey, String alias) {
        NumberEqualRule<T, V> rule = new NumberEqualRule<>(
                property, value);
        this.addRule(property, rule, messageKey, alias, defaultCondition);
    }

    public void addRule(String property, IRule<T> rule, String messageKey, String alias, IActiveRuleCondition<T> condition) {
        if (this.rules.containsKey(property)) {
            this.rules.get(property).add(new RuleItem<>(rule, messageKey, alias, condition));
        } else {
            List<RuleItem<T>> ruleItems = new ArrayList<>();
            ruleItems.add(new RuleItem<>(rule, messageKey, alias, condition));

            this.rules.put(property, ruleItems);
        }
    }

    public void addRule(String property, IRule<T> rule, String messageKey, String alias) {
        this.addRule(property, rule, messageKey, alias, this.defaultCondition);
    }


    public void addRule(IRule<T> rule, String messageKey, String alias) {
        this.addRule(rule, messageKey, alias, this.defaultCondition);
    }

    public void addRule(IRule<T> rule, String messageKey, String alias, IActiveRuleCondition<T> condition) {
        this.classRules.add(new RuleItem<>(rule, messageKey, alias, condition));

    }

    public void addParamRule(String property, IParamRule<T> paramRule, String messageKey, String alias,
                             IActiveRuleCondition<T> condition) {
        if (this.rules.containsKey(property)) {
            this.rules.get(property).add(new RuleItem<>(paramRule, messageKey, alias, condition));
        }
    }

    public void addParamRule(String property, IParamRule<T> paramRule, String messageKey, String alias) {
        this.addParamRule(property, paramRule, messageKey, alias, this.defaultCondition);
    }

    public void addParamRule(IParamRule<T> paramRule, String messageKey, String alias,
                             IActiveRuleCondition<T> condition) {
        this.classRules.add(new RuleItem<>(paramRule, messageKey, alias, condition));
    }

    public void addParamRule(IParamRule<T> paramRule, String messageKey, String alias) {
        this.addParamRule(paramRule, messageKey, alias, this.defaultCondition);
    }


    @Override
    public boolean isSatisfy(T model) {
        boolean propertyIsValid = true;
        for (Entry<String, List<RuleItem<T>>> entry : this.rules.entrySet()) {
            for (RuleItem<T> rule : entry.getValue()) {
                if (!rule.getCondition().isActive(model)) {
                    continue;
                }

                if (rule.getParamRule() != null) {
                    Pair result = rule.getParamRule().isSatisfy(model);
                    if (!result.isSatisfy()) {
                        propertyIsValid = false;
                        model.addParamBrokenRule(rule.getMessageKey(), entry.getKey(),
                                result.getParams(), rule.getAlias(), result.isAutoFormat());
                        break;

                    }
                } else if (rule.getRule() != null) {
                    if (!rule.getRule().isSatisfy(model)) {
                        propertyIsValid = false;
                        model.addBrokenRule(rule.getMessageKey(), entry.getKey(), rule.getAlias());
                        break;
                    }
                }
            }
            if (!propertyIsValid) {
                if (failFast) {
                    break;
                }
            }
        }

        boolean classIsValid = true;
        for (RuleItem<T> rule : this.classRules) {
            if (!rule.getCondition().isActive(model)) {
                continue;
            }
            if (rule.getParamRule() != null) {
                Pair result = rule.getParamRule().isSatisfy(model);
                if (!result.isSatisfy()) {
                    classIsValid = false;
                    model.addParamBrokenRule(rule.getMessageKey(), "",
                            result.getParams(), rule.getAlias(), result.isAutoFormat());

                    if (this.failFast) {
                        break;
                    }
                }

            } else if (rule.getRule() != null) {
                if (!rule.getRule().isSatisfy(model)) {
                    classIsValid = false;
                    model.addBrokenRule(rule.getMessageKey(), rule.getAlias());
                    if (this.failFast) {
                        break;
                    }
                }
            }
        }
        return propertyIsValid && classIsValid;
    }

    @Override
    public void reset() {
        this.classRules.clear();
        this.rules.clear();
        this.init();
    }
}
