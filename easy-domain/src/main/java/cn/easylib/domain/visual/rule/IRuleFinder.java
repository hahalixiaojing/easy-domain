package cn.easylib.domain.visual.rule;

import cn.easylib.domain.base.EntityBase;

import java.util.List;

public interface IRuleFinder {

    <T extends EntityBase<?>> List<Class<?>> findList(Class<T> cls);

}
