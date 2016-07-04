package com.guisehn.crossover;

public interface CrossoverOperator {

    public CrossoverResult crossover(int[] square1, int[] square2,
            int minimumCrossoverPoint, int maximumCrossoverPoint);
    
}
