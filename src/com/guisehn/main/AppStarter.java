package com.guisehn.main;

import java.util.Scanner;

public class AppStarter {
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        System.out.println("Digite o tamanho do quadrado mágico:");
        int size = in.nextInt();

        System.out.println("Digite o tamanho da população:");
        int populationSize = in.nextInt();
        
        MagicSquareFinder finder = new MagicSquareFinder(size, populationSize);
        finder.start();
        
        /*MainScreen screen = new MainScreen();
        screen.setVisible(true);*/
    }
    
}
