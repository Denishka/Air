package org.example;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MapHelper {


    public static void addValue(Map<Integer, Set<Integer>> map, int hashCode, int value) {

        Set<Integer> integerSet = map.get(hashCode);
        if (Objects.isNull(integerSet)) {
            integerSet = new HashSet<>();
        }
        integerSet.add(value);

        map.put(hashCode, integerSet);
    }
}
