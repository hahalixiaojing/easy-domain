package cn.easylib.domain.visual;

import cn.easylib.domain.visual.application.ApplicationDescriptor;
import cn.easylib.domain.visual.entity.EntityDescriptor;
import cn.easylib.domain.visual.entity.EnumInfoDescriptor;
import cn.easylib.domain.visual.event.EventDescriptor;
import cn.easylib.domain.visual.rule.RuleDescriptorGroup;
import cn.easylib.domain.visual.service.DomainServiceDescriptor;

import java.util.List;

public class DomainModelVisualInfo {

    private  List<EntityDescriptor> entityDescriptorList;
    private  List<RuleDescriptorGroup> ruleDescriptorList;
    private  List<EventDescriptor> eventDescriptors;
    private  List<DomainServiceDescriptor> domainServiceDescriptors;
    private  List<EnumInfoDescriptor> enumInfoDescriptorList;
    private  List<ApplicationDescriptor> applicationDescriptors;


    public List<EntityDescriptor> getEntityDescriptorList() {
        return entityDescriptorList;
    }

    public void setEntityDescriptorList(List<EntityDescriptor> entityDescriptorList) {
        this.entityDescriptorList = entityDescriptorList;
    }

    public List<RuleDescriptorGroup> getRuleDescriptorList() {
        return ruleDescriptorList;
    }

    public void setRuleDescriptorList(List<RuleDescriptorGroup> ruleDescriptorList) {
        this.ruleDescriptorList = ruleDescriptorList;
    }

    public List<EventDescriptor> getEventDescriptors() {
        return eventDescriptors;
    }

    public void setEventDescriptors(List<EventDescriptor> eventDescriptors) {
        this.eventDescriptors = eventDescriptors;
    }

    public List<DomainServiceDescriptor> getDomainServiceDescriptors() {
        return domainServiceDescriptors;
    }

    public void setDomainServiceDescriptors(List<DomainServiceDescriptor> domainServiceDescriptors) {
        this.domainServiceDescriptors = domainServiceDescriptors;
    }

    public List<EnumInfoDescriptor> getEnumInfoDescriptorList() {
        return enumInfoDescriptorList;
    }

    public void setEnumInfoDescriptorList(List<EnumInfoDescriptor> enumInfoDescriptorList) {
        this.enumInfoDescriptorList = enumInfoDescriptorList;
    }

    public List<ApplicationDescriptor> getApplicationDescriptors() {
        return applicationDescriptors;
    }

    public void setApplicationDescriptors(List<ApplicationDescriptor> applicationDescriptors) {
        this.applicationDescriptors = applicationDescriptors;
    }
}
