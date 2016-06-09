package com.guisehn.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomMagicSquareGenerator {

    private final int length;
    
    public RandomMagicSquareGenerator(int size) {
        this.length = (int)Math.pow(size, 2);
    }
    
    public int[] generate() {
        List<Integer> list = new ArrayList<>();
        
        for (int i = 0; i < length; i++) {
            list.add(i + 1);
        }
        
        Collections.shuffle(list);
        
        return list.stream().mapToInt(i -> i).toArray();
    }
    
}
