package com.andyadc.test;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@link Monitored @Monitored} annotation from Java Interceptor Specification
 */
@Inherited
@DataAccess
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface Monitored {
}
