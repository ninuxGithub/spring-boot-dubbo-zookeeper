package com.example.api.reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shenzm
 * @date 2019-5-10
 * @description 作用
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AutoField {

    String value() default "defaultValue";

    boolean unique() default true;
}
