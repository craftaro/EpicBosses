package net.aminecraftdev.custombosses.innerapi.command.builder.attributes;

import java.lang.annotation.*;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Usage {

    String value();

}
