package cn.easylib.domain.visual.entity;

import cn.easylib.domain.base.ConcurrentEntityBase;
import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.base.IEntity;
import cn.easylib.domain.visual.VisualException;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class EntityParser {

    private final Map<Class<?>, IEntityFieldFinder> entityCls = new HashMap<>();
    private final Map<Class<?>, String> entityClsDesc = new HashMap<>();
    private final Map<Class<?>, String> entityClsName = new HashMap<>();


    public <T extends EntityBase<?>> void registerEntity(Class<T> entityClass, IEntityFieldFinder finder,
                                                         String entityName, String description) {

        if (entityCls.containsKey(entityClass)) {
            throw new VisualException("repeat register");
        }
        entityCls.put(entityClass, finder);
        entityClsDesc.put(entityClass, description);
        entityClsName.put(entityClass, entityName);
    }


    public <T extends EntityBase<?>> List<EntityDescriptor> parse(Class<T> cls) {
        return this.privateParse(cls);
    }

    private FieldInfo getFieldName(Pair<FieldGetter<?, ?>, String> p) {
        try {
            return p.getKey().getFieldName(p.getRight());
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new VisualException(e);
        }
    }

    public List<List<EntityDescriptor>> all() {
        return this.entityCls.keySet().stream().map(this::privateParse).collect(Collectors.toList());
    }

    private List<EntityDescriptor> privateParse(Class<?> cls) {

        if (entityCls.containsKey(cls)) {

            return entityCls.get(cls).fieldGetterList()
                    .stream()
                    .map(this::getFieldName)
                    .collect(Collectors.groupingBy(FieldInfo::getClsType)).entrySet().stream().map(t -> {

                        String simpleCls = t.getKey().getSimpleName();
                        String description = entityClsDesc.get(cls);
                        String name = entityClsName.get(cls);
                        boolean isRoot = TypeUtils.isAssignable(t.getKey(),IEntity.class);

                        List<FieldInfo> value = t.getValue();

                        return new EntityDescriptor(name, simpleCls, description, value, isRoot);
                    }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


}
