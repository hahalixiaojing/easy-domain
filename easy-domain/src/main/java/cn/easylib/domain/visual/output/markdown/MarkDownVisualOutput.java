package cn.easylib.domain.visual.output.markdown;

import cn.easylib.domain.visual.DomainModelVisualInfo;
import cn.easylib.domain.visual.IVisualOutput;
import cn.easylib.domain.visual.application.IApplicationServiceVisualOutput;
import cn.easylib.domain.visual.entity.EntityDescriptor;
import cn.easylib.domain.visual.entity.IEntityVisualOutput;
import cn.easylib.domain.visual.event.IEventSubscriberVisualOutput;
import cn.easylib.domain.visual.event.IEventVisualOutput;
import cn.easylib.domain.visual.rule.IEntityRuleVisualOutput;
import org.apache.commons.lang3.SystemUtils;

public class MarkDownVisualOutput implements IVisualOutput {

    private final IApplicationServiceVisualOutput applicationServiceVisualOutput;
    private final IEventVisualOutput eventVisualOutput;
    private final IEventSubscriberVisualOutput eventSubscriberVisualOutput;
    private final IEntityRuleVisualOutput entityRuleVisualOutput;
    private final IEntityVisualOutput entityVisualOutput;

    public MarkDownVisualOutput() {

        applicationServiceVisualOutput = new MarkDownApplicationServiceVisualOutput();
        eventVisualOutput = new MarkDownEventVisualOutput();
        eventSubscriberVisualOutput = new MarkDownEventSubscriberVisualOutput();
        entityRuleVisualOutput = new MarkDownEntityRuleVisualOutput();
        entityVisualOutput = new MarkDownEntityVisualOutput();
    }

    @Override
    public String output(DomainModelVisualInfo domainModelVisualInfo) {

        EntityDescriptor first = domainModelVisualInfo.getEntityDescriptorList()
                .stream()
                .filter(EntityDescriptor::getRoot)
                .findFirst()
                .orElse(null);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("# 可视化文档");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append("## * 实体模型");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append(entityVisualOutput.output(domainModelVisualInfo.getEntityDescriptorList()));
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);

        stringBuilder.append("## * 实体规则");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append(entityRuleVisualOutput.output(domainModelVisualInfo.getRuleDescriptorList()));
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);

        stringBuilder.append("## * 领域事件");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append(eventVisualOutput.output(domainModelVisualInfo.getEventDescriptors(), first));

        stringBuilder.append("## * 领域事件订阅");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append(eventSubscriberVisualOutput.output(domainModelVisualInfo.getEventDescriptors()));

        stringBuilder.append("## * 领域服务");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);

        stringBuilder.append("## * 应用服务");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        applicationServiceVisualOutput.output(domainModelVisualInfo.getApplicationDescriptors());

        return stringBuilder.toString();
    }
}
