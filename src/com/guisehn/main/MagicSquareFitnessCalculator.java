package com.guisehn.main;

import java.util.Arrays;
import java.util.stream.IntStream;

public class MagicSquareFitnessCalculator {
    
    private final int size;
    private final int magicValue;
    private final int[] mainDiagonalIndexes;
    private final int[] secondaryDiagonalIndexes;
    
    public MagicSquareFitnessCalculator(int size) {
        this.size = size;
        this.magicValue = (size + (int)Math.pow(size, 3)) / 2;
        
        DiagonalIndexesFinder dif = new DiagonalIndexesFinder(size);
        this.mainDiagonalIndexes = dif.findMainDiagonalIndexes();
        this.secondaryDiagonalIndexes = dif.findSecondaryDiagonalIndexes();
    }
    
    public int getMagicValue() {
        return magicValue;
    }
    
    public int calculateFitness(int[] square, boolean checkUniqueNumbers) {
        if (checkUniqueNumbers && !checkUniqueNumbers(square)) {
            return -1;
        }
        
        return calculateMainDiagonalFitness(square)
            + calculateSecondaryDiagonalFitness(square)
            + calculateLinesFitnessSum(square)
            + calculateColumnsFitnessSum(square);
    }
    
    public boolean checkUniqueNumbers(int[] square) {
        return Arrays.stream(square).distinct().count() == square.length;
    }
    
    public int calculateMainDiagonalFitness(int[] square) {
        int sum = sumValuesForIndexes(square, mainDiagonalIndexes);
        return Math.abs(magicValue - sum);
    }
    
    public int calculateSecondaryDiagonalFitness(int[] square) {
        int sum = sumValuesForIndexes(square, secondaryDiagonalIndexes);
        return Math.abs(magicValue - sum);
    }
    
    public int calculateLinesFitnessSum(int[] square) {
        int sum = 0;
        
        for (int i = 0; i < square.length; i += size) {
            int[] indexes = IntStream.range(i, i + size).toArray();
            int lineSum = sumValuesForIndexes(square, indexes);
            int difference = Math.abs(magicValue - lineSum);
            
            sum += difference;
        }
        
        return sum;
    }
    
    public int calculateColumnsFitnessSum(int[] square) {
        int sum = 0;
        
        for (int i = 0; i < size; i++) {
            final int j = i; // Java won't let me use the variable i inside
                             // the `map` function over the stream ಠ_ಠ

            int[] indexes = IntStream.range(0, size)
                .map(n -> j + (n * size))
                .toArray();
            
            int columnSum = sumValuesForIndexes(square, indexes);
            int difference = Math.abs(magicValue - columnSum);
            
            sum += difference;
        }
        
        return sum;
    }
    
    private int sumValuesForIndexes(int[] square, int[] indexes) {   
        return Arrays.stream(indexes)
            .map(i -> square[i])
            .reduce((a, b) -> a + b)
            .getAsInt();
    }
    
}
