package io.architecture.scarlet.internal;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

@Documented
@java.lang.annotation.Target(METHOD)
@Retention(RUNTIME)
public @interface Target {
    String type() default "";

}
