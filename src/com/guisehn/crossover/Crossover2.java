package com.guisehn.crossover;

import java.util.Random;

// Crossover method described in
// http://www.ceng.metu.edu.tr/~ucoluk/research/publications/tspnew.pdf
public class Crossover2 implements CrossoverOperator {
    
    private final Random random = new Random();
    
    @Override
    public CrossoverResult crossover(int[] square1, int[] square2,
            int minimumCrossoverPoint, int maximumCrossoverPoint) {
        int[] inv1 = buildInversionSequenceFromSquare(square1);
        int[] inv2 = buildInversionSequenceFromSquare(square2);
        int crossoverPoint = generateCrossoverPoint(minimumCrossoverPoint, maximumCrossoverPoint);

        int[] invCross1 = crossInversionSequences(inv1, inv2, crossoverPoint);
        int[] invCross2 = crossInversionSequences(inv2, inv1, crossoverPoint);
        
        int[] child1 = buildSquareFromInversionSequence(invCross1);
        int[] child2 = buildSquareFromInversionSequence(invCross2);
        
        return new CrossoverResult(
            new int[][] { child1, child2 },
            "Crossover point: " + crossoverPoint);
    }
    
    private int generateCrossoverPoint(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
    
    private int[] crossInversionSequences(int[] inv1, int[] inv2,
            int crossoverPoint) {
        int N = inv1.length;
        int[] result = new int[N];
        
        for (int i = 0; i < N; i++) {
            if (i <= crossoverPoint) {
                result[i] = inv1[i];
            } else {
                result[i] = inv2[i];
            }
        }

        return result;
    }
    
    private int[] buildInversionSequenceFromSquare(int[] square) {
        int N = square.length;
        int[] inversion = new int[N];
        
        for (int i = 0, n = 1; i < N; i++, n++) {
            inversion[i] = 0;
            
            for (int j = 0; square[j] != n; j++) {
                if (square[j] > n) inversion[i]++;
            }
        }
        
        return inversion;
    }

    private int[] buildSquareFromInversionSequence(int[] inv) {
        int N = inv.length;
        int[] pos = new int[N];
        
        for (int i = N - 1; i >= 0; i--) {
            pos[i] = inv[i];

            for (int m = i + 1; m < N; m++) {
                if (pos[m] >= inv[i]) pos[m]++;
            }
        }
        
        int[] square = new int[N];
        for (int i = 0; i < N; i++) square[pos[i]] = i + 1;
        return square;
    }
    
}
