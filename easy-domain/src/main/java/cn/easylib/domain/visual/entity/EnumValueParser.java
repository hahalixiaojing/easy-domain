package cn.easylib.domain.visual.entity;

import cn.easylib.domain.base.EntityBase;
import cn.easylib.domain.base.IEnumValue;
import cn.easylib.domain.visual.VisualException;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnumValueParser {
    private final Map<Class<?>, IEnumValueFinder> enumValueFinderMap = new HashMap<>();

    public <T extends EntityBase<?>> void registerEnum(Class<T> entityClass, IEnumValueFinder finder) {
        this.enumValueFinderMap.put(entityClass, finder);
    }

    public <T extends EntityBase<?>> List<EnumInfoDescriptor> parse(Class<T> cls) {

        IEnumValueFinder iEnumValueFinder = this.enumValueFinderMap.get(cls);

        List<Class<?>> enums = iEnumValueFinder.findEnums();

        return enums.stream().map(e -> {
            EnumInfoDescriptor descriptor = new EnumInfoDescriptor();
            descriptor.setName(e.getSimpleName());
            descriptor.setValueList(parseEnumValues(e));

            return descriptor;
        }).collect(Collectors.toList());
    }

    private List<EnumValue> parseEnumValues(Class<?> cls) {
        try {
            Object[] values = (Object[]) (cls.getMethod("values").invoke(null));
            return Arrays.stream(values).map(IEnumValue.class::cast).map(v -> {

                EnumValue enumValue = new EnumValue();
                enumValue.setValue(v.getValue().toString());
                enumValue.setDescription(v.getName());
                enumValue.setName(v.toString());
                return enumValue;
            }).collect(Collectors.toList());

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new VisualException(e);
        }
    }
}
