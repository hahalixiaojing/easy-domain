package cn.easylib.domain.visual;

import cn.easylib.domain.visual.application.ApplicationDescriptor;
import cn.easylib.domain.visual.entity.EntityDescriptor;
import cn.easylib.domain.visual.event.EventDescriptor;
import cn.easylib.domain.visual.rule.RuleDescriptor;
import cn.easylib.domain.visual.rule.RuleDescriptorGroup;
import cn.easylib.domain.visual.service.DomainServiceDescriptor;

import java.util.List;

public class DomainModelVisualInfo {

    private final EntityDescriptor entityDescriptor;
    private final List<RuleDescriptorGroup> ruleDescriptorList;
    private final List<EventDescriptor> eventDescriptors;
    private final List<DomainServiceDescriptor> domainServiceDescriptors;

    private final List<ApplicationDescriptor> applicationDescriptors;

    public DomainModelVisualInfo(EntityDescriptor entityDescriptor,
                                 List<RuleDescriptorGroup> ruleDescriptorList,
                                 List<EventDescriptor> eventDescriptors,
                                 List<DomainServiceDescriptor> domainServiceDescriptors,
                                 List<ApplicationDescriptor> applicationDescriptors
    ) {
        this.entityDescriptor = entityDescriptor;
        this.ruleDescriptorList = ruleDescriptorList;
        this.eventDescriptors = eventDescriptors;
        this.domainServiceDescriptors = domainServiceDescriptors;
        this.applicationDescriptors = applicationDescriptors;
    }

    public EntityDescriptor getEntityDescriptor() {
        return entityDescriptor;
    }

    public List<RuleDescriptorGroup> getRuleDescriptorList() {
        return ruleDescriptorList;
    }

    public List<EventDescriptor> getEventDescriptors() {
        return eventDescriptors;
    }

    public List<DomainServiceDescriptor> getDomainServiceDescriptors() {
        return domainServiceDescriptors;
    }

    public List<ApplicationDescriptor> getApplicationDescriptors() {
        return applicationDescriptors;
    }
}
