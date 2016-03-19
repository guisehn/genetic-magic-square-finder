package com.guisehn.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.Timer;

public class Chronometer {

    private final ActionListener listener;
    private final DecimalFormat decimalFormatter;
    
    private Timer timer;
    private long startTime;
    
    public Chronometer(ActionListener listener) {
        this.listener = listener;
        this.decimalFormatter = new DecimalFormat("00");
    }
    
    public String getTime() {
        final long now = System.currentTimeMillis();
        long diff = now - startTime;

        final long hours = diff / (60 * 60 * 1000);
        diff = Math.round(diff % (60 * 60 * 1000));

        final long minutes = diff / (60 * 1000);
        diff = Math.round(diff % (60 * 1000));

        final long seconds = diff / 1000;
        diff = Math.round(diff % 1000);

        final long centiseconds = diff / 10;

        return decimalFormatter.format(hours) + ":" 
            + decimalFormatter.format(minutes) + ":"
            + decimalFormatter.format(seconds) + "."
            + decimalFormatter.format(centiseconds);
    }
    
    public void start() {
        stop();
        
        startTime = System.currentTimeMillis();
        
        timer = new Timer(1, (ActionEvent) -> {
            listener.actionPerformed(new ActionEvent(this, 0, getTime()));
        });
        
        timer.start();
    }
    
    public void stop() {
        if (timer != null) {
            timer.stop();
        }
    }
    
}
