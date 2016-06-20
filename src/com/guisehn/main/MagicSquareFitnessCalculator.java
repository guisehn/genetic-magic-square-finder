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
        
        int linesDiff = calculateLinesFitnessSum(square, true);
        int columnsDiff = calculateColumnsFitnessSum(square, true);
        int mainDiagonalDiff = calculateMainDiagonalFitness(square, true);
        int secondaryDiagonalDiff = calculateSecondaryDiagonalFitness(square, true);
        
        int linesSum = calculateLinesFitnessSum(square, false);
        int columnsSum = calculateColumnsFitnessSum(square, false);
        int mainDiagonalSum = calculateMainDiagonalFitness(square, false);
        int secondaryDiagonalSum = calculateSecondaryDiagonalFitness(square, false);
        int extra = Math.abs((linesSum + columnsSum + mainDiagonalSum + secondaryDiagonalSum) - (magicValue * ((size * 2) + 2)));
        
        return linesDiff + columnsDiff + mainDiagonalDiff + secondaryDiagonalDiff + extra;
    }
    
    public boolean checkUniqueNumbers(int[] square) {
        return Arrays.stream(square).distinct().count() == square.length;
    }
    
    public int calculateMainDiagonalFitness(int[] square, boolean diff) {
        int sum = sumValuesForIndexes(square, mainDiagonalIndexes);
        return diff ? Math.abs(magicValue - sum) : sum;
    }
    
    public int calculateSecondaryDiagonalFitness(int[] square, boolean diff) {
        int sum = sumValuesForIndexes(square, secondaryDiagonalIndexes);
        return diff ? Math.abs(magicValue - sum) : sum;
    }
    
    public int calculateLinesFitnessSum(int[] square, boolean diff) {
        int sum = 0;
        
        for (int i = 0; i < square.length; i += size) {
            int[] indexes = IntStream.range(i, i + size).toArray();
            int lineSum = sumValuesForIndexes(square, indexes);
            
            if (diff) {
                int difference = Math.abs(magicValue - lineSum);
                sum += difference;
            } else {
                sum += lineSum;
            }
        }
        
        return sum;
    }
    
    public int calculateColumnsFitnessSum(int[] square, boolean diff) {
        int sum = 0;
        
        for (int i = 0; i < size; i++) {
            final int j = i; // Java won't let me use the variable i inside
                             // the `map` function over the stream ಠ_ಠ

            int[] indexes = IntStream.range(0, size)
                .map(n -> j + (n * size))
                .toArray();
            
            int columnSum = sumValuesForIndexes(square, indexes);
            
            if (diff) {
                int difference = Math.abs(magicValue - columnSum);
                sum += difference;
            } else {
                sum += columnSum;
            }
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
