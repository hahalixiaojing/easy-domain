package cn.easylib.domain.visual.service;

import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.base.IDomainService;
import cn.easylib.domain.base.IDomainServiceDescriptor;

import java.util.*;
import java.util.stream.Collectors;

public class DomainServiceParser {

    private final Map<Class<?>, IDomainServiceFinder> domainServiceMap = new HashMap<>();


    public <T extends EntityBase<?>> void registerDomainService(Class<T> entityClass, IDomainServiceFinder finder) {
        this.domainServiceMap.put(entityClass, finder);
    }

    public <T extends EntityBase<?>> List<DomainServiceDescriptor> parse(Class<T> cls) {

        List<Class<?>> domainServiceClsList = this.domainServiceMap.get(cls).findList(cls);
        return domainServiceClsList.stream().map(s -> {

            if (s.isAssignableFrom(IDomainService.class)) {

                String description = Optional.ofNullable(s.getAnnotation(IDomainServiceDescriptor.class))
                        .map(IDomainServiceDescriptor::description)
                        .orElse("");

                return new DomainServiceDescriptor(s.getSimpleName(),description);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

    }
}
