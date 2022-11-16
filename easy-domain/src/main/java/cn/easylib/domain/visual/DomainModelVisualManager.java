package cn.easylib.domain.visual;

import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.visual.command.CommandDescriptor;
import cn.easylib.domain.visual.command.CommandParser;
import cn.easylib.domain.visual.entity.EntityDescriptor;
import cn.easylib.domain.visual.entity.EntityParser;
import cn.easylib.domain.visual.event.EventDescriptor;
import cn.easylib.domain.visual.event.EventParser;
import cn.easylib.domain.visual.rule.RuleDescriptor;
import cn.easylib.domain.visual.rule.RuleParser;
import cn.easylib.domain.visual.service.DomainServiceDescriptor;
import cn.easylib.domain.visual.service.DomainServiceParser;

import java.util.*;


public class DomainModelVisualManager<T extends EntityBase<?>> {


    private CommandParser commandParser = new CommandParser();
    private EntityParser entityParser = new EntityParser();
    private EventParser eventParser = new EventParser();
    private RuleParser ruleParser = new RuleParser();
    private DomainServiceParser domainServiceParser = new DomainServiceParser();


    private final HashMap<Class<T>, DomainModelDescriptor> domainModels = new HashMap<>();

    public DomainModelVisualManager() {
    }

    public void register(Class<T> entityClass) {

        DomainModelDescriptor build = this.build(entityClass);

        this.domainModels.put(entityClass, build);
    }

    public DomainModelDescriptor get(Class<T> entityClass) {
        return this.domainModels.get(entityClass);
    }


    public List<DomainModelDescriptor> getDomainModelDescriptor() {
        return new ArrayList<>(this.domainModels.values());
    }


    private DomainModelDescriptor build(Class<T> entityClass) {

        EntityDescriptor entityDescriptor = this.entityParser.parse(entityClass);
        List<CommandDescriptor> commandDescriptorList = this.commandParser.parser(entityClass,
                null);
        List<RuleDescriptor> ruleDescriptorList = this.ruleParser.parse(entityClass,
                null);
        List<EventDescriptor> eventDescriptors = this.eventParser.parse(entityClass,
                null);
        List<DomainServiceDescriptor> domainServiceDescriptors = this.domainServiceParser.parse(entityClass,
                null);

        return new DomainModelDescriptor(entityDescriptor,
                ruleDescriptorList,
                eventDescriptors,
                domainServiceDescriptors,
                commandDescriptorList);
    }

}
