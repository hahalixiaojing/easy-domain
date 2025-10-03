package cn.easylib.domain.visual.entity;

import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.base.IEnumValue;

import java.util.List;

public interface IEnumValueFinder {
    <T extends EntityBase<?>> List<Class<?>> findEnums();
}
