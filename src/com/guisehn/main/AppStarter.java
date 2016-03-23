package com.guisehn.main;

import com.guisehn.ui.MainScreen;

public class AppStarter {

    public static void main(String[] args) {
        System.setProperty("apple.awt.application.name",
            "Matrizes Inteligentes");
        
        MainScreen screen = new MainScreen();
        screen.setVisible(true);
    }
    
}
