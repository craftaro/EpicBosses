package net.aminecraftdev.custombosses.innerapi.command.builder.attributes;

import java.lang.annotation.*;

/**
 * Created by LukeBingham on 03/04/2017.
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {

    String value();

}
