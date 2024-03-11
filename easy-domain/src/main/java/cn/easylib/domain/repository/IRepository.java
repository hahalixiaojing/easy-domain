package cn.easylib.domain.repository;

import cn.easylib.domain.base.EntityBase;

public interface IRepository<ID, T extends EntityBase<ID>> {

    void insert(T entityBase);

    void update(T entityBase);

    T findByID(ID id);

    void delete(T entityBase);
}
