package cn.easylib.domain.base;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IDomainServiceDescriptor {
    String description() default "";
}
