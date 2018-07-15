package com.andyadc.codeblocks.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to indicate that a particular feature is still under heavy development and should
 * <i>not</i> be used in production.
 * <p>
 * Using a class marked with this annotation in production and having trouble is should not be considered
 * unexpected. Use these classes <i>AT YOUR OWN RISK</i>
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface Beta {
}
