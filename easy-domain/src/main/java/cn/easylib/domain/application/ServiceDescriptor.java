package cn.easylib.domain.application;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceDescriptor {

    String name() default "";
    String description() default "";
}
