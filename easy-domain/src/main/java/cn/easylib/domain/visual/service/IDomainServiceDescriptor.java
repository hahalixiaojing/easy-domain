package cn.easylib.domain.visual.service;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IDomainServiceDescriptor {
    String description() default "";
}
