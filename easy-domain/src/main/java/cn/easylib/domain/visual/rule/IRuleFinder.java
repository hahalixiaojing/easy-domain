package cn.easylib.domain.visual.rule;

import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.rules.EntityRule;

import java.util.List;

public interface IRuleFinder<T extends EntityBase<?>> {

    List<EntityRule<T>> findList(Class<T> cls, String packageName);

}
