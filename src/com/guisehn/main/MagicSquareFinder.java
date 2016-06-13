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
    private final double mutationProbability;

    private final MagicSquareFitnessCalculator fitnessCalculator;
    private final RandomMagicSquareGenerator randomGenerator;
    private final MagicSquareRepairer repairer;
    private final IndividualComparator comparator;
    private final Random random = new Random();
    
    public MagicSquareFinder(int size, int populationSize, double mutationProbability) {
        this.size = size;
        this.arraySize = (int)Math.pow(size, 2);
        this.populationSize = populationSize;
        this.mutationProbability = mutationProbability;

        this.fitnessCalculator = new MagicSquareFitnessCalculator(size);
        this.repairer = new MagicSquareRepairer(size);
        this.randomGenerator = new RandomMagicSquareGenerator(size);
        this.comparator = new IndividualComparator();
    }
    
    public void start() {
        List<Individual> population = generateInitialPopulation();
        int generationCount = 0;
        
        while (true) {
            ++generationCount;
            
            /*if (generationCount % 1000 == 0) {
                population = generateInitialPopulation();
            }*/
            
            Collections.sort(population, comparator);
            
            System.out.println("Geração " + generationCount);
            System.out.println("---");
            
            for (int i = 0; i < populationSize; i++) {
                Individual individual = population.get(i);
                
                System.out.println(Arrays.toString(individual.getSquare()) +
                    " - Fitness = " + individual.getFitness());
            }
            
            if(generationCount == 5){
                break;
            }
            
            createNewGeneration(population);
            
            System.out.println("===");
        }
    }
    
    private List<Individual> createMatingPool(List<Individual> population) {
        List<Individual> matingPool = new ArrayList<>();
        
        int poolSize = populationSize / 2;
        for (int i = 0; i < poolSize; i++) {
            Individual i1 = Utils.getRandom(population);
            Individual i2 = Utils.getRandom(population);
            
            matingPool.add(i1.getFitness() > i2.getFitness() ? i1 : i2);
        }
        
        return matingPool;
    }
    
    private void createNewGeneration(List<Individual> population) {
        List<Individual> matingPool = createMatingPool(population);

        population.clear();
        
        while (population.size() < populationSize) {
            Individual i1 = Utils.getRandom(matingPool);
            Individual i2 = Utils.getRandom(matingPool);
            Individual child = crossover(i1, i2);
            
            population.add(child);
        }
    }
    
    private Individual crossover(Individual i1, Individual i2) {
        Individual child;

        int middle = arraySize / 2;
        int[] square1 = i1.getSquare();
        int[] square2 = i2.getSquare();
        int[] newSquare = new int[arraySize];
        
        // pega metade de um e metade de outro
        for (int i = 0; i < arraySize; i++) {
            if (i <= middle) {
                newSquare[i] = square1[i];
            } else {
                newSquare[i] = square2[i];
            }
        }
        
        // mutação
        if (Math.random() <= mutationProbability) {
            int index1 = random.nextInt(arraySize);
            int index2 = random.nextInt(arraySize);
            int aux = newSquare[index1];
            newSquare[index1] = newSquare[index2];
            newSquare[index2] = aux;
        }
        
        // reparação
        child = new Individual(newSquare, fitnessCalculator);
        
        if (child.getFitness() == -1) {
            repairer.repair(child.getSquare());
            child.updateFitness();
        }
        
        return child;
    }
    
    private List<Individual> generateInitialPopulation() {
        List<Individual> individuals = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            individuals.add(new Individual(randomGenerator.generate(),
                fitnessCalculator));
        }
        
        return individuals;
    }
    
    private String serializeSquare(int[] square) {
        String[] numbersAsStrings = Arrays.stream(square).mapToObj(i -> i + "")
            .toArray(String[]::new);

        return String.join(",", numbersAsStrings);
    }
    
}

class Individual {
    private final MagicSquareFitnessCalculator fitnessCalculator;
    private final int[] square;

    private int fitness;
    
    public Individual(int[] square, MagicSquareFitnessCalculator fitnessCalculator) {
        this.square = square;
        this.fitnessCalculator = fitnessCalculator;

        updateFitness();
    }

    public int[] getSquare() {
        return square;
    }

    public int getFitness() {
        return fitness;
    }

    public final void updateFitness() {
        this.fitness = fitnessCalculator.calculateFitness(square, true);
    }
}

class IndividualComparator implements Comparator<Individual> {
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
