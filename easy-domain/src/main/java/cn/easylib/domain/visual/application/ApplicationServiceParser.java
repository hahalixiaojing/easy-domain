package cn.easylib.domain.visual.application;

import cn.easylib.domain.application.ICommandService;
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

                            boolean commandService = Arrays.stream(m.getDeclaringClass().getInterfaces()).filter(
                                    i -> i == ICommandService.class
                            ).count() == 1;

                            ServiceDescriptor annotation = m.getAnnotation(ServiceDescriptor.class);
                            if (annotation == null) {
                                return null;
                            }
                            return new ApplicationDescriptor(
                                    annotation.name(),
                                    annotation.description(),
                                    m.getDeclaringClass().getSimpleName(),
                                    m.getName(),
                                    commandService
                            );
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))

                .flatMap(List::stream)
                .collect(Collectors.toList());

    }
}
