package com.songoda.epicbosses.managers.interfaces;

import com.songoda.epicbosses.utils.ILoadable;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Nov-18
 */
public interface IMechanicManager<T, J, M> extends ILoadable {

    void registerMechanic(M mechanic);

    void handleMechanicApplication(T t, J j);

}
