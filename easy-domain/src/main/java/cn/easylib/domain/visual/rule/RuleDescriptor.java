package cn.easylib.domain.visual.rule;

public class RuleDescriptor {

    private final String ruleKey;
    private final String ruleDescription;

    public RuleDescriptor(String ruleKey, String ruleDescription) {
        this.ruleKey = ruleKey;
        this.ruleDescription = ruleDescription;
    }

    public String getRuleKey() {
        return ruleKey;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }
}
