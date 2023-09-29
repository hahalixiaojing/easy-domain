package cn.easylib.domain.visual.entity;


import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EntityVisual {

    String description() default "";

}
