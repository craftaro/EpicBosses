package net.aminecraftdev.custombosses.innerapi.factory;

/**
 * Created by charl on 04-May-17.
 */
public abstract class Factory<T> implements FactoryBuilder<T> {

    protected T object;

    public abstract T build();
}
