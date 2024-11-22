package com.huanli233.dex_mixin.api.annotations.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface Constant {

    /**
     * Causes this injector to match <tt>ACONST_NULL</tt> (null object) literals
     *
     * @return true to match <tt>null</tt>
     */
    public boolean nullValue() default false;

    /**
     * Specify an integer constant to match, includes byte and short values.
     *
     * @return integer value to match
     */
    public int intValue() default 0;

    /**
     * Specify a float constant to match
     *
     * @return float value to match
     */
    public float floatValue() default 0.0F;

    /**
     * Specify a long constant to match
     *
     * @return long value to match
     */
    public long longValue() default 0L;

    /**
     * Specify a double constant to match
     *
     * @return double value to match
     */
    public double doubleValue() default 0.0;

    /**
     * Specify a String constant to match
     *
     * @return string value to match
     */
    public String stringValue() default "";

    /**
     * Specify a type literal to match
     *
     * @return type literal to match
     */
    public Class<?> classValue() default Object.class;

    public int ordinal() default -1;

}
