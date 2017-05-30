package net.aminecraftdev.custombosses.innerapi.command.builder.attributes;

import java.lang.annotation.*;

/**
 * Created by charl on 03-May-17.
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Alias {

    String[] value();

}
