package cn.easylib.domain.visual.output.markdown;

import cn.easylib.domain.visual.rule.IEntityRuleVisualOutput;
import cn.easylib.domain.visual.rule.RuleDescriptor;
import cn.easylib.domain.visual.rule.RuleDescriptorGroup;
import org.apache.commons.lang3.SystemUtils;

import java.util.List;

public class MarkDownEntityRuleVisualOutput implements IEntityRuleVisualOutput {
    @Override
    public String output(List<RuleDescriptorGroup> group) {

        StringBuilder stringBuilder = new StringBuilder();


        group.forEach(g->{
            String s = this.buildEntityRule(g.getRuleDescriptorList());
            stringBuilder.append(s);
        });

        return stringBuilder.toString();
    }

    private String buildEntityRule(List<RuleDescriptor> ruleDescriptor) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("|规则编码|规则描述|是否有条件执行|");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append("|-|-|-|");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);

        ruleDescriptor.forEach(r -> {

            stringBuilder.append("|");
            stringBuilder.append(r.getRuleKey());
            stringBuilder.append("|");
            stringBuilder.append(r.getRuleDescription());
            stringBuilder.append("|");
            stringBuilder.append(r.isWithConditionRule());
            stringBuilder.append("|");
            stringBuilder.append(SystemUtils.LINE_SEPARATOR);

        });

        return stringBuilder.toString();
    }
}
