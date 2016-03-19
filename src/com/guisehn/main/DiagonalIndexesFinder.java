package com.guisehn.main;

public class DiagonalIndexesFinder {
    
    private final int size;
    private final int length;
    
    public DiagonalIndexesFinder(int magicSquareSize) {
        size = magicSquareSize;
        length = (int)Math.pow(size, 2);
    }
    
    public int[] findMainDiagonalIndexes() {
        int[] indexes = new int[size];
        int step = size + 1;
        
        for (int a = 0, b = 0; b < length; a++, b += step) {
            indexes[a] = b;
        }
        
        return indexes;
    }
    
    public int[] findSecondaryDiagonalIndexes() {
        int[] indexes = new int[size];
        int step = size - 1;
        
        for (int a = 0, b = length - size; b > 0; a++, b -= step) {
            indexes[a] = b;
        }
        
        return indexes;
    }
    
}
