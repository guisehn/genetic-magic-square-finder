package com.guisehn.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MagicSquareFinder {
    
    public static final int MAGIC_SQUARE_FOUND_EVENT = 0;
    public static final int SEARCH_ENDED_EVENT = 1;
    
    private final ActionListener listener;
    private final int limit;
    private final int size;
    private final List<Integer[]> foundSquares;

    private Thread thread;
    private Permutator permutator;

    public MagicSquareFinder(int size, int limit, ActionListener listener) {
        this.limit = limit;
        this.size = size;
        this.listener = listener;
        this.foundSquares = new ArrayList<>();
    }
    
    public int getAmountOfSquaresFound() {
        return foundSquares.size();
    }

    public long getPermutationCount() {
        return permutator.getPermutationCount();
    }
    
    public void start() {
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
    
    public void search() {
        foundSquares.clear();
        
        MagicSquareChecker checker = new MagicSquareChecker(size);

        Integer[] square = buildInitialSquare();
        permutator = new Permutator(square);
        
        while ((square = permutator.next()) != null) {
            if (thread.isInterrupted() || foundSquares.size() == limit) {
                break;
            }
            
            if (checker.isMagic(square, false)) {
                foundSquares.add(square);
                
                listener.actionPerformed(new ActionEvent(this,
                    MAGIC_SQUARE_FOUND_EVENT, serializeSquare(square)));
            }
        }
        
        if (!thread.isInterrupted()) {
            listener.actionPerformed(new ActionEvent(this, SEARCH_ENDED_EVENT,
                foundSquares.size() + ""));

            stop();
        }
    }
    
    private Integer[] buildInitialSquare() {
        final Integer[] square = new Integer[(int)Math.pow(size, 2)];
        
        for (int i = 0; i < square.length; i++) {
            square[i] = i + 1;
        }
        
        return square;
    }
    
    private String serializeSquare(Integer[] square) {
        String[] numbers = new String[square.length];
        
        for (int i = 0; i < square.length; i++) {
            numbers[i] = square[i] + "";
        }

        return String.join(",", numbers);
    }
    
}
