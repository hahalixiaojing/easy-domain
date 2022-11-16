package cn.easylib.domain.visual.service;

import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.base.IDomainService;

import java.util.List;

public interface IDomainServiceFinder<T extends EntityBase<?>> {
    List<IDomainService> findList(Class<T> cls, String packageName);
}
