package net.aminecraftdev.custombosses.utils;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public interface IConverter<OutputObject, InputObject> {

    OutputObject to(InputObject inputObject);

    InputObject from(OutputObject outputObject);

}
