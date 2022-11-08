package cn.easylib.domain.visual;

import cn.easylib.domain.visual.command.CommandDescriptor;
import cn.easylib.domain.visual.entity.EntityDescriptor;
import cn.easylib.domain.visual.event.EventDescriptor;
import cn.easylib.domain.visual.event.EventSubscriberDescriptor;
import cn.easylib.domain.visual.rule.RuleDescriptor;
import cn.easylib.domain.visual.service.DomainServiceDescriptor;

import java.util.List;

public class DomainModelDescriptor {

    private final List<RuleDescriptor> ruleDescriptorList;
    private final List<EventDescriptor> eventDescriptors;
    private final List<DomainServiceDescriptor> domainServiceDescriptors;
    private final List<CommandDescriptor> commandDescriptorList;
    private final EntityDescriptor entityDescriptor;

    public DomainModelDescriptor(
            EntityDescriptor entityDescriptor,
            List<RuleDescriptor> ruleDescriptorList,
            List<EventDescriptor> eventDescriptors,
            List<DomainServiceDescriptor> domainServiceDescriptors,
            List<CommandDescriptor> commandDescriptorList) {

        this.entityDescriptor = entityDescriptor;
        this.ruleDescriptorList = ruleDescriptorList;
        this.eventDescriptors = eventDescriptors;
        this.domainServiceDescriptors = domainServiceDescriptors;
        this.commandDescriptorList = commandDescriptorList;
    }

    public List<RuleDescriptor> getRuleDescriptorList() {
        return ruleDescriptorList;
    }


    public List<DomainServiceDescriptor> getDomainServiceDescriptors() {
        return domainServiceDescriptors;
    }

    public List<CommandDescriptor> getCommandDescriptorList() {
        return commandDescriptorList;
    }

    public EntityDescriptor getEntityDescriptor() {
        return entityDescriptor;
    }

    public List<EventDescriptor> getEventDescriptors() {
        return eventDescriptors;
    }
}
