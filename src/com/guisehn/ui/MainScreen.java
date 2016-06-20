package com.guisehn.ui;

import com.guisehn.main.MagicSquareFinder;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.Timer;

public class MainScreen extends javax.swing.JFrame {

    private final Chronometer chronometer;
    private final Timer generationCountTimer;

    private MagicSquareFinder finder;

    public MainScreen() {
        initComponents();
        
        chronometer = new Chronometer((ActionEvent e) -> {
            chronometerLabel.setText(e.getActionCommand());
        });
        
       generationCountTimer = new Timer(1000, (ActionEvent) -> {
            if (finder != null) {
                setGenerationCounterLabelText(finder.getGenerationCount());
            }
        });
        
        foundSquaresTextArea.setEditable(false);
        
        chronometerLabel.setVisible(false);
        generationCountLabel.setVisible(false);
    }

    private void startChronometer() {
        chronometerLabel.setText("");
        chronometerLabel.setVisible(true);

        chronometer.start();
    }
    
    private void startPermutationCounter() {
        setGenerationCounterLabelText(0);
        generationCountLabel.setVisible(true);
        generationCountTimer.start();
    }
    
    private void setGenerationCounterLabelText(long count) {
        generationCountLabel.setText(String.format("%,d", count)
            + " gerações até agora");
    }
    
    private void startFinder(int size, int populationSize, int eliteSize, 
            int mutationProbability) {
        generationLogTextArea.setText("");

        foundSquaresTextArea.setText("");
        foundSquaresTextArea.setTabSize(((int)Math.pow(size, 2) + "").length());
        
        if (finder != null) {
            finder.stop();
        }
        
        finder = new MagicSquareFinder(size, populationSize, eliteSize, mutationProbability,
            (ActionEvent e) -> {
                final int eventType = e.getID();

                try {
                    String textToAppend = e.getActionCommand();

                    switch (eventType) {
                        case MagicSquareFinder.LOG_EVENT:
                            if (clearLogCheckBox.isSelected()) {
                                generationLogTextArea.setText("");
                            }

                            generationLogTextArea.append(textToAppend);
                            break;

                        case MagicSquareFinder.MAGIC_SQUARE_FOUND_EVENT:
                            foundSquaresTextArea.append("Encontrado aos ");
                            foundSquaresTextArea.append(chronometerLabel.getText());
                            foundSquaresTextArea.append("\n" + textToAppend);
                            foundSquaresTextArea.append("\n---\n\n");
                            break;
                    }
                } catch (OutOfMemoryError err) {
                    JOptionPane.showMessageDialog(null, "Estouro de memória!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    
                    chronometer.stop();

                    throw err;
                }
            }
        );
        
        finder.start();
        startPermutationCounter();
    }
    
    private int getSquareSizeValue() {
        String text = squareSizeTextField.getText();
        return text.matches("[0-9]+") ? Integer.valueOf(text) : -1;
    }

    private int getPopulationSizeValue() {
        String text = populationSizeTextField.getText();
        return text.matches("[0-9]+") ? Integer.valueOf(text) : -1;
    }
    
    private int getEliteSizeValue() {
        String text = eliteSizeTextField.getText();
        return text.matches("[0-9]+") ? Integer.valueOf(text) : 0;
    }
    
    private int getMutationProbabilityValue() {
        String text = mutationProbabilityTextField.getText();
        return text.matches("0*(100|[0-9]{1,2})?") ? Integer.valueOf(text) : -1;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        foundSquaresTextArea = new javax.swing.JTextArea();
        squareSizeLabel = new javax.swing.JLabel();
        squareSizeTextField = new javax.swing.JTextField();
        startButton = new javax.swing.JButton();
        chronometerLabel = new javax.swing.JLabel();
        generationCountLabel = new javax.swing.JLabel();
        populationSizeLabel = new javax.swing.JLabel();
        populationSizeTextField = new javax.swing.JTextField();
        eliteSizeLabel = new javax.swing.JLabel();
        eliteSizeTextField = new javax.swing.JTextField();
        mutationProbabilityLabel = new javax.swing.JLabel();
        mutationProbabilityTextField = new javax.swing.JTextField();
        foundSquaresLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        generationLogTextArea = new javax.swing.JTextArea();
        generationHistoryLabel = new javax.swing.JLabel();
        stopButton = new javax.swing.JButton();
        clearLogCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        foundSquaresTextArea.setColumns(20);
        foundSquaresTextArea.setRows(5);
        foundSquaresTextArea.setBorder(null);
        foundSquaresTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(foundSquaresTextArea);

        squareSizeLabel.setText("Tamanho da matriz:");

        startButton.setText("Iniciar");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        chronometerLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        chronometerLabel.setText("[Chronometer]");

        generationCountLabel.setText("[Generation count]");

        populationSizeLabel.setText("Tamanho da população:");

        populationSizeTextField.setText("200");

        eliteSizeLabel.setText("Tamanho da elite:");

        eliteSizeTextField.setText("180");
        eliteSizeTextField.setToolTipText("");

        mutationProbabilityLabel.setText("Chance de mutação (%):");

        mutationProbabilityTextField.setText("5");
        mutationProbabilityTextField.setToolTipText("");

        foundSquaresLabel.setText("Matrizes encontradas");

        generationLogTextArea.setColumns(20);
        generationLogTextArea.setRows(5);
        generationLogTextArea.setBorder(null);
        generationLogTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane2.setViewportView(generationLogTextArea);

        generationHistoryLabel.setText("Histórico do algoritmo genético");

        stopButton.setText("Parar");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        clearLogCheckBox.setSelected(true);
        clearLogCheckBox.setText("Limpar histórico periodicamente");
        clearLogCheckBox.setToolTipText("");
        clearLogCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearLogCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(generationCountLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(chronometerLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(squareSizeLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(squareSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(populationSizeLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(populationSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(eliteSizeLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(eliteSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(mutationProbabilityLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mutationProbabilityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(clearLogCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(21, 21, 21))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(foundSquaresLabel)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(generationHistoryLabel)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jScrollPane2))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(squareSizeLabel)
                    .addComponent(squareSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eliteSizeLabel)
                    .addComponent(eliteSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearLogCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(populationSizeLabel)
                    .addComponent(populationSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mutationProbabilityLabel)
                    .addComponent(mutationProbabilityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(stopButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(foundSquaresLabel)
                    .addComponent(generationHistoryLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generationCountLabel)
                    .addComponent(chronometerLabel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        int size = getSquareSizeValue();
        int populationSize = getPopulationSizeValue();
        int eliteSize = getEliteSizeValue();
        int mutationProbability = getMutationProbabilityValue();
        
        if (size <= 0) {
            showMessageDialog(null, "O tamanho da matriz deve ser um valor"
                + " inteiro e positivo");
            squareSizeTextField.requestFocus();
            return;
        }
        
        if (populationSize <= 0) {
            showMessageDialog(null, "O tamanho da população deve ser um valor"
                + " inteiro e positivo");
            populationSizeTextField.requestFocus();
            return; 
        }

        if (eliteSize >= populationSize) {
            showMessageDialog(null, "O tamanho da elite deve ser menor que o"
                + " tamanho da população geral");
            populationSizeTextField.requestFocus();
            return; 
        }
        
        if (mutationProbability == -1) {
            showMessageDialog(null, "A chance de mutação deve ser um valor"
                + " inteiro de 0 e 100");
            mutationProbabilityTextField.requestFocus();
            return;            
        }
        
        if (size == 2) {
            JOptionPane.showMessageDialog(null,
                "Não existem quadrados mágicos 2x2. Mas iremos deixar o "
                + "algoritmo tentar encontrá-los! :)",
                "Resultado impossível", JOptionPane.INFORMATION_MESSAGE);
        }
        
        startChronometer();
        startFinder(size, populationSize, eliteSize, mutationProbability);
    }//GEN-LAST:event_startButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        if (finder != null) {
            finder.stop();
        }
        
        if (chronometer != null) {
            chronometer.stop();
        }
    }//GEN-LAST:event_stopButtonActionPerformed

    private void clearLogCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearLogCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearLogCheckBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel chronometerLabel;
    private javax.swing.JCheckBox clearLogCheckBox;
    private javax.swing.JLabel eliteSizeLabel;
    private javax.swing.JTextField eliteSizeTextField;
    private javax.swing.JLabel foundSquaresLabel;
    private javax.swing.JTextArea foundSquaresTextArea;
    private javax.swing.JLabel generationCountLabel;
    private javax.swing.JLabel generationHistoryLabel;
    private javax.swing.JTextArea generationLogTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel mutationProbabilityLabel;
    private javax.swing.JTextField mutationProbabilityTextField;
    private javax.swing.JLabel populationSizeLabel;
    private javax.swing.JTextField populationSizeTextField;
    private javax.swing.JLabel squareSizeLabel;
    private javax.swing.JTextField squareSizeTextField;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables
}
