package com.songoda.epicbosses.utils;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public interface IReplaceableConverter<OutputObject, InputObject> extends IConverter<OutputObject, InputObject> {

    InputObject from(OutputObject outputObject, Map<String, String> replaceMap);

}
