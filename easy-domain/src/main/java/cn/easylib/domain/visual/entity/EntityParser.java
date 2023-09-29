package cn.easylib.domain.visual.entity;

import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.base.IEntity;
import cn.easylib.domain.visual.VisualException;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.tuple.MutablePair;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class EntityParser {

    private final Map<Class<?>, IEntityFieldFinder> entityCls = new HashMap<>();


    public <T extends EntityBase<?>> void registerEntity(Class<T> entityClass, IEntityFieldFinder finder) {

        if (entityCls.containsKey(entityClass)) {
            throw new VisualException("repeat register");
        }
        entityCls.put(entityClass, finder);
    }


    public <T extends EntityBase<?>> List<EntityDescriptor> parse(Class<T> cls) {
        return this.privateParse(cls);
    }

    private FieldInfo getFieldName(IEntityFieldFinder.EntityFieldInfo p) {
        try {
            return p.fieldGetter.getFieldName(p.name,p.collection,p.collectionType);
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new VisualException(e);
        }
    }

    public List<List<EntityDescriptor>> all() {
        return this.entityCls.keySet().stream().map(this::privateParse).collect(Collectors.toList());
    }

    private List<EntityDescriptor> privateParse(Class<?> cls) {

        if (!entityCls.containsKey(cls)) {
            return Collections.emptyList();
        }

        return entityCls.get(cls).fieldGetterList()
                .stream()
                .map(this::getFieldName)
                .collect(Collectors.groupingBy(FieldInfo::getClsType))
                .entrySet()
                .stream()
                .map(t -> new MutablePair<Class<?>, List<FieldInfo>>(t.getKey(), t.getValue()))
                .map(this::buildEntityDescriptor).collect(Collectors.toList());
    }


    private EntityDescriptor buildEntityDescriptor(Map.Entry<Class<?>, List<FieldInfo>> t) {
        EntityVisual annotation = t.getKey().getAnnotation(EntityVisual.class);

        String description = Optional.ofNullable(annotation)
                .map(EntityVisual::description).orElse("");

        String simpleCls = t.getKey().getSimpleName();
        boolean isRoot = TypeUtils.isAssignable(t.getKey(), IEntity.class);

        List<FieldInfo> value = t.getValue();

        List<EntityActionDescriptor> methodEntityActionDescriptorList = Collections.emptyList();
        List<EntityActionDescriptor> constructorActionDescriptorList = Collections.emptyList();
        if (isRoot) {
            constructorActionDescriptorList = this.buildConstructorAction(
                    t.getKey().getDeclaredConstructors());
            methodEntityActionDescriptorList = this.buildMethodAction(
                    t.getKey().getDeclaredMethods());

        }

        ArrayList<EntityActionDescriptor> entityActionDescriptors = new ArrayList<>();
        entityActionDescriptors.addAll(constructorActionDescriptorList);
        entityActionDescriptors.addAll(methodEntityActionDescriptorList);


        return new EntityDescriptor(simpleCls,
                description,
                value,
                entityActionDescriptors,
                isRoot);
    }

    private List<EntityActionDescriptor> buildConstructorAction(Constructor<?>[] constructors) {

        return Arrays.stream(constructors)
                .filter(s -> s.getAnnotation(EntityActionVisual.class) != null)
                .map(m -> {

                            EntityActionVisual ann = m.getAnnotation(EntityActionVisual.class);
                            List<String> triggerEvents = Arrays.stream(ann.triggerEvents())
                                    .map(Class::getSimpleName)
                                    .collect(Collectors.toList());

                            return new EntityActionDescriptor("Constructor",
                                    "构造",
                                    triggerEvents);
                        }

                ).collect(Collectors.toList());

    }

    private List<EntityActionDescriptor> buildMethodAction(Method[] methods) {
        return Arrays
                .stream(methods)
                .filter(s -> s.getAnnotation(EntityActionVisual.class) != null)
                .map(m -> {

                    String name = m.getName();
                    EntityActionVisual ann = m.getAnnotation(EntityActionVisual.class);
                    List<String> triggerEvents = Arrays.stream(ann.triggerEvents())
                            .map(Class::getSimpleName)
                            .collect(Collectors.toList());

                    return new EntityActionDescriptor(name, "", triggerEvents);

                }).collect(Collectors.toList());
    }


}
