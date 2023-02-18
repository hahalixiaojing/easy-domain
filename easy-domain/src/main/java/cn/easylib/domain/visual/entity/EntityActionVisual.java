package cn.easylib.domain.visual.entity;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EntityActionVisual {
    Class<?>[] triggerEvents() default {};
}
