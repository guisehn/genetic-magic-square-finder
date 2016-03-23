package com.guisehn.main;

public class Permutator {

    private final Integer[] array;
    private final int[] swappings;
    
    private long permutationCount;
    private boolean firstCall;
    private boolean ended;

    public Permutator(Integer[] array) {
        this.array = array.clone();
        this.permutationCount = 0;
        this.firstCall = true;
        this.ended = false;
        
        this.swappings = new int[array.length];
        for (int i = 0; i < swappings.length; i++) {
            swappings[i] = i;
        }
    }
    
    public long getPermutationCount() {
        return permutationCount;
    }

    public Integer[] next() {
        if (ended) {
            return null;
        }
        
        if (firstCall) {
            firstCall = false;
        } else if (!performSwaps()) {
            ended = true;
            return null;
        }
        
        permutationCount++;
        
        return array.clone();
    }
    
    private boolean performSwaps() {
        int i, l = array.length - 1;
        
        for (i = l; i >= 0 && swappings[i] == l; i--) {
            swap(i, swappings[i]);
            swappings[i] = i;
        }

        if (i < 0) {
            return false;
        }

        swap(i, swappings[i]);
        swappings[i]++;
        swap(i, swappings[i]);
        
        return true;
    }

    private void swap(int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

}
