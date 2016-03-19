package com.guisehn.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MagicSquareFinder {
    
    public static final int MAGIC_SQUARE_FOUND_EVENT = 0;
    public static final int SEARCH_ENDED_EVENT = 1;
    
    private final ActionListener listener;
    private final int limit;
    private final int size;

    private Thread thread;
    private int counter;

    public MagicSquareFinder(int size, int limit, ActionListener listener) {
        this.limit = limit;
        this.size = size;
        this.listener = listener;
        this.counter = 0;
    }
    
    public int getCounter() {
        return counter;
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
            System.out.println("parou!");
            thread.interrupt();
        }
    }
    
    public void search() {
        for (counter = 1; counter <= limit; counter++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                thread.interrupt(); // When this exception is thrown, Java
                                    // puts the thread into an active
                                    // state again ಠ_ಠ
                break;
            }
            
            if (thread.isInterrupted()) {
                break;
            }
            
            listener.actionPerformed(new ActionEvent(this,
                MAGIC_SQUARE_FOUND_EVENT,
                "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16"));
        }
        
        if (thread.isInterrupted()) {
            return;
        }
        
        listener.actionPerformed(new ActionEvent(this, SEARCH_ENDED_EVENT,
            limit + ""));

        stop();
    }
    
}
