package com.songoda.epicbosses.utils;

import com.songoda.epicbosses.utils.exceptions.NotImplementedException;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public interface IConverter<OutputObject, InputObject> {

    OutputObject to(InputObject inputObject) throws NotImplementedException;

    InputObject from(OutputObject outputObject) throws NotImplementedException;

}
