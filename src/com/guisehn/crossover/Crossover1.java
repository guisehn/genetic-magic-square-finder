package com.guisehn.crossover;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Crossover1 implements CrossoverOperator {

    private final Random random = new Random();
    
    @Override
    public CrossoverResult crossover(int[] square1, int[] square2,
        int minimumCrossoverPoint, int maximumCrossoverPoint) {
        int arraySize = square1.length;
        int maxIndex = arraySize - 1;
        int crossoverPoint = generateCrossoverPoint(minimumCrossoverPoint, maximumCrossoverPoint);
        int[] newSquare = new int[arraySize];
        Set<Integer> used = new HashSet<>();
        
        for (int i = 0; i < crossoverPoint; i++) {
            newSquare[i] = square1[i];
            used.add(newSquare[i]);
        }
        
        for (int i = 0, j = crossoverPoint; j < arraySize; i++) {
            int n = square2[i];

            if (!used.contains(n)) {
                newSquare[j++] = square2[i];
            }
        }
        
        return new CrossoverResult(new int[][] { newSquare },
            "Ponto de cruzamento: " + crossoverPoint);
    }
    
    private int generateCrossoverPoint(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
    
}
