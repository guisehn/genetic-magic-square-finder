package com.guisehn.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MagicSquareFinder {
    
    private final int size;
    private final int arraySize;
    private final int populationSize;
    private final int eliteSize;
    private final double mutationProbability;
    
    private final MagicSquareFitnessCalculator fitnessCalculator;
    private final RandomMagicSquareGenerator randomGenerator;
    private final MagicSquareRepairer repairer;
    private final IndividualComparator comparator;
    private final Random random = new Random();
    
    public MagicSquareFinder(int size, int populationSize, int eliteSize, double mutationProbability) {
        this.size = size;
        this.arraySize = (int)Math.pow(size, 2);
        this.populationSize = populationSize;
        this.eliteSize = eliteSize;
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

            Collections.sort(population, comparator);
            
            if (generationCount == 1 || generationCount % 1000 == 0) {
                System.out.println("Geração " + generationCount);
                
                System.out.println("Elite:");
                for (int i = 0; i < eliteSize; i++) {
                    System.out.println("Melhor fitness = " + population.get(i));
                }
                
                System.out.println("---");                
            }
            
            createNewGeneration(population);
        }
    }
    
    private List<Individual> createMatingPool(List<Individual> population) {
        List<Individual> matingPool = new ArrayList<>();
        
        int poolSize = populationSize / 2;
        for (int i = 0; i < poolSize; i++) {
            Individual i1 = Utils.getRandom(population);
            Individual i2 = Utils.getRandom(population);
            
            matingPool.add(i1.getFitness() > i2.getFitness() ? i2 : i2);
        }
        
        return matingPool;
    }
    
    private void createNewGeneration(List<Individual> population) {
        List<Individual> matingPool = createMatingPool(population);

        // Elitismo. Mantém os melhores N indivíduos (que estão no inicio
        // da população, já que ela é ordenada pelo fitness) para a próxima
        // geração.
        population.subList(eliteSize, populationSize).clear();
        
        while (population.size() < populationSize) {
            Individual i1 = Utils.getRandom(matingPool);
            Individual i2 = Utils.getRandom(matingPool);
            
            // Não permite que indivíduo cruze consigo mesmo pois irá gerar
            // filho idêntico.
            if (i1 == i2) {
                continue;
            }
            
            Individual child1 = crossover(i1, i2);
            
            population.add(child1);
        }
    }
    
    private Individual crossover(Individual i1, Individual i2) {
        int maxIndex = arraySize - 1;
        int cutPoint = random.nextInt(maxIndex - 1) + 1;
        int[] square1 = i1.getSquare();
        int[] square2 = i2.getSquare();
        int[] newSquare = new int[arraySize];
        Set<Integer> used = new HashSet<>();
        
        for (int i = 0; i < cutPoint; i++) {
            newSquare[i] = square1[i];
            used.add(newSquare[i]);
        }
        
        for (int i = 0, j = cutPoint; j < arraySize; i++) {
            int n = square2[i];

            if (!used.contains(n)) {
                newSquare[j++] = square2[i];
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
        
        Individual child = new Individual(newSquare, fitnessCalculator);
        
        /*System.out.println("CUT POINT: " + cutPoint);
        System.out.println("PAI 1 = " + Arrays.toString(i1.getSquare()) + " - Fitness : " + i1.getFitness());
        System.out.println("PAI 2 = " + Arrays.toString(i2.getSquare()) + " - Fitness : " + i2.getFitness());
        System.out.println("FILHO = " + Arrays.toString(child.getSquare()) + " - Fitness : " + child.getFitness());*/
        
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
    
    @Override
    public String toString() {
        return "(" + fitness + ") " + Arrays.toString(square);
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
