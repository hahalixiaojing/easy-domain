package cn.easylib.domain.visual.application;

import cn.easylib.domain.base.EntityBase;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationServiceParser {


    private final Map<Class<?>, IApplicationServiceFinder> applicationServiceFinderMap = new HashMap<>();

    public <T extends EntityBase<?>> void registerApplicationService(Class<T> entityClass,
                                                                     IApplicationServiceFinder finder) {
        applicationServiceFinderMap.put(entityClass, finder);
    }

    public <T extends EntityBase<?>> List<ApplicationDescriptor> parser(Class<T> cls) {

        List<Class<?>> list = this.applicationServiceFinderMap.get(cls).findList(cls);

        return list.stream()
                .map(s -> Arrays.stream(s.getMethods())
                        .filter(m -> Modifier.isPublic(m.getModifiers()))
                        .map(m -> {

                            CommandServiceVisual commandServiceDescriptor =
                                    m.getAnnotation(CommandServiceVisual.class);

                            ReadServiceVisual readServiceDescriptor =
                                    m.getAnnotation(ReadServiceVisual.class);

                            if (commandServiceDescriptor == null && readServiceDescriptor == null) {
                                return null;
                            }

                            String name = Optional.ofNullable(commandServiceDescriptor)
                                    .map(CommandServiceVisual::name)
                                    .orElse(Optional.ofNullable(readServiceDescriptor)
                                            .map(ReadServiceVisual::name)
                                            .orElse("")
                                    );
                            String description = Optional.ofNullable(commandServiceDescriptor)
                                    .map(CommandServiceVisual::description)
                                    .orElse(Optional.ofNullable(readServiceDescriptor)
                                            .map(ReadServiceVisual::description)
                                            .orElse("")
                                    );
                            String type = Optional.ofNullable(commandServiceDescriptor).map(t -> {
                                return "Command";
                            }).orElse("Query");


                            return new ApplicationDescriptor(
                                    name,
                                    description,
                                    m.getDeclaringClass().getSimpleName(),
                                    m.getName(),
                                    type
                            );
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))

                .flatMap(List::stream)
                .collect(Collectors.toList());

    }
}
