package com.guisehn.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MagicSquareFinder {
    
    private final int size;
    private final int arraySize;
    private final int populationSize;
    private final MagicSquareFitnessCalculator fitnessCalculator;
    private final RandomMagicSquareGenerator randomGenerator;
    private final CustomComparator comparator = new CustomComparator();
    private final Random random = new Random();
    
    public MagicSquareFinder(int size, int populationSize) {
        this.size = size;
        this.arraySize = (int)Math.pow(size, 2);
        this.populationSize = populationSize;
        this.fitnessCalculator = new MagicSquareFitnessCalculator(size);
        this.randomGenerator = new RandomMagicSquareGenerator(size);
    }
    
    public void start() {
        List<Individual> population = generateInitialPopulation();
        int generationCount = 0;
        
        while (true) {
            ++generationCount;
            
            population = generateInitialPopulation();
            
            /*if (generationCount % 1000 == 0) {
                population = generateInitialPopulation();
            }*/
            
            Collections.sort(population, comparator);
            
            for (int i = 0; i < populationSize; i++) {
                Individual individual = population.get(i);
                
                //System.out.println(Arrays.toString(individual.getSquare()));
                //System.out.println("Fitness = " + individual.getFitness());
            }
            
            System.out.println("Tentativa " + generationCount);
            
            generateNewGeneration(population, 4);
            
            System.out.println("------");
        }
    }
    
    private void generateNewGeneration(List<Individual> population, int amountToCross) {
        Individual first = population.get(0);
        Individual second = population.get(1);
        Individual third = population.get(2);
        Individual fourth = population.get(3);

        List<Individual> children = new ArrayList<>();
        children.add(crossover(first, second));
        children.add(crossover(first, third));
        children.add(crossover(second, third));
        children.add(crossover(second, fourth));
        
        for (Individual i : children) {
            if (i.getFitness() == -1) {
                System.out.println("Filho inválido (números repetidos):");
                System.out.println(Arrays.toString(i.getSquare()));
            } else {
                System.out.println("Gerou filho válido:");
                System.out.println(Arrays.toString(i.getSquare()));
                System.exit(0);
            }
        }
        
        /*population.add(crossover(first, second));
        population.add(crossover(first, third));
        
        population.add(crossover(second, third));
        population.add(crossover(second, fourth));*/
    }
    
    private Individual crossover(Individual i1, Individual i2) {
        int middle = arraySize / 2;
        int[] square1 = i1.getSquare();
        int[] square2 = i2.getSquare();
        int[] newSquare = new int[arraySize];
        
        // pega metade de um, e metade de outro
        for (int i = 0; i < arraySize; i++) {
            if (i <= middle) {
                newSquare[i] = square1[i];
            } else {
                newSquare[i] = square2[i];
            }
        }
        
        // mutação
        if (Math.random() < 0.001) {
            int index1 = random.nextInt(arraySize);
            int index2 = random.nextInt(arraySize);
            int aux = newSquare[index1];
            newSquare[index1] = newSquare[index2];
            newSquare[index2] = aux;
        }
        
        repair(newSquare);
        
        return new Individual(newSquare);
    }
    
    private void repair(int[] square) {
        
    }
    
    private List<Individual> generateInitialPopulation() {
        List<Individual> individuals = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            individuals.add(new Individual(randomGenerator.generate()));
        }
        
        return individuals;
    }
    
    private String serializeSquare(int[] square) {
        String[] numbersAsStrings = Arrays.stream(square).mapToObj(i -> i + "")
            .toArray(String[]::new);

        return String.join(",", numbersAsStrings);
    }
    
    public class Individual {
        private int[] square;
        private int fitness;
        
        public Individual(int[] square) {
            this.square = square;
            this.fitness = fitnessCalculator.calculateFitness(square, true);
        }
        
        public int[] getSquare() {
            return square;
        }
        
        public int getFitness() {
            return fitness;
        }
    }
    
    public class CustomComparator implements Comparator<Individual> {
        @Override
        public int compare(Individual o1, Individual o2) {
            int f1 = o1.getFitness();
            int f2 = o2.getFitness();

            if (f1 > f2) {
                return 1;
            }
            
            if (f1 == f2) {
                return 0;
            }
            
            return -1;
        }
    }
    
}
