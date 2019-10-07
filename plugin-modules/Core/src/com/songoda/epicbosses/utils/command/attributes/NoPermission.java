package com.songoda.epicbosses.utils.command.attributes;

import java.lang.annotation.*;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 08-Jun-17
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoPermission {

    String value();

}
