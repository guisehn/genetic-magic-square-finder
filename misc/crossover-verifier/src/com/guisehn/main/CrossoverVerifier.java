package com.guisehn.main;

import com.guisehn.crossover.Crossover2;
import java.util.Scanner;

public class CrossoverVerifier {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        int firstMutationPoint, secondMutationPoint;
        
        System.out.print("1st parent: ");
        int[] parent1 = stringToSquare(reader.nextLine());
        //int[] parent1 = stringToSquare("[2, 7, 6, 9, 5, 1, 4, 3, 8]");
        
        System.out.print("2nd parent: ");
        int[] parent2 = stringToSquare(reader.nextLine());
        //int[] parent2 = stringToSquare("[9, 2, 8, 3, 4, 7, 5, 1, 6]");

        System.out.print("Crossover point: ");
        int crossoverPoint = reader.nextInt();
        //int crossoverPoint = 5;

        reader.nextLine();
        System.out.println();
        
        new Crossover2().crossover(parent1, parent2, crossoverPoint
            , crossoverPoint);
    }
    
    private static int[] stringToSquare(String str) {
        String[] parts = str.replaceAll("(\\[|\\])", "").split(",\\s*");
        int[] square = new int[parts.length];
        
        for (int i = 0; i < parts.length; i++)
            square[i] = Integer.parseInt(parts[i]);
        
        return square;
    }
    
}
