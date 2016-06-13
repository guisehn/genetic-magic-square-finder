package com.guisehn.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MagicSquareRepairer {
    
    private final int length;
    
    public MagicSquareRepairer(int size) {
        this.length = (int)Math.pow(size, 2);
    }
    
    public void repair(int[] square) {
        int[] missingNumbers = findMissingNumbers(square);
        int[] duplicateIndexes = findDuplicateIndexes(square);
        
        for (int i = 0; i < duplicateIndexes.length; i++) {
            int index = duplicateIndexes[i];
            square[index] = missingNumbers[i];
        }
    }
    
    private int[] findMissingNumbers(int[] square) {
        List<Integer> squareAsList = IntStream.of(square)
            .boxed()
            .collect(Collectors.toList());
        
        return IntStream.rangeClosed(1, length)
            .filter(n -> !squareAsList.contains(n))
            .toArray();
    }
    
    private int[] findDuplicateIndexes(int[] square) {
        Set<Integer> uniques = new HashSet<>();
        List<Integer> duplicateIndexes = new ArrayList<>();
        
        for (int i = 0; i < square.length; i++) {
            boolean added = uniques.add(square[i]);
            
            if (!added) {
                duplicateIndexes.add(i);
            }
        }
        
        return duplicateIndexes.stream().mapToInt(i -> i).toArray();
    }
    
}
