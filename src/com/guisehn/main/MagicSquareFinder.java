package com.guisehn.main;

import com.guisehn.crossover.Crossover2;
import com.guisehn.crossover.CrossoverOperator;
import com.guisehn.crossover.CrossoverResult;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MagicSquareFinder {
    
    public static final int LOG_EVENT = 0;
    public static final int MAGIC_SQUARE_FOUND_EVENT = 1;
    public static final int SEARCH_ENDED_EVENT = 2;
    
    private final int size;
    private final int arraySize;
    private final int populationSize;
    private final int eliteSize;
    private final int eliteDeathPeriod;
    private final int minimumCrossoverPoint;
    private final int maximumCrossoverPoint;
    private final double mutationProbability;
    private final boolean allowDuplicates;
    private final boolean showGenerationDetails;
    
    private final MagicSquareFitnessCalculator fitnessCalculator;
    private final RandomMagicSquareGenerator randomGenerator;
    private final IndividualComparator comparator;
    private final CrossoverOperator crossoverOperator;
    private final ActionListener listener;
    private final Random random = new Random();
    private final Set<Individual> magicSquaresFound;
    private final List<Individual> population;
    private final StringBuilder log;

    private Thread thread;
    private int generationCount;
    private int amountOfGenerationsSinceLastNewMagicSquare;
    
    public MagicSquareFinder(int size, int populationSize, int eliteSize,
             int eliteDeathPeriod, double mutationProbability,
             boolean allowDuplicates, int minimumCrossoverPoint,
             int maximumCrossoverPoint, boolean showGenerationDetails,
             ActionListener listener) {
        this.size = size;
        this.arraySize = (int)Math.pow(size, 2);
        this.populationSize = populationSize;
        this.eliteSize = eliteSize;
        this.eliteDeathPeriod = eliteDeathPeriod;
        this.mutationProbability = mutationProbability;
        this.allowDuplicates = allowDuplicates;
        this.minimumCrossoverPoint = minimumCrossoverPoint;
        this.maximumCrossoverPoint = maximumCrossoverPoint;
        this.showGenerationDetails = showGenerationDetails;
        
        this.fitnessCalculator = new MagicSquareFitnessCalculator(size);
        this.randomGenerator = new RandomMagicSquareGenerator(size);
        this.crossoverOperator = new Crossover2();
        this.comparator = new IndividualComparator();
        this.magicSquaresFound = new HashSet<>();
        this.population = new ArrayList<>();
        this.log = new StringBuilder();
        this.listener = listener;
    }
    
    /**
     * Inicia busca
     */
    public void start() {
        stop();
        
        thread = new Thread() {
            @Override
            public void run() {
                startGeneticAlgorithm();
            }
        };
        
        thread.start();
    }
    
    /**
     * Encerra busca
     */
    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }
    }
    
    /**
     * Retorna contagem de geração atual
     * @return contagem de geração atual
     */
    public int getGenerationCount() {
        return generationCount;
    }
    
    /**
     * Inicia o algoritmo genético
     */
    private void startGeneticAlgorithm() {
        generationCount = amountOfGenerationsSinceLastNewMagicSquare = 0;
        log.setLength(0);

        generateInitialPopulation();

        while (!thread.isInterrupted()) {
            sortPopulation();
            addCurrentGenerationToLog();
            
            // Publica o log para a saída a cada N gerações.
            if (generationCount == 0 || generationCount % 200 == 0) {
                publishAndClearLog();
            }
            
            addAndPublishMagicSquares();
            
            if (checkForCompletion()) {
                break;
            }
            
            createNewGeneration();
        }
        
        stop();
    }
    
    /**
     * Ordena população conforme a aptidão
     */
    private void sortPopulation() {
        Collections.sort(population, comparator);
    }
    
    /**
     * Gera a população inicial aleatoriamente
     */
    private void generateInitialPopulation() {
        population.clear();

        for (int i = 0; i < populationSize; i++) {
            population.add(new Individual(randomGenerator.generate(),
                null, null, null, "", fitnessCalculator));
        }
    }

    /**
     * Caso existam novos quadrados mágicos na geração atual, adiciona-os
     * na lista e publica para a saída
     */
    private void addAndPublishMagicSquares() {
        Individual[] magicSquares = population.stream()
            .filter(i -> i.getFitness() == 0)
            .toArray(Individual[]::new);
        
        for (Individual magicSquare : magicSquares) {
            boolean added = magicSquaresFound.add(magicSquare);

            if (added) {
                amountOfGenerationsSinceLastNewMagicSquare = 0;
                publishMagicSquare(magicSquare);
            } else {
                /*System.out.println("achou igual!" + Arrays.toString(magicSquare.getSquare()));
                System.out.println("pai1=" + Arrays.toString(magicSquare.getParent1()));
                System.out.println("pai2=" + Arrays.toString(magicSquare.getParent2()));
                System.out.println("crossover=" + magicSquare.getCrossoverDetails());
                System.out.println("---");*/
            }
        }
    }
    
    /**
     * Verifica se já encontrou todos os quadrados mágicos esperados.
     * @return Retorna verdadeiro se encontrou todos e deve encerrar algoritmo.
     */
    private boolean checkForCompletion() {
        int amountFound = magicSquaresFound.size();
        
        if (!thread.isInterrupted() && (
            (size == 1 && amountFound == 1) ||
            (size == 3 && amountFound == 8) ||
            amountFound >= 10)
        ) {
            listener.actionPerformed(new ActionEvent(this, SEARCH_ENDED_EVENT,
                null));
            
            publishAndClearLog();
            return true;
        }
        
        return false;
    }
    
    /**
     * Adiciona a geração atual para o log
     */
    private void addCurrentGenerationToLog() {
        log.append("======================\nGeração ").append(generationCount)
            .append("\n======================");

        int i = 0;
        
        for (Individual individual : population) {
            if (showGenerationDetails) {
                log.append("\nIndivíduo ").append(++i);
            }
            
            log.append("\n").append(individual.toString(true));
            
            if (showGenerationDetails) {
                log.append("\n").append(individual.getGenerationDetails(true))
                    .append("\n---");
            }
        }
        
        log.append("\n");
    }
    
    /**
     * Publica o log para a saída e limpa-o
     */
    private void publishAndClearLog() {
        listener.actionPerformed(new ActionEvent(this, LOG_EVENT, log.toString()));
        log.setLength(0);
    }
    
    /**
     * Publica um quadrado mágico para a saída
     * @param magicSquare Indivíduo contendo o quadrado mágico
     */
    private void publishMagicSquare(Individual magicSquare) {
        StringBuilder sb = new StringBuilder();
        sb.append(SquareFormatter.format(magicSquare.getSquare()));
        sb.append("\n");
        sb.append("\nNº da geração: ").append(generationCount);
        sb.append("\n").append(magicSquare.getGenerationDetails(false));

        listener.actionPerformed(new ActionEvent(this, MAGIC_SQUARE_FOUND_EVENT,
            sb.toString()));
    }
    
    /**
     * Seleciona os indivíduos para cruzamento utilizando torneio
     * @return Indivíduos selecionados para cruzamento após torneio
     */
    private List<Individual> createMatingPool() {
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
    
    /**
     * Cria nova geração
     */
    private void createNewGeneration() {
        generationCount++;

        // Mata a elite a cada N gerações sem um novo quadrado mágico
        if (eliteDeathPeriod != 0 && amountOfGenerationsSinceLastNewMagicSquare > eliteDeathPeriod) {
            population.subList(0, eliteSize).clear();
            amountOfGenerationsSinceLastNewMagicSquare = 0;
        } else {
            amountOfGenerationsSinceLastNewMagicSquare++;
        }
        
        // Cruza os indivíduos
        List<Individual> matingPool = createMatingPool();
        
        // Elitismo. Mantém os melhores N indivíduos (que estão no inicio
        // da população, já que ela é ordenada pelo fitness) para a próxima
        // geração.
        try {
            population.subList(eliteSize, populationSize).clear();
            population.stream().forEach(individual -> {
                individual.setBelongsToElite(true);
            });
        } catch (java.lang.IndexOutOfBoundsException e) { }
        
        while (population.size() < populationSize) {
            Individual i1 = Utils.getRandom(matingPool);
            Individual i2 = Utils.getRandom(matingPool);
            Individual[] children = crossoverAndMutate(i1, i2);
            
            if (allowDuplicates) {
                population.addAll(Arrays.asList(children));
            } else {
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
    }
    
    /**
     * Faz crossover de dois indivíduos e mutação
     * @param parent1 primeiro pai
     * @param parent2 segundo pai
     * @return filhos gerados
     */
    private Individual[] crossoverAndMutate(Individual parent1, Individual parent2) {
        CrossoverResult result = crossoverOperator.crossover(parent1.getSquare(),
                parent2.getSquare(), minimumCrossoverPoint, maximumCrossoverPoint);

        int[][] children = result.getChildren();
        int[][] mutationPoints = new int[children.length][];
 
        // Mutação
        for (int i = 0; i < children.length; i++) {
            int[] child = children[i];

            if (Math.random() <= mutationProbability) {
                int index1, index2;
                
                do {
                    index1 = random.nextInt(arraySize);
                    index2 = random.nextInt(arraySize);
                }
                while (index1 == index2);
                
                int aux = child[index1];
                child[index1] = child[index2];
                child[index2] = aux;

                mutationPoints[i] = new int[] { index1, index2 };
            } else {
                mutationPoints[i] = null;
            }
        }
        
        // Monta os objetos do tipo Individual
        Individual[] individuals = new Individual[children.length];
        
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual(children[i], parent1.getSquare(),
                parent2.getSquare(), mutationPoints[i], result.getDetails(),
                fitnessCalculator);
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
