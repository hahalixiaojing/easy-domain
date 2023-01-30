package cn.easylib.domain.visual.entity;

import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.base.IEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityParser {

    private final Set<Class<?>> entityCls = new HashSet<>();

    public <T extends EntityBase<?>> void registerEntity(Class<T> entityClass) {
        entityCls.add(entityClass);
    }

    public EntityDescriptor parse(Class<?> cls) {

        if (entityCls.contains(cls) && cls.isAssignableFrom(IEntity.class)) {
            return new EntityDescriptor(cls.getSimpleName(), cls.getSimpleName());
        }
        return null;
    }

    public List<EntityDescriptor> all(){
        return this.entityCls.stream().map(this::parse).collect(Collectors.toList());
    }

}
