package nl.rdb.springbootplayground.architecture;

import org.springframework.core.annotation.AliasFor;

/**
 * Annotation to mark that we wish to deviate from the rules
 * defined by ArchUnit. Providing a reason is mandatory.
 */
public @interface ArchIgnore {

    /**
     * The key of the violation to ignore.
     *
     * @return String
     */
    @AliasFor("violation")
    ViolationType[] value() default ViolationType.NONE;

    /**
     * The key of the violation to ignore.
     *
     * @return String
     */
    @AliasFor("value")
    ViolationType[] violation() default ViolationType.NONE;

    /**
     * The reason the violation should be ignored.
     *
     * @return String
     */
    String reason() default "";
}
