package cn.easylib.domain.rules;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EntityRuleDescriptor {
    String description() default "";
}
