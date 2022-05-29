package easy.domain.rules;

import easy.domain.base.BrokenRuleObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class EntityRule<T extends BrokenRuleObject> implements IRule<T> {

    private final ConcurrentHashMap<String, List<RuleItem<T>>> rules;
    private final List<RuleItem<T>> classRules;
    private final DefaultActiveRuleCondition<T> defaultCondition = new DefaultActiveRuleCondition<>();

    public EntityRule() {
        this.rules = new ConcurrentHashMap<>();
        this.classRules = new ArrayList<>();
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

    /**
     * 是否为null和空字符或空白字符串
     *
     * @param property   要验证的属性性
     * @param messageKey 错误消息ID
     */
    public void isBlank(String property, String messageKey, String alias) {
        IsBlankRule<T> rule = new IsBlankRule<>(property);
        this.addRule(property, rule, messageKey, alias, defaultCondition);
    }

    /**
     * 是否为电子邮件字符串
     *
     * @param property   要验证的属性名称 例如：name, address.city
     * @param messageKey 错误描述KEY
     */
    public void email(String property, String messageKey, String alias) {
        EmailRule<T> rule = new EmailRule<>(property);
        this.addRule(property, rule, messageKey, alias, defaultCondition);
    }

    /**
     * 数字应该大于
     *
     * @param property   要验证的属性名称 例如：name, address.city
     * @param v          比较值
     * @param messageKey 错误描述KEY
     */
    public <V extends Number> void numberShouldGreaterThan(String property,
                                                           V v, String messageKey, String alias) {
        NumberShouldGreaterThanRule<T, V> rule = new NumberShouldGreaterThanRule<>(
                property, v);
        this.addRule(property, rule, messageKey, alias, defaultCondition);
    }

    /**
     * 数字应该小于
     *
     * @param property   要验证的属性名称 例如：name, address.city
     * @param v          比较值
     * @param messageKey 错误描述KEY
     */
    public <V extends Number> void numberShouldLessThan(String property,
                                                        V v, String messageKey, String alias) {
        NumberShouldLessThanRule<T, V> rule = new NumberShouldLessThanRule<>(
                property, v);

        this.addRule(property, rule, messageKey, alias, defaultCondition);
    }

    /**
     * 日期就是大于
     *
     * @param property   要验证的属性名称 例如：name, address.city
     * @param date       比较值
     * @param messageKey 错误描述KEY
     */
    public void dateShouldGreaterThan(String property, Date date,
                                      String messageKey, String alias) {
        DateShouldGreaterThanRule<T> rule = new DateShouldGreaterThanRule<>(
                property, date);

        this.addRule(property, rule, messageKey, alias, defaultCondition);
    }

    /**
     * 日期应小于
     *
     * @param property   要验证的属性名称 例如：name, address.city
     * @param date       比较值
     * @param messageKey 错误描述KEY
     */
    public void dateShouldLessThan(String property, Date date, String messageKey, String alias) {
        DateShouldLessThanRule<T> rule = new DateShouldLessThanRule<>(
                property, date);

        this.addRule(property, rule, messageKey, alias, defaultCondition);

    }

    /**
     * boolean值应该等于
     *
     * @param property   要验证的属性名称 例如：name, address.city
     * @param bool       比较值
     * @param messageKey 错误描述KEY
     */
    public void booleanShouldEqual(String property, boolean bool,
                                   String messageKey, String alias) {
        BooleanRule<T> rule = new BooleanRule<>(property, bool);
        this.addRule(property, rule, messageKey, alias, this.defaultCondition);
    }

    /**
     * 数字应该 等于
     *
     * @param property   要验证的属性名称 例如：name, address.city
     * @param value      比较值
     * @param messageKey 错误描述KEY
     */
    public <V extends Number> void numberShouldEqual(String property,
                                                     V value, String messageKey, String alias) {
        NumberEqualRule<T, V> rule = new NumberEqualRule<>(
                property, value);
        this.addRule(property, rule, messageKey, alias, defaultCondition);
    }

    /**
     * 添加自定义规则
     *
     * @param property   要验证的属性名称 例如：name, address.city
     * @param rule       自定义规则
     * @param messageKey 错误描述KEY
     */
    public void addRule(String property, IRule<T> rule, String messageKey, String alias, IActiveRuleCondition<T> condition) {
        if (this.rules.containsKey(property)) {
            this.rules.get(property).add(new RuleItem<>(rule, messageKey, alias, condition));
        } else {
            List<RuleItem<T>> ruleItems = new ArrayList<>();
            ruleItems.add(new RuleItem<>(rule, messageKey, alias, condition));

            this.rules.put(property, ruleItems);
        }
    }

    /**
     * 添加自定义规则
     *
     * @param property   要验证的属性名称 例如：name, address.city
     * @param rule       自定义规则
     * @param messageKey 错误描述KEY
     * @param alias      别名
     */
    public void addRule(String property, IRule<T> rule, String messageKey, String alias) {
        this.addRule(property, rule, messageKey, alias, this.defaultCondition);
    }

    /**
     * 添加自定义规则
     *
     * @param rule       自定义规则
     * @param messageKey 错误描述KEY
     * @param alias      别名
     */
    public void addRule(IRule<T> rule, String messageKey, String alias) {
        this.classRules.add(new RuleItem<>(rule, messageKey, alias, this.defaultCondition));
    }

    public void addRule(IRule<T> rule, String messageKey, String alias, IActiveRuleCondition<T> condition) {
        this.addRule("", rule, messageKey, alias, condition);

    }

    public void addParamRule(String property, IParamRule<T> paramRule, String messageKey, String alias, IActiveRuleCondition<T> condition) {
        if (this.rules.containsKey(property)) {
            this.rules.get(property).add(new RuleItem<>(paramRule, messageKey, alias, condition));
        }
    }

    public void addParamRule(String property, IParamRule<T> paramRule, String messageKey, String alias) {
        this.addParamRule(property, paramRule, messageKey, alias, this.defaultCondition);
    }

    public void addParamRule(IParamRule<T> paramRule, String messageKey, String alias, IActiveRuleCondition<T> condition) {
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

                    }
                } else if (rule.getRule() != null) {
                    if (!rule.getRule().isSatisfy(model)) {
                        propertyIsValid = false;
                        model.addBrokenRule(rule.getMessageKey(), entry.getKey(), rule.getAlias());
                        break;
                    }
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
                    model.addParamBrokenRule(rule.getMessageKey(), "", result.getParams(), rule.getAlias(), result.isAutoFormat());
                    break;
                }

            } else if (rule.getRule() != null) {
                if (!rule.getRule().isSatisfy(model)) {
                    classIsValid = false;
                    model.addBrokenRule(rule.getMessageKey(), rule.getAlias());
                    break;
                }
            }
        }
        return propertyIsValid && classIsValid;
    }
}
