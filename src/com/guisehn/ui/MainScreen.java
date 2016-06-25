package com.guisehn.ui;

import com.guisehn.main.MagicSquareFinder;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import static javax.swing.JOptionPane.showMessageDialog;

public class MainScreen extends javax.swing.JFrame {

    private final Chronometer chronometer;
    private final Timer generationCountTimer;
    private int amountFound;
    private int previousSquareSize;

    private MagicSquareFinder finder;

    public MainScreen() {
        initComponents();
        
        resetCrossoverPoints();
        
        previousSquareSize = getSquareSizeValue();
        
        chronometer = new Chronometer((ActionEvent e) -> {
            chronometerLabel.setText(e.getActionCommand());
        });
        
       generationCountTimer = new Timer(1000, (ActionEvent) -> {
            if (finder != null) {
                setGenerationCounterLabelText(finder.getGenerationCount());
            }
        });
        
        foundSquaresTextArea.setEditable(false);
        generationLogTextArea.setEditable(false);

        chronometerLabel.setVisible(false);
        generationCountLabel.setVisible(false);
    }
    
    private int getMinimumCrossoverPointAllowed() {
        return 0;
    }
    
    private int getMaximumCrossoverPointAllowed(int size) {
        return (size * size) - 1;
    }
    
    private void resetCrossoverPoints() {
        int size = getSquareSizeValue();
        int min = getMinimumCrossoverPointAllowed();
        int max = getMaximumCrossoverPointAllowed(size);
        
        if (size > 1) {
            min++;
            max--;
        }
        
        if (size > 0) {
            minimumCrossoverPointTextField.setText("" + min);
            maximumCrossoverPointTextField.setText("" + max);
        }
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
        generationCountLabel.setText("Geração " + String.format("%,d", count));
    }
    
    private void startFinder(int size, int populationSize, int eliteSize, 
            int eliteDeathPeriod, double mutationProbability,
            boolean allowDuplicates, int minimumCrossoverPoint,
            int maximumCrossoverPoint) {
        amountFound = 0;
        
        generationLogTextArea.setText("");

        foundSquaresTextArea.setText("");
        foundSquaresTextArea.setTabSize(((int)Math.pow(size, 2) + "").length());
        
        if (finder != null) {
            finder.stop();
        }
        
        finder = new MagicSquareFinder(size, populationSize, eliteSize,
            eliteDeathPeriod, mutationProbability, allowDuplicates,
            minimumCrossoverPoint, maximumCrossoverPoint,
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
                            foundSquaresTextArea.append("Quadrado " + (++amountFound) + " encontrado aos ");
                            foundSquaresTextArea.append(chronometerLabel.getText());
                            foundSquaresTextArea.append("\n" + textToAppend);
                            foundSquaresTextArea.append("\n\n");
                            break;
                            
                        case MagicSquareFinder.SEARCH_ENDED_EVENT:
                            foundSquaresTextArea.append("Busca encerrada.");
                            chronometer.stop();
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

    private int getMinimumCrossoverPointValue() {
        String text = minimumCrossoverPointTextField.getText();
        return text.matches("[0-9]+") ? Integer.valueOf(text) : -1;
    }

    private int getMaximumCrossoverPointValue() {
        String text = maximumCrossoverPointTextField.getText();
        return text.matches("[0-9]+") ? Integer.valueOf(text) : -1;
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
    
    private int getEliteDeathPeriod() {
        String text = eliteDeathPeriodTextField.getText();
        return text.matches("[0-9]+") ? Integer.valueOf(text) : -1;
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
        startButton = new javax.swing.JButton();
        chronometerLabel = new javax.swing.JLabel();
        generationCountLabel = new javax.swing.JLabel();
        foundSquaresLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        generationLogTextArea = new javax.swing.JTextArea();
        generationHistoryLabel = new javax.swing.JLabel();
        stopButton = new javax.swing.JButton();
        clearLogCheckBox = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        squareSizeLabel = new javax.swing.JLabel();
        squareSizeTextField = new javax.swing.JTextField();
        populationSizeLabel = new javax.swing.JLabel();
        populationSizeTextField = new javax.swing.JTextField();
        allowDuplicatesCheckBox = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        eliteSizeLabel = new javax.swing.JLabel();
        eliteSizeTextField = new javax.swing.JTextField();
        eliteDeathPeriodLabel = new javax.swing.JLabel();
        eliteDeathPeriodTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        mutationProbabilityTextField = new javax.swing.JTextField();
        mutationProbabilityLabel = new javax.swing.JLabel();
        minimumCrossoverPointLabel = new javax.swing.JLabel();
        minimumCrossoverPointTextField = new javax.swing.JTextField();
        maximumCrossoverPointTextField = new javax.swing.JTextField();
        maximumCrossoverPointLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        foundSquaresTextArea.setColumns(20);
        foundSquaresTextArea.setRows(5);
        foundSquaresTextArea.setBorder(null);
        foundSquaresTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(foundSquaresTextArea);

        startButton.setText("Iniciar");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        chronometerLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        chronometerLabel.setText("[Chronometer]");

        generationCountLabel.setText("[Generation count]");

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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("População"));
        jPanel1.setToolTipText("");

        squareSizeLabel.setText("Tamanho da matriz:");

        squareSizeTextField.setText("4");
        squareSizeTextField.setNextFocusableComponent(populationSizeTextField);
        squareSizeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                squareSizeTextFieldFocusLost(evt);
            }
        });

        populationSizeLabel.setText("Tamanho da população:");

        populationSizeTextField.setText("200");
        populationSizeTextField.setNextFocusableComponent(eliteSizeTextField);

        allowDuplicatesCheckBox.setText("Permitir indivíduos idênticos");
        allowDuplicatesCheckBox.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(squareSizeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(squareSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(populationSizeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(populationSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(allowDuplicatesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(squareSizeLabel)
                    .addComponent(squareSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(populationSizeLabel)
                    .addComponent(populationSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(allowDuplicatesCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Elitismo"));

        eliteSizeLabel.setText("Tamanho da elite:");

        eliteSizeTextField.setText("180");
        eliteSizeTextField.setToolTipText("");
        eliteSizeTextField.setNextFocusableComponent(eliteDeathPeriodTextField);

        eliteDeathPeriodLabel.setText("Período de morte da elite:");

        eliteDeathPeriodTextField.setText("15000");
        eliteDeathPeriodTextField.setToolTipText("");
        eliteDeathPeriodTextField.setNextFocusableComponent(mutationProbabilityTextField);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(eliteSizeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(eliteSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(eliteDeathPeriodLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(eliteDeathPeriodTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eliteSizeLabel)
                    .addComponent(eliteSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eliteDeathPeriodLabel)
                    .addComponent(eliteDeathPeriodTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Cruzamento"));
        jPanel3.setToolTipText("");

        mutationProbabilityTextField.setText("50");
        mutationProbabilityTextField.setToolTipText("");
        mutationProbabilityTextField.setNextFocusableComponent(startButton);

        mutationProbabilityLabel.setText("Chance de mutação (%):");

        minimumCrossoverPointLabel.setText("Ponto mínimo:");

        minimumCrossoverPointTextField.setToolTipText("");
        minimumCrossoverPointTextField.setNextFocusableComponent(startButton);

        maximumCrossoverPointTextField.setToolTipText("");
        maximumCrossoverPointTextField.setNextFocusableComponent(startButton);

        maximumCrossoverPointLabel.setText("Ponto máximo:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(minimumCrossoverPointLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(minimumCrossoverPointTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mutationProbabilityLabel)
                            .addComponent(maximumCrossoverPointLabel))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mutationProbabilityTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(maximumCrossoverPointTextField))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minimumCrossoverPointLabel)
                    .addComponent(minimumCrossoverPointTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maximumCrossoverPointLabel)
                    .addComponent(maximumCrossoverPointTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mutationProbabilityLabel)
                    .addComponent(mutationProbabilityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );

        jMenu1.setText("Ajuda");

        aboutMenuItem.setText("Sobre o programa");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(aboutMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(generationCountLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(chronometerLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(foundSquaresLabel)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(generationHistoryLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 493, Short.MAX_VALUE))
                                    .addComponent(jScrollPane2)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearLogCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton)
                    .addComponent(stopButton)
                    .addComponent(clearLogCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(foundSquaresLabel)
                    .addComponent(generationHistoryLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
        int eliteDeathPeriod = getEliteDeathPeriod();
        int minimumCrossoverPoint = getMinimumCrossoverPointValue();
        int minimumCrossoverPointAllowed = getMinimumCrossoverPointAllowed();
        int maximumCrossoverPoint = getMaximumCrossoverPointValue();
        int maximumCrossoverPointAllowed = getMaximumCrossoverPointAllowed(size);
        boolean allowDuplicates = allowDuplicatesCheckBox.isSelected();
        
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
        
        if (eliteDeathPeriod < 0) {
            showMessageDialog(null, "O período de morte da elite deve ser um número" +
                "inteiro maior ou igual a 0. Use 0 para que a elite nunca morra.");
            eliteDeathPeriodTextField.requestFocus();
            return; 
        }
        
        if (mutationProbability == -1) {
            showMessageDialog(null, "A chance de mutação deve ser um valor"
                + " inteiro de 0 e 100");
            mutationProbabilityTextField.requestFocus();
            return;            
        }
        
        if (minimumCrossoverPoint < minimumCrossoverPointAllowed ||
                maximumCrossoverPoint > maximumCrossoverPointAllowed) {
            showMessageDialog(null, "Os pontos mínimos e máximos de crossover são" +
                " respectivamente " + minimumCrossoverPointAllowed + " e " +
                maximumCrossoverPointAllowed + "\npara matrizes " + size + "x" +
                size + ".");
            return;
        }
        
        if (size == 2) {
            JOptionPane.showMessageDialog(null,
                "Não existem quadrados mágicos 2x2. Mas iremos deixar o "
                + "algoritmo tentar encontrá-los! :)",
                "Resultado impossível", JOptionPane.INFORMATION_MESSAGE);
        }
        
        startChronometer();
        startFinder(size, populationSize, eliteSize, eliteDeathPeriod,
            mutationProbability * 0.01, allowDuplicates,
            minimumCrossoverPoint, maximumCrossoverPoint);
    }//GEN-LAST:event_startButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        if (finder != null) {
            finder.stop();
        }
        
        if (chronometer != null) {
            chronometer.stop();
        }
    }//GEN-LAST:event_stopButtonActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/guisehn/genetic-magic-square-finder/blob/master/README.md"));
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void squareSizeTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_squareSizeTextFieldFocusLost
        int size = getSquareSizeValue();

        if (size != previousSquareSize) {
            previousSquareSize = size;
            resetCrossoverPoints();
        }
    }//GEN-LAST:event_squareSizeTextFieldFocusLost

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
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JCheckBox allowDuplicatesCheckBox;
    private javax.swing.JLabel chronometerLabel;
    private javax.swing.JCheckBox clearLogCheckBox;
    private javax.swing.JLabel eliteDeathPeriodLabel;
    private javax.swing.JTextField eliteDeathPeriodTextField;
    private javax.swing.JLabel eliteSizeLabel;
    private javax.swing.JTextField eliteSizeTextField;
    private javax.swing.JLabel foundSquaresLabel;
    private javax.swing.JTextArea foundSquaresTextArea;
    private javax.swing.JLabel generationCountLabel;
    private javax.swing.JLabel generationHistoryLabel;
    private javax.swing.JTextArea generationLogTextArea;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel maximumCrossoverPointLabel;
    private javax.swing.JTextField maximumCrossoverPointTextField;
    private javax.swing.JLabel minimumCrossoverPointLabel;
    private javax.swing.JTextField minimumCrossoverPointTextField;
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
