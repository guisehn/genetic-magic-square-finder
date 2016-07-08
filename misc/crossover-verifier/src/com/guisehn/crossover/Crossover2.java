package com.guisehn.crossover;

import java.util.Arrays;
import java.util.Random;

// http://www.ceng.metu.edu.tr/~ucoluk/research/publications/tspnew.pdf
public class Crossover2 implements CrossoverOperator {
    
    private final Random random = new Random();
    
    @Override
    public CrossoverResult crossover(int[] square1, int[] square2,
            int minimumCrossoverPoint, int maximumCrossoverPoint) {
        
        System.out.println("Mounting inversion sequence for first parent");
        System.out.println("=======================================");
        int[] inv1 = buildInversionSequenceFromSquare(square1);
        System.out.println();
        
        System.out.println("Mounting inversion sequence for second parent");
        System.out.println("=======================================");
        int[] inv2 = buildInversionSequenceFromSquare(square2);
        
        int crossoverPoint = generateCrossoverPoint(minimumCrossoverPoint, maximumCrossoverPoint);
        
        System.out.println("=======================================");
        System.out.println("inv of 1st parent = " + getRepresentation(inv1));
        System.out.println("inv of 2nd parent = " + getRepresentation(inv2));
        System.out.println("Crossover point = " + crossoverPoint);
        System.out.println("=======================================");
        System.out.println();
        
        System.out.println("First child crossover");
        System.out.println("=======================================");
        int[] invCross1 = crossInversionSequences(inv1, inv2, crossoverPoint);
        System.out.println("inv of 1st child = " + getRepresentation(invCross1));
        System.out.println();
        
        System.out.println("Transforming first child to permutation representation");
        System.out.println("=======================================");
        int[] child1 = buildSquareFromInversionSequence(invCross1);
        System.out.println();

        System.out.println("Second child crossover");
        System.out.println("=======================================");
        int[] invCross2 = crossInversionSequences(inv2, inv1, crossoverPoint);
        System.out.println("inv of 2nd child = " + getRepresentation(invCross2));
        System.out.println();
        
        System.out.println("Transforming second child to permutation representation");
        System.out.println("=======================================");
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
        
        Arrays.fill(inversion, -1);
        System.out.println("n = " + getRepresentation(square));
        System.out.println("inv = " + getRepresentation(inversion));
        System.out.println();
        
        for (int i = 0, n = 1; i < N; i++, n++) {
            System.out.println("Iteration #" + n);
            
            inversion[i] = 0;
            
            for (int j = 0; square[j] != n; j++) {
                if (square[j] > n) inversion[i]++;
            }
            
            System.out.println("To the left of " + n + " there are " + inversion[i]
                + " numbers greather than it.");
            System.out.println("inv = " + getRepresentation(inversion));
            System.out.println();
        }
        
        return inversion;
    }

    private int[] buildSquareFromInversionSequence(int[] inv) {
        int N = inv.length;
        int[] pos = new int[N];
        
        Arrays.fill(pos, -1);
        System.out.println("Initialize pos = " + getRepresentation(pos));
        System.out.println();
        
        for (int i = N - 1; i >= 0; i--) {
            System.out.println("Iteration i=" + i);
            
            pos[i] = inv[i];
            
            System.out.println("inv = " + getRepresentation(inv));
            System.out.println("pos = " + getRepresentation(pos));
            
            boolean changed = false;
            
            for (int m = i + 1; m < N; m++) {
                if (pos[m] >= inv[i]) {
                    pos[m]++;
                    changed = true;
                }
            }
            
            if (changed) {
                System.out.println("pos = " + getRepresentation(pos));
            }
            
            System.out.println();
        }
        
        System.out.println("Mounting final representation:");
        
        int[] square = new int[N];
        for (int i = 0; i < N; i++) {
            System.out.println("Iteration #" + (i + 1) + ": pos[" + i + "] is "
                + pos[i] + ", thus square[" + pos[i] + "] is " + (i + 1));

            square[pos[i]] = i + 1;
        }
        
        System.out.println();
        System.out.println("square = " + getRepresentation(square));
        
        return square;
    }
    
    private String getRepresentation(int[] x) {
        return Arrays.toString(x).replaceAll("-1", "_");
    }
    
}
