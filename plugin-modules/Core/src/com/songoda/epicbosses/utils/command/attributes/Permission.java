package com.songoda.epicbosses.utils.command.attributes;

import java.lang.annotation.*;

/**
 * Created by charl on 11-May-17.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    String value();

}
