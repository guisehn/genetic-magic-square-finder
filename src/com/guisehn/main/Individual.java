package com.guisehn.main;

import java.util.Arrays;

public class Individual {
    private final MagicSquareFitnessCalculator fitnessCalculator;
    private final int[] square;
    private Integer fitness;
    
    public Individual(int[] square, MagicSquareFitnessCalculator fitnessCalculator) {
        this.square = square;
        this.fitnessCalculator = fitnessCalculator;
    }

    public int[] getSquare() {
        return square;
    }

    public int getFitness() {
        if (fitness == null) {
            fitness = fitnessCalculator.calculateFitness(square, true);
        }

        return fitness;
    }

    @Override
    public String toString() {
        return "(" + fitness + ") " + Arrays.toString(square);
    }
}
