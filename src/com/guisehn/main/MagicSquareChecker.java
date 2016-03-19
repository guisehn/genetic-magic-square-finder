package com.guisehn.main;

import java.util.Arrays;
import java.util.stream.IntStream;

public class MagicSquareChecker {
    
    private final int size;
    private final int magicValue;
    private final int[] mainDiagonalIndexes;
    private final int[] secondaryDiagonalIndexes;
    
    public MagicSquareChecker(int size) {
        this.size = size;
        this.magicValue = (size + (int)Math.pow(size, 3)) / 2;
        
        DiagonalIndexesFinder dif = new DiagonalIndexesFinder(size);
        this.mainDiagonalIndexes = dif.findMainDiagonalIndexes();
        this.secondaryDiagonalIndexes = dif.findSecondaryDiagonalIndexes();
    }
    
    public int getMagicValue() {
        return magicValue;
    }
    
    public boolean isMagic(Integer[] square, boolean checkUniqueNumbers) {
        return checkMainDiagonal(square) && checkSecondaryDiagonal(square)
            && checkLines(square) && checkColumns(square)
            && (checkUniqueNumbers ? checkUniqueNumbers(square) : true);
    }
    
    public boolean checkUniqueNumbers(Integer[] square) {
        return Arrays.stream(square).distinct().count() == square.length;
    }
    
    public boolean checkMainDiagonal(Integer[] square) {
        return sumValuesForIndexes(square, mainDiagonalIndexes) == magicValue;
    }
    
    public boolean checkSecondaryDiagonal(Integer[] square) {
        return sumValuesForIndexes(square, secondaryDiagonalIndexes) == magicValue;
    }
    
    public boolean checkLines(Integer[] square) {
        for (int i = 0; i < square.length; i += size) {
            int[] indexes = IntStream.range(i, i + size).toArray();
            
            if (sumValuesForIndexes(square, indexes) != magicValue) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean checkColumns(Integer[] square) {
        for (int i = 0; i < size; i++) {
            final int j = i; // Java won't let me use the variable i inside
                             // the `map` function over the stream ಠ_ಠ

            int[] indexes = IntStream.range(0, size)
                .map(n -> j + (n * size))
                .toArray();
            
            if (sumValuesForIndexes(square, indexes) != magicValue) {
                return false;
            }
        }
        
        return true;
    }
    
    private int sumValuesForIndexes(Integer[] square, int[] indexes) {   
        return Arrays.stream(indexes)
            .map(i -> square[i])
            .reduce((a, b) -> a + b)
            .getAsInt();
    }
    
}
