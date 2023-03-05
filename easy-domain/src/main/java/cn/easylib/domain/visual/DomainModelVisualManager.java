package cn.easylib.domain.visual;

import cn.easylib.domain.application.subscriber.IDomainEventManager;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.visual.application.ApplicationServiceParser;
import cn.easylib.domain.visual.application.IApplicationServiceFinder;
import cn.easylib.domain.visual.entity.EntityParser;
import cn.easylib.domain.visual.entity.IEntityFieldFinder;
import cn.easylib.domain.visual.event.EventParser;
import cn.easylib.domain.visual.event.IEventFinder;
import cn.easylib.domain.visual.rule.IRuleFinder;
import cn.easylib.domain.visual.rule.RuleParser;
import cn.easylib.domain.visual.service.DomainServiceParser;
import cn.easylib.domain.visual.service.IDomainServiceFinder;


public class DomainModelVisualManager {


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

    public <T extends EntityBase<?>> void registerDomainEntity(Class<T> entityClass,
                                                               IEntityFieldFinder finder) {
        this.entityParser.registerEntity(entityClass, finder);
    }

    public <T extends EntityBase<?>> void registerApplicationService(Class<T> entityClass,
                                                                     IApplicationServiceFinder finder) {
        this.applicationServiceParser.registerApplicationService(entityClass, finder);
    }

    public <T extends EntityBase<?>> void registerDomainEvent(Class<T> entityClass,
                                                              IEventFinder finder) {
        this.eventParser.registerDomainEvent(entityClass, finder);
    }

    public <T extends EntityBase<?>> void registerDomainRule(Class<T> entityClass,
                                                             IRuleFinder finder) {
        this.ruleParser.registerDomainRule(entityClass, finder);
    }

    public <T extends EntityBase<?>> void registerDomainService(Class<T> entityClass,
                                                                IDomainServiceFinder finder) {
        this.domainServiceParser.registerDomainService(entityClass, finder);
    }

    public <T extends EntityBase<?>> DomainModelVisualInfo build(Class<T> entityClass) {
        return new DomainModelVisualInfo(
                this.entityParser.parse(entityClass),
                this.ruleParser.parse(entityClass),
                this.eventParser.parse(entityClass),
                this.domainServiceParser.parse(entityClass),
                this.applicationServiceParser.parser(entityClass)
        );
    }
}
