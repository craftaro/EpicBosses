package com.songoda.epicbosses.utils.base;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public interface BaseAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {

}
