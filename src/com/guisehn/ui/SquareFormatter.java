package com.guisehn.ui;

import java.util.Arrays;

public class SquareFormatter {

    public static String format(int[] square) {
        String[] items = Arrays.stream(square).mapToObj(i -> i + "")
            .toArray(String[]::new);
        
        final int size = (int)Math.sqrt(items.length);
        final StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < items.length; i += size) {
            final String[] line = Arrays.copyOfRange(items, i, i + size);
            sb.append(String.join("\t", line)).append("\n");
        }
        
        return sb.toString().trim();
    }
    
}
