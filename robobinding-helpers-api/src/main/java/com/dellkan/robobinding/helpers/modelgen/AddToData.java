package com.dellkan.robobinding.helpers.modelgen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface AddToData {
    String alternativeName() default "";
    String alternativeDataFormatter() default "";

    /**
     * See getData(group)
     * @return
     */
    String[] group() default "";

    /**
     * Nested structure this field should be put into. Use dots ' . ' to denote nested structures.
     * <br>
     * Example: parent.child
     * <br><br>
     * Gives the following json:
     * <pre>
     * {@code
     *
     * "parent": {
     *     "child": {
     *         "yourExampleField": <value>
     *     }
     * }
     * }
     * </pre>
     * @return
     */
    String path() default "";
    String onlyIf() default "";
}
