package com.andyadc.scaffold.showcase.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注解类或属性的元数据，这些元数据可用于代码生成或运行时动态内容生成
 *
 * @author andaicheng
 * @version 2017/3/11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PACKAGE})
public @interface MetaData {

    /**
     * 简要注解说明
     */
    String desc();

    /**
     * 注释说明：用于描述代码内部用法说明
     */
    String comments() default "";

    /**
     * 类描述: 可以说明实体对应的数据库表名
     */
    String tableName() default "";
}
