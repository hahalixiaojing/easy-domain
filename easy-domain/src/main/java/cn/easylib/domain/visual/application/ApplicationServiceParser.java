package cn.easylib.domain.visual.application;

import cn.easylib.domain.application.*;
import cn.easylib.domain.base.EntityBase;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationServiceParser {


    private final IApplicationServiceFinder iApplicationServiceFinder;

    public ApplicationServiceParser(IApplicationServiceFinder iApplicationServiceFinder) {
        this.iApplicationServiceFinder = iApplicationServiceFinder;
    }

    public <T extends EntityBase<?>> List<ApplicationDescriptor> parser(Class<T> cls, String packageName) {

        List<IApplication> list = this.iApplicationServiceFinder.findList(cls, packageName);

        return list.stream()
                .map(s -> Arrays.stream(s.getClass().getMethods())
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
                        .collect(Collectors.toList()))

                .flatMap(List::stream)
                .collect(Collectors.toList());

    }
}
