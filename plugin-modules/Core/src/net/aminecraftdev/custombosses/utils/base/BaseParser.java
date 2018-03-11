package net.aminecraftdev.custombosses.utils.base;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public abstract class BaseParser<Input, Output> {

    protected Input input;

    public BaseParser(Input input) {
        this.input = input;
    }

    public abstract Output parse();

}
