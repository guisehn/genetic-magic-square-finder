package com.guisehn.main;

import java.util.List;
import java.util.Random;

public class Utils {
    
    private final static Random random = new Random();
    
    public static int getRandom(int[] array) {
        int index = random.nextInt(array.length);
        return array[index];
    }
    
    public static <T> T getRandom(List<T> list) {
        int index = random.nextInt(list.size());
        return list.get(index);
    }
    
}
