package net.aminecraftdev.custombosses.utils;

import org.junit.Test;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public class StringTest {

    @Test
    public void testReplaceList() {
        List<String> list = new ArrayList<>(Arrays.asList("abcd {0}", "efg {1}", "hij {2}"));
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{0}", "123");
        replaceMap.put("{1}", "456");
        replaceMap.put("{2}", "789");

        System.out.println(list);
        list.replaceAll(s -> replaceString(s, replaceMap));
        System.out.println(list);
    }

    private String replaceString(String input, Map<String, String> replaceMap) {
        if(replaceMap == null) return input;

        for(String replaceKey : replaceMap.keySet()) {
            if(input.contains(replaceKey)) {
                input = input.replace(replaceKey, replaceMap.get(replaceKey));
            }
        }

        return input;
    }


}
