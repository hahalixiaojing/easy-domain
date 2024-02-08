package cn.easylib.domain.base;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BrokenRuleObject {

    private final List<BrokenRule> brokenRules;
    private final BrokenRuleMessage brokenRuleMessage;

    private static final EmptyBrokenRule emptyBrokenRule = new EmptyBrokenRule();


    public BrokenRuleObject() {
        this.brokenRules = new ArrayList<>();
        this.brokenRuleMessage = this.getBrokenRuleMessages();
    }

    protected abstract BrokenRuleMessage getBrokenRuleMessages();

    protected abstract String takeEntityInfo();

    public abstract Boolean validate();


    public List<BrokenRule> getBrokenRules() {
        return Collections.unmodifiableList(this.brokenRules);
    }

    public void addBrokenRule(String messageKey) {
        String message = this.brokenRuleMessage.getRuleDescription(messageKey);
        BrokenRule rule = new BrokenRule(messageKey, message);
        this.brokenRules.add(rule);
    }

    public void addBrokenRule(String messageKey, String property) {
        String message = this.brokenRuleMessage.getRuleDescription(messageKey);
        BrokenRule rule = new BrokenRule(messageKey, message, property);
        this.brokenRules.add(rule);
    }

    public void addBrokenRule(String messageKey, String property, String alias) {
        String message = this.brokenRuleMessage.getRuleDescription(messageKey);
        BrokenRule rule = new BrokenRule(messageKey, message, property, alias, null);
        this.brokenRules.add(rule);
    }

    public void addParamBrokenRule(String messageKey, Object[] params, boolean isAutoFormat) {
        this.addParamBrokenRule(messageKey, "", params, "", isAutoFormat);
    }

    public void addParamBrokenRule(String messageKey, String property, Object[] params,
                                   String alias,
                                   boolean isAutoFormat) {

        final String message = this.brokenRuleMessage.getRuleDescription(messageKey);
        String realMessage;

        if (isAutoFormat) {
            realMessage = String.format(message, params);
        } else {
            realMessage = message;
        }
        final BrokenRule rule = new BrokenRule(messageKey, realMessage, property, alias, params);
        this.brokenRules.add(rule);


    }

    public BrokenRule findBrokenRule(String property) {
        BrokenRule rule = null;
        for (BrokenRule b : this.brokenRules) {
            if (b.getProperty().equals(property)) {
                rule = b;
                break;
            }
        }
        if (rule == null) {
            return emptyBrokenRule;
        }
        return rule;
    }

    public void throwBrokenRuleException() {

        BrokenRuleException brokenRuleException = this.exceptionCause();
        if (brokenRuleException != null) {
            throw brokenRuleException;
        }
    }

    public BrokenRuleException exceptionCause() {
        if (this.getBrokenRules().size() > 0) {
            BrokenRule brokenRule = this.getBrokenRules().get(0);
            return new BrokenRuleException(brokenRule.getName(),
                    brokenRule.getDescription(),
                    this.takeEntityInfo(),
                    brokenRule.getExtraData()
            );
        }
        return null;
    }

    public BrokeRuleAggregateException aggregateExceptionCause() {
        if (this.getBrokenRules().size() > 0) {

            List<BrokenRuleException> brokenRuleExceptions = new ArrayList<>();

            for (BrokenRule message : this.getBrokenRules()) {
                BrokenRuleException brokenRuleException = new BrokenRuleException(message.getName(),
                        message.getDescription(), this.takeEntityInfo(), message.getExtraData());

                brokenRuleExceptions.add(brokenRuleException);
            }
            return new BrokeRuleAggregateException(brokenRuleExceptions);
        }
        return null;
    }

    public void throwBrokeRuleAggregateException() {
        BrokeRuleAggregateException brokeRuleAggregateException = this.aggregateExceptionCause();
        if (brokeRuleAggregateException != null) {
            throw brokeRuleAggregateException;
        }
    }

    public void clearBrokenRules() {
        this.brokenRules.clear();
    }
}
