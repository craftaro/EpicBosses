package net.aminecraftdev.custombosses.utils.identifier;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 30-Mar-18
 */
public class Identifier<T> {

    private T identifier;

    public Identifier(T newIdentity) {
        this.identifier = newIdentity;
    }

    public T getIdentifier() {
        return this.identifier;
    }
}
