package com.guisehn.main;

import com.guisehn.crossover.Crossover2;
import com.guisehn.crossover.CrossoverOperator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MagicSquareFinder {
    
    public static final int LOG_EVENT = 0;
    
    private final int size;
    private final int arraySize;
    private final int populationSize;
    private final int eliteSize;
    private final double mutationProbability;
    
    private final MagicSquareFitnessCalculator fitnessCalculator;
    private final RandomMagicSquareGenerator randomGenerator;
    private final IndividualComparator comparator;
    private final CrossoverOperator crossoverOperator;
    private final ActionListener listener;
    private final Random random = new Random();

    private Thread thread;
    private int generationCount;
    
    public MagicSquareFinder(int size, int populationSize, int eliteSize,
            double mutationProbability, ActionListener listener) {
        this.size = size;
        this.arraySize = (int)Math.pow(size, 2);
        this.populationSize = populationSize;
        this.eliteSize = eliteSize;
        this.mutationProbability = mutationProbability;

        this.fitnessCalculator = new MagicSquareFitnessCalculator(size);
        this.randomGenerator = new RandomMagicSquareGenerator(size);
        this.crossoverOperator = new Crossover2();
        this.comparator = new IndividualComparator();
        this.listener = listener;
    }
    
    public void start() {
        stop();
        
        thread = new Thread() {
            @Override
            public void run() {
                search();
            }
        };
        
        thread.start();
    }
    
    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }
    
    public int getGenerationCount() {
        return generationCount;
    }
    
    private void search() {
        List<Individual> population = generateInitialPopulation();

        generationCount = 0;
        
        StringBuilder log = new StringBuilder();
        
        while (true) {
            if (thread.isInterrupted()) {
                break;
            }

            ++generationCount;

            Collections.sort(population, comparator);
            
            log.append("Geração ").append(generationCount).append("\n");
            log.append("População:\n");

            for (int i = 0; i < populationSize; i++) {
                log.append(population.get(i)).append("\n");
            }
            
            log.append("---\n");
            
            if (generationCount == 1 || generationCount % 1000 == 0) {
                listener.actionPerformed(new ActionEvent(this, LOG_EVENT,
                        log.toString()));

                log.setLength(0);
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
            
            if (i1 == i2) {
                continue;
            }
            
            matingPool.add(i1.getFitness() > i2.getFitness() ? i1 : i2);
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
            
            for (Individual child : children) {
                String representation = child.toString();
                boolean duplicate = false;
                
                for (Individual individual : population) {
                    if (representation.equals(individual.toString())) {
                        duplicate = true;
                        break;
                    }
                }
                
                if (!duplicate) {
                    population.add(child);
                }
            }
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
