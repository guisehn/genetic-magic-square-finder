package com.guisehn.main;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiagonalIndexesFinderTest {
    
    public DiagonalIndexesFinderTest() { }

    private void assertArrayEqualsWithoutOrder(int[] arr1, int[] arr2) {
        arr1 = arr1.clone(); arr2 = arr2.clone();
        Arrays.sort(arr1); Arrays.sort(arr2);
        assertArrayEquals(arr1, arr2);
    }
    
    @Test
    public void testFindMainDiagonalIndexes() {
        int[] values;
        
        values = new DiagonalIndexesFinder(1).findMainDiagonalIndexes();
        assertArrayEqualsWithoutOrder(new int[] { 0 }, values);
        
        values = new DiagonalIndexesFinder(2).findMainDiagonalIndexes();
        assertArrayEqualsWithoutOrder(new int[] { 0, 3 }, values);
        
        values = new DiagonalIndexesFinder(3).findMainDiagonalIndexes();
        assertArrayEqualsWithoutOrder(new int[] { 0, 4, 8 }, values);
        
        values = new DiagonalIndexesFinder(4).findMainDiagonalIndexes();
        assertArrayEqualsWithoutOrder(new int[] { 0, 5, 10, 15 }, values);
        
        values = new DiagonalIndexesFinder(5).findMainDiagonalIndexes();
        assertArrayEqualsWithoutOrder(new int[] { 0, 6, 12, 18, 24 }, values);
    }

    @Test
    public void testFindSecondaryDiagonalIndexes() {
        int[] values;
        
        values = new DiagonalIndexesFinder(1).findSecondaryDiagonalIndexes();
        assertArrayEqualsWithoutOrder(new int[] { 0 }, values);
        
        values = new DiagonalIndexesFinder(2).findSecondaryDiagonalIndexes();
        assertArrayEqualsWithoutOrder(new int[] { 1, 2 }, values);
        
        values = new DiagonalIndexesFinder(3).findSecondaryDiagonalIndexes();
        assertArrayEqualsWithoutOrder(new int[] { 2, 4, 6 }, values);
        
        values = new DiagonalIndexesFinder(4).findSecondaryDiagonalIndexes();
        assertArrayEqualsWithoutOrder(new int[] { 3, 6, 9, 12 }, values);
        
        values = new DiagonalIndexesFinder(5).findSecondaryDiagonalIndexes();
        assertArrayEqualsWithoutOrder(new int[] { 4, 8, 12, 16, 20 }, values);
    }
    
}
