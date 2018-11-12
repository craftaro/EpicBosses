package com.songoda.epicbosses.managers.interfaces;

import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.IMechanic;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Nov-18
 */
public interface IMechanicManager<T, J> extends ILoadable {

    void registerMechanic(IMechanic mechanic);

    boolean handleMechanicApplication(T t, J j);

}
