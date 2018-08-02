package net.aminecraftdev.custombosses.utils.command.attributes;

import java.lang.annotation.*;

/**
 * Created by charl on 03-May-17.
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

    String value();

}
