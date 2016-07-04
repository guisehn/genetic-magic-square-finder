package com.guisehn.crossover;

import java.util.Arrays;
import java.util.Random;

// http://www.ceng.metu.edu.tr/~ucoluk/research/publications/tspnew.pdf
public class Crossover2 implements CrossoverOperator {
    
    private final Random random = new Random();
    
    @Override
    public CrossoverResult crossover(int[] square1, int[] square2,
            int minimumCrossoverPoint, int maximumCrossoverPoint) {
        
        System.out.println("Montando sequência de inversão do pai 1");
        System.out.println("=======================================");
        int[] inv1 = buildInversionSequenceFromSquare(square1);
        System.out.println();
        
        System.out.println("Montando sequência de inversão do pai 2");
        System.out.println("=======================================");
        int[] inv2 = buildInversionSequenceFromSquare(square2);
        
        int crossoverPoint = generateCrossoverPoint(minimumCrossoverPoint, maximumCrossoverPoint);
        
        System.out.println("=======================================");
        System.out.println("Portanto:");
        System.out.println("inv pai 1 = " + getRepresentation(inv1));
        System.out.println("inv pai 2 = " + getRepresentation(inv2));
        System.out.println("Ponto de crossover = " + crossoverPoint);
        System.out.println("=======================================");
        System.out.println();
        
        System.out.println("Cruzando filho 1");
        System.out.println("=======================================");
        int[] invCross1 = crossInversionSequences(inv1, inv2, crossoverPoint);
        System.out.println("inv do filho 1 = " + getRepresentation(invCross1));
        System.out.println();
        
        System.out.println("Transformando filho 1 de volta");
        System.out.println("=======================================");
        int[] child1 = buildSquareFromInversionSequence(invCross1);
        System.out.println();

        System.out.println("Cruzando filho 2");
        System.out.println("=======================================");
        int[] invCross2 = crossInversionSequences(inv2, inv1, crossoverPoint);
        System.out.println("inv do filho 2 = " + getRepresentation(invCross2));
        System.out.println();
        
        System.out.println("Transformando filho 2 de volta");
        System.out.println("=======================================");
        int[] child2 = buildSquareFromInversionSequence(invCross2);
        
        return new CrossoverResult(
            new int[][] { child1, child2 },
            "Ponto de cruzamento: " + crossoverPoint);
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
            System.out.println("Iteração " + n);
            
            inversion[i] = 0;
            
            for (int j = 0; square[j] != n; j++) {
                if (square[j] > n) inversion[i]++;
            }
            
            System.out.println("À esquerda de " + n + " há " + inversion[i]
                + " números maiores que ele.");
            System.out.println("inv = " + getRepresentation(inversion));
            System.out.println();
        }
        
        return inversion;
    }

    private int[] buildSquareFromInversionSequence(int[] inv) {
        int N = inv.length;
        int[] pos = new int[N];
        
        Arrays.fill(pos, -1);
        System.out.println("Inicializa pos = " + getRepresentation(pos));
        System.out.println();
        
        for (int i = N - 1; i >= 0; i--) {
            System.out.println("Iteração i=" + i);
            
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
        
        System.out.println("Montagem do quadrado final:");
        
        int[] square = new int[N];
        for (int i = 0; i < N; i++) {
            System.out.println("Iteração " + i + ": pos[" + i + "] é " + pos[i]
                + ", logo square[" + pos[i] + "] é " + (i+1));
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
