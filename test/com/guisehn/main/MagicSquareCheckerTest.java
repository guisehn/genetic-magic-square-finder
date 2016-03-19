package com.guisehn.main;

import org.junit.Test;
import static org.junit.Assert.*;

public class MagicSquareCheckerTest {

    @Test
    public void testGetMagicValue() {
        assertEquals(1, new MagicSquareChecker(1).getMagicValue());
        assertEquals(5, new MagicSquareChecker(2).getMagicValue());
        assertEquals(15, new MagicSquareChecker(3).getMagicValue());
        assertEquals(34, new MagicSquareChecker(4).getMagicValue());
        assertEquals(65, new MagicSquareChecker(5).getMagicValue());
    }
    
    @Test
    public void testCheckUniqueNumbers() {
        MagicSquareChecker checker;
        
        checker = new MagicSquareChecker(1);
        assertTrue(checker.checkUniqueNumbers(new Integer[] { 1 }));
        
        checker = new MagicSquareChecker(2);
        assertTrue(checker.checkUniqueNumbers(new Integer[] { 1, 2 }));
        assertFalse(checker.checkUniqueNumbers(new Integer[] { 1, 1 }));
        
        checker = new MagicSquareChecker(3);
        assertTrue(checker.checkUniqueNumbers(new Integer[] { 1, 2, 3, 4, 5, 6,
            7, 8, 9 }));
        assertFalse(checker.checkUniqueNumbers(new Integer[] { 1, 1, 3, 4, 5, 6,
            7, 8, 9 }));
        
        checker = new MagicSquareChecker(4);
        assertTrue(checker.checkUniqueNumbers(new Integer[] { 1, 2, 3, 4, 5, 6,
            7, 8, 9, 10, 11, 12, 13, 14, 15, 16 }));
        assertFalse(checker.checkUniqueNumbers(new Integer[] { 1, 2, 3, 4, 5, 6,
            7, 8, 9, 10, 11, 12, 13, 14, 15, 15 }));
        
        checker = new MagicSquareChecker(5);
        assertTrue(checker.checkUniqueNumbers(new Integer[] { 1, 2, 3, 4, 5, 6,
            7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
            24, 25 }));
        assertFalse(checker.checkUniqueNumbers(new Integer[] { 1, 2, 3, 4, 5, 6,
            7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 16, 18, 19, 20, 21, 22, 23,
            24, 25 }));
    }
    
    @Test
    public void testCheckMainDiagonalReturnsTrue() {
        Integer[] square;
        
        // 1x1
        square = new Integer[] { 1 };
        assertTrue(new MagicSquareChecker(1).checkMainDiagonal(square));

        // 2x2
        square = new Integer[] {
            2, 0,
            0, 3
        };
        assertTrue(new MagicSquareChecker(2).checkMainDiagonal(square));
        
        // 3x3
        square = new Integer[] {
            4, 0, 0,
            0, 5, 0,
            0, 0, 6
        };
        assertTrue(new MagicSquareChecker(3).checkMainDiagonal(square));

        // 4x4
        square = new Integer[] {
            7, 0, 0, 0 ,
            0, 8, 0, 0 ,
            0, 0, 9, 0 ,
            0, 0, 0, 10
        };
        assertTrue(new MagicSquareChecker(4).checkMainDiagonal(square));
        
        // 5x5
        square = new Integer[] {
            11, 0 , 0 , 0 , 0 ,
            0 , 12, 0 , 0 , 0 ,
            0 , 0 , 13, 0 , 0 ,
            0 , 0 , 0 , 14, 0 ,
            0 , 0 , 0 , 0 , 15
        };
        assertTrue(new MagicSquareChecker(5).checkMainDiagonal(square));
    }
    
    @Test
    public void testCheckMainDiagonalReturnsFalse() {
        Integer[] square;
        
        // 1x1
        square = new Integer[] { 2 };
        assertFalse(new MagicSquareChecker(1).checkMainDiagonal(square));

        // 2x2
        square = new Integer[] {
            2, 0,
            0, 4
        };
        assertFalse(new MagicSquareChecker(2).checkMainDiagonal(square));
        
        // 3x3
        square = new Integer[] {
            4, 0, 0,
            0, 5, 0,
            0, 0, 7
        };
        assertFalse(new MagicSquareChecker(3).checkMainDiagonal(square));

        // 4x4
        square = new Integer[] {
            7, 0, 0, 0 ,
            0, 8, 0, 0 ,
            0, 0, 9, 0 ,
            0, 0, 0, 11
        };
        assertFalse(new MagicSquareChecker(4).checkMainDiagonal(square));
        
        // 5x5
        square = new Integer[] {
            11, 0 , 0 , 0 , 0 ,
            0 , 12, 0 , 0 , 0 ,
            0 , 0 , 13, 0 , 0 ,
            0 , 0 , 0 , 14, 0 ,
            0 , 0 , 0 , 0 , 16
        };
        assertFalse(new MagicSquareChecker(5).checkMainDiagonal(square));
    }
    
    @Test
    public void testCheckSecondaryDiagonalReturnsTrue() {
        Integer[] square;
        
        // 1x1
        square = new Integer[] { 1 };
        assertTrue(new MagicSquareChecker(1).checkSecondaryDiagonal(square));

        // 2x2
        square = new Integer[] {
            0, 2,
            3, 0
        };
        assertTrue(new MagicSquareChecker(2).checkSecondaryDiagonal(square));
        
        // 3x3
        square = new Integer[] {
            0, 0, 4,
            0, 5, 0,
            6, 0, 0
        };
        assertTrue(new MagicSquareChecker(3).checkSecondaryDiagonal(square));

        // 4x4
        square = new Integer[] {
            0 , 0, 0, 7,
            0 , 0, 8, 0,
            0 , 9, 0, 0,
            10, 0, 0, 0
        };
        assertTrue(new MagicSquareChecker(4).checkSecondaryDiagonal(square));
        
        // 5x5
        square = new Integer[] {
            0 , 0 , 0 , 0 , 11,
            0 , 0 , 0 , 12, 0 ,
            0 , 0 , 13, 0 , 0 ,
            0 , 14, 0 , 0 , 0 ,
            15, 0 , 0 , 0 , 0
        };
        assertTrue(new MagicSquareChecker(5).checkSecondaryDiagonal(square));
    }
    
    @Test
    public void testCheckSecondaryDiagonalReturnsFalse() {
        Integer[] square;
        
        // 1x1
        square = new Integer[] { 2 };
        assertFalse(new MagicSquareChecker(1).checkSecondaryDiagonal(square));

        // 2x2
        square = new Integer[] {
            0, 2,
            4, 0
        };
        assertFalse(new MagicSquareChecker(2).checkSecondaryDiagonal(square));
        
        // 3x3
        square = new Integer[] {
            0, 0, 4,
            0, 5, 0,
            7, 0, 0
        };
        assertFalse(new MagicSquareChecker(3).checkSecondaryDiagonal(square));

        // 4x4
        square = new Integer[] {
            0 , 0, 0, 7,
            0 , 0, 8, 0,
            0 , 9, 0, 0,
            11, 0, 0, 0
        };
        assertFalse(new MagicSquareChecker(4).checkSecondaryDiagonal(square));
        
        // 5x5
        square = new Integer[] {
            0 , 0 , 0 , 0 , 11,
            0 , 0 , 0 , 12, 0 ,
            0 , 0 , 13, 0 , 0 ,
            0 , 14, 0 , 0 , 0 ,
            16, 0 , 0 , 0 , 0
        };
        assertFalse(new MagicSquareChecker(5).checkSecondaryDiagonal(square));
    }
    
    @Test
    public void testCheckColumnsReturnsTrue() {
        Integer[] square;
        
        square = new Integer[] {
            7 , 7 , 7 , 7 ,
            8 , 8 , 8 , 8 ,
            9 , 9 , 9 , 9 ,
            10, 10, 10, 10
        };
        assertTrue(new MagicSquareChecker(4).checkColumns(square));
    }
    
    @Test
    public void testCheckColumnsReturnsFalse() {
        Integer[] square;
        
        square = new Integer[] {
            7 , 7 , 7 , 7 ,
            8 , 8 , 8 , 8 ,
            9 , 9 , 9 , 9 ,
            11, 10, 10, 10
        };
        assertFalse(new MagicSquareChecker(4).checkColumns(square));
        
        square = new Integer[] {
            7 , 7 , 7 , 7 ,
            8 , 8 , 8 , 8 ,
            9 , 8 , 9 , 9 ,
            10, 10, 10, 10
        };
        assertFalse(new MagicSquareChecker(4).checkColumns(square));

        square = new Integer[] {
            7 , 7 , 7 , 7 ,
            8 , 8 , 7 , 8 ,
            9 , 9 , 9 , 9 ,
            10, 10, 10, 10
        };
        assertFalse(new MagicSquareChecker(4).checkColumns(square));

        square = new Integer[] {
            7 , 7 , 7 , 7 ,
            8 , 8 , 8 , 8 ,
            9 , 9 , 9 , 9 ,
            10, 10, 10, 9
        };
        assertFalse(new MagicSquareChecker(4).checkColumns(square));
    }
    
    @Test
    public void testCheckLinesReturnsTrue() {
        Integer[] square;
        
        square = new Integer[] {
            7, 8, 9, 10,
            7, 8, 9, 10,
            7, 8, 9, 10,
            7, 8, 9, 10
        };
        assertTrue(new MagicSquareChecker(4).checkLines(square));
    }
    
    @Test
    public void testCheckLinesReturnsFalse() {
        Integer[] square;
        
        square = new Integer[] {
            6, 8, 9, 10,
            7, 8, 9, 10,
            7, 8, 9, 10,
            7, 8, 9, 10
        };
        assertFalse(new MagicSquareChecker(4).checkLines(square));
        
        square = new Integer[] {
            7, 8, 9, 10,
            7, 7, 9, 10,
            7, 8, 9, 10,
            7, 8, 9, 10
        };
        assertFalse(new MagicSquareChecker(4).checkLines(square));

        square = new Integer[] {
            7, 8, 9, 10,
            7, 8, 9, 10,
            7, 8, 8, 10,
            7, 8, 9, 10
        };
        assertFalse(new MagicSquareChecker(4).checkLines(square));

         square = new Integer[] {
            7, 8, 9, 10,
            7, 8, 9, 10,
            7, 8, 9, 10,
            7, 8, 9, 9
        };
        assertFalse(new MagicSquareChecker(4).checkLines(square));
    }

    @Test
    public void testIsMagicReturnsTrue() {
        Integer[] square;
        
        // 1x1
        square = new Integer[] { 1 };
        assertTrue(new MagicSquareChecker(1).isMagic(square, false));
        
        // 3x3
        square = new Integer[] {
            2, 7, 6,
            9, 5, 1,
            4, 3, 8
        };
        assertTrue(new MagicSquareChecker(3).isMagic(square, false));

        // 4x4
        square = new Integer[] {
            1 , 12, 8 , 13,
            14, 7 , 11, 2 ,
            15, 6 , 10, 3 ,
            4 , 9 , 5 , 16
        };
        assertTrue(new MagicSquareChecker(4).isMagic(square, false));
        
        // 5x5
        square = new Integer[] {
            23, 6 , 19, 2 , 15,
            4 , 12, 25, 8 , 16,
            10, 18, 1 , 14, 22,
            11, 24, 7 , 20, 3 ,
            17, 5 , 13, 21, 9 
        };
        assertTrue(new MagicSquareChecker(5).isMagic(square, false));
    }
    
    @Test
    public void testIsMagicReturnsFalse() {
        Integer[] square;
        
        // 1x1
        square = new Integer[] { 2 };
        assertFalse(new MagicSquareChecker(1).isMagic(square, false));
        
        // 3x3
        square = new Integer[] {
            7, 2, 6,
            9, 5, 1,
            4, 3, 8
        };
        assertFalse(new MagicSquareChecker(3).isMagic(square, false));

        // 4x4
        square = new Integer[] {
            12, 1, 8 , 13,
            14, 7 , 11, 2 ,
            15, 6 , 10, 3 ,
            4 , 9 , 5 , 16
        };
        assertFalse(new MagicSquareChecker(4).isMagic(square, false));
        
        // 5x5
        square = new Integer[] {
            6 , 23, 19, 2 , 15,
            4 , 12, 25, 8 , 16,
            10, 18, 1 , 14, 22,
            11, 24, 7 , 20, 3 ,
            17, 5 , 13, 21, 9 
        };
        assertFalse(new MagicSquareChecker(5).isMagic(square, false));
    }
    
}
