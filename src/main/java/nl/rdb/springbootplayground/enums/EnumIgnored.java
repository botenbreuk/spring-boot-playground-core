/*
 * Copyright (c) 2020. 42 bv (www.42.nl). All rights reserved.
 */

package nl.rdb.springbootplayground.enums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation on an enum or one (or more) of its values to prevent it from being externally communicated through the
 * /enums endpoint.
 */
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumIgnored {
}
