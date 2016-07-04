package com.guisehn.crossover;

public class CrossoverResult {

    private final int[][] children;
    private final String details;
    
    public CrossoverResult(int[][] children, String details) {
        this.children = children;
        this.details = details;
    }

    public int[][] getChildren() {
        return children;
    }

    public String getDetails() {
        return details;
    }
    
}
