package com.songoda.epicbosses.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Nov-18
 */
public class MapUtils {

    private static MapUtils INSTANCE = new MapUtils();

    public <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        Map<K, V> resultMap = new LinkedHashMap<>();

        list.sort(Map.Entry.comparingByValue());
        list.forEach(entry -> resultMap.put(entry.getKey(), entry.getValue()));

        return resultMap;
    }

    public static MapUtils get() {
        return INSTANCE;
    }

}
