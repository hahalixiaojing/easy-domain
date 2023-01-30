package cn.easylib.domain.visual;

import cn.easylib.domain.application.subscriber.IDomainEventManager;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.visual.application.ApplicationServiceParser;
import cn.easylib.domain.visual.application.IApplicationServiceFinder;
import cn.easylib.domain.visual.entity.EntityParser;
import cn.easylib.domain.visual.event.EventParser;
import cn.easylib.domain.visual.event.IEventFinder;
import cn.easylib.domain.visual.rule.IRuleFinder;
import cn.easylib.domain.visual.rule.RuleParser;
import cn.easylib.domain.visual.service.DomainServiceParser;
import cn.easylib.domain.visual.service.IDomainServiceFinder;


public class DomainModelVisualManager<T extends EntityBase<?>> {


    private final ApplicationServiceParser applicationServiceParser;
    private final EntityParser entityParser;
    private final EventParser eventParser;
    private final RuleParser ruleParser;
    private final DomainServiceParser domainServiceParser;


    public DomainModelVisualManager(IDomainEventManager iDomainEventManager) {
        this.applicationServiceParser = new ApplicationServiceParser();
        this.entityParser = new EntityParser();
        this.eventParser = new EventParser(iDomainEventManager);
        this.ruleParser = new RuleParser();
        this.domainServiceParser = new DomainServiceParser();
    }

    public void registerDomainEntity(Class<T> entityClass) {
        this.entityParser.registerEntity(entityClass);
    }

    public void registerApplicationService(Class<T> entityClass, IApplicationServiceFinder finder) {
        this.applicationServiceParser.registerApplicationService(entityClass, finder);
    }

    public void registerDomainEvent(Class<T> entityClass, IEventFinder finder) {
        this.eventParser.registerDomainEvent(entityClass, finder);
    }

    public void registerDomainRule(Class<T> entityClass, IRuleFinder finder) {
        this.ruleParser.registerDomainRule(entityClass, finder);
    }

    public void registerDomainService(Class<T> entityClass, IDomainServiceFinder finder) {
        this.domainServiceParser.registerDomainService(entityClass, finder);
    }
}
