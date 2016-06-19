package com.guisehn.main;

import com.guisehn.crossover.Crossover1;
import com.guisehn.crossover.Crossover2;
import com.guisehn.crossover.CrossoverOperator;
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
    private final int eliteSize;
    private final double mutationProbability;
    
    private final MagicSquareFitnessCalculator fitnessCalculator;
    private final RandomMagicSquareGenerator randomGenerator;
    private final IndividualComparator comparator;
    private final CrossoverOperator crossoverOperator;
    private final Random random = new Random();
    
    public MagicSquareFinder(int size, int populationSize, int eliteSize, double mutationProbability) {
        this.size = size;
        this.arraySize = (int)Math.pow(size, 2);
        this.populationSize = populationSize;
        this.eliteSize = eliteSize;
        this.mutationProbability = mutationProbability;

        this.fitnessCalculator = new MagicSquareFitnessCalculator(size);
        this.crossoverOperator = new Crossover2();
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
            
            Individual[] children = crossoverAndMutate(i1, i2);
            population.addAll(Arrays.asList(children));
        }
    }
    
    private Individual[] crossoverAndMutate(Individual i1, Individual i2) {
        int[][] children = crossoverOperator.crossover(i1.getSquare(),
                i2.getSquare());
 
        // mutação
        for (int[] child : children) {
            if (Math.random() <= mutationProbability) {
                int index1 = random.nextInt(arraySize);
                int index2 = random.nextInt(arraySize);
                int aux = child[index1];
                child[index1] = child[index2];
                child[index2] = aux;
            }   
        }
        
        // transforma pra Individual
        Individual[] individuals = new Individual[children.length];
        
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual(children[i], fitnessCalculator);
        }
        
        return individuals;
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
