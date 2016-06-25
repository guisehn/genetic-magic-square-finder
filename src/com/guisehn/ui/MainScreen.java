package com.guisehn.ui;

import com.guisehn.main.MagicSquareFinder;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private PrintWriter printWriter;
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
    
    private void generateFileWriter() {
        closeFileWriter();
        
        File logsFolder = new File("logs");
        
        if (!logsFolder.exists()) {
            logsFolder.mkdir();
        }
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        
        try {
            String fileName = dateFormat.format(new Date()) + ".txt";
            printWriter = new PrintWriter("logs/" + fileName, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void appendToFile(String str) {
        if (printWriter != null) {
            printWriter.append(str);
        }
    }
    
    private void closeFileWriter() {
        if (printWriter != null) {
            printWriter.close();
            printWriter = null;
        }
    }
    
    private void startFinder(int size, int populationSize, int eliteSize, 
            int eliteDeathPeriod, double mutationProbability,
            boolean allowDuplicates, int minimumCrossoverPoint,
            int maximumCrossoverPoint, boolean showGenerationDetails) {
        amountFound = 0;
        
        generationLogTextArea.setText("");

        foundSquaresTextArea.setText("");
        foundSquaresTextArea.setTabSize(((int)Math.pow(size, 2) + "").length());
        
        if (finder != null) {
            finder.stop();
        }
        
        if (saveToFileCheckBox.isSelected()) {
            generateFileWriter();
        }
        
        finder = new MagicSquareFinder(size, populationSize, eliteSize,
            eliteDeathPeriod, mutationProbability, allowDuplicates,
            minimumCrossoverPoint, maximumCrossoverPoint, showGenerationDetails,
            (ActionEvent e) -> {
                final int eventType = e.getID();

                try {
                    String textToAppend = e.getActionCommand();

                    switch (eventType) {
                        case MagicSquareFinder.LOG_EVENT:
                            if (clearLogCheckBox.isSelected()) {
                                generationLogTextArea.setText("");
                            }

                            appendToFile(textToAppend);
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
                            closeFileWriter();
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
        populationPanel = new javax.swing.JPanel();
        squareSizeLabel = new javax.swing.JLabel();
        squareSizeTextField = new javax.swing.JTextField();
        populationSizeLabel = new javax.swing.JLabel();
        populationSizeTextField = new javax.swing.JTextField();
        allowDuplicatesCheckBox = new javax.swing.JCheckBox();
        elitismPanel = new javax.swing.JPanel();
        eliteSizeLabel = new javax.swing.JLabel();
        eliteSizeTextField = new javax.swing.JTextField();
        eliteDeathPeriodLabel = new javax.swing.JLabel();
        eliteDeathPeriodTextField = new javax.swing.JTextField();
        crossoverPanel = new javax.swing.JPanel();
        mutationProbabilityTextField = new javax.swing.JTextField();
        mutationProbabilityLabel = new javax.swing.JLabel();
        minimumCrossoverPointLabel = new javax.swing.JLabel();
        minimumCrossoverPointTextField = new javax.swing.JTextField();
        maximumCrossoverPointTextField = new javax.swing.JTextField();
        maximumCrossoverPointLabel = new javax.swing.JLabel();
        outputPanel = new javax.swing.JPanel();
        showGenerationDetailsCheckBox = new javax.swing.JCheckBox();
        saveToFileCheckBox = new javax.swing.JCheckBox();
        menuBar = new javax.swing.JMenuBar();
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

        populationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("População"));
        populationPanel.setToolTipText("");

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
        populationSizeTextField.setNextFocusableComponent(allowDuplicatesCheckBox);

        allowDuplicatesCheckBox.setText("Permitir indivíduos idênticos");
        allowDuplicatesCheckBox.setToolTipText("");
        allowDuplicatesCheckBox.setNextFocusableComponent(eliteSizeTextField);

        javax.swing.GroupLayout populationPanelLayout = new javax.swing.GroupLayout(populationPanel);
        populationPanel.setLayout(populationPanelLayout);
        populationPanelLayout.setHorizontalGroup(
            populationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(populationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(populationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(populationPanelLayout.createSequentialGroup()
                        .addComponent(squareSizeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(squareSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(populationPanelLayout.createSequentialGroup()
                        .addComponent(populationSizeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(populationSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(populationPanelLayout.createSequentialGroup()
                .addComponent(allowDuplicatesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        populationPanelLayout.setVerticalGroup(
            populationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(populationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(populationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(squareSizeLabel)
                    .addComponent(squareSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(populationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(populationSizeLabel)
                    .addComponent(populationSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(allowDuplicatesCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        elitismPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Elitismo"));

        eliteSizeLabel.setText("Tamanho da elite:");

        eliteSizeTextField.setText("180");
        eliteSizeTextField.setToolTipText("");
        eliteSizeTextField.setNextFocusableComponent(eliteDeathPeriodTextField);

        eliteDeathPeriodLabel.setText("Período de morte da elite:");

        eliteDeathPeriodTextField.setText("15000");
        eliteDeathPeriodTextField.setToolTipText("");
        eliteDeathPeriodTextField.setNextFocusableComponent(minimumCrossoverPointTextField);

        javax.swing.GroupLayout elitismPanelLayout = new javax.swing.GroupLayout(elitismPanel);
        elitismPanel.setLayout(elitismPanelLayout);
        elitismPanelLayout.setHorizontalGroup(
            elitismPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(elitismPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(elitismPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(elitismPanelLayout.createSequentialGroup()
                        .addComponent(eliteSizeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(eliteSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(elitismPanelLayout.createSequentialGroup()
                        .addComponent(eliteDeathPeriodLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(eliteDeathPeriodTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        elitismPanelLayout.setVerticalGroup(
            elitismPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(elitismPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(elitismPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eliteSizeLabel)
                    .addComponent(eliteSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(elitismPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eliteDeathPeriodLabel)
                    .addComponent(eliteDeathPeriodTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        crossoverPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Cruzamento"));
        crossoverPanel.setToolTipText("");

        mutationProbabilityTextField.setText("50");
        mutationProbabilityTextField.setToolTipText("");
        mutationProbabilityTextField.setNextFocusableComponent(showGenerationDetailsCheckBox);

        mutationProbabilityLabel.setText("Chance de mutação (%):");

        minimumCrossoverPointLabel.setText("Ponto mínimo:");

        minimumCrossoverPointTextField.setToolTipText("");
        minimumCrossoverPointTextField.setNextFocusableComponent(maximumCrossoverPointTextField);

        maximumCrossoverPointTextField.setToolTipText("");
        maximumCrossoverPointTextField.setNextFocusableComponent(mutationProbabilityTextField);

        maximumCrossoverPointLabel.setText("Ponto máximo:");

        javax.swing.GroupLayout crossoverPanelLayout = new javax.swing.GroupLayout(crossoverPanel);
        crossoverPanel.setLayout(crossoverPanelLayout);
        crossoverPanelLayout.setHorizontalGroup(
            crossoverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(crossoverPanelLayout.createSequentialGroup()
                .addGroup(crossoverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(crossoverPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(crossoverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(maximumCrossoverPointLabel)
                            .addComponent(minimumCrossoverPointLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, crossoverPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mutationProbabilityLabel)
                        .addGap(18, 18, 18)))
                .addGroup(crossoverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(minimumCrossoverPointTextField)
                    .addComponent(maximumCrossoverPointTextField)
                    .addComponent(mutationProbabilityTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                .addContainerGap())
        );
        crossoverPanelLayout.setVerticalGroup(
            crossoverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(crossoverPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(crossoverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minimumCrossoverPointLabel)
                    .addComponent(minimumCrossoverPointTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(crossoverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maximumCrossoverPointLabel)
                    .addComponent(maximumCrossoverPointTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(crossoverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mutationProbabilityLabel)
                    .addComponent(mutationProbabilityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );

        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Saída"));
        outputPanel.setToolTipText("");

        showGenerationDetailsCheckBox.setText("Exibir histórico completo");

        saveToFileCheckBox.setText("Gravar histórico completo em arquivo");

        javax.swing.GroupLayout outputPanelLayout = new javax.swing.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(showGenerationDetailsCheckBox)
            .addComponent(saveToFileCheckBox)
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(showGenerationDetailsCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveToFileCheckBox)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jMenu1.setText("Ajuda");

        aboutMenuItem.setText("Sobre o programa");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(aboutMenuItem);

        menuBar.add(jMenu1);

        setJMenuBar(menuBar);

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
                                .addComponent(populationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(elitismPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(crossoverPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(outputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(foundSquaresLabel)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(generationHistoryLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 606, Short.MAX_VALUE))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(populationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(elitismPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(crossoverPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(outputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        boolean showGenerationDetails = showGenerationDetailsCheckBox.isSelected();
        
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
            minimumCrossoverPoint, maximumCrossoverPoint, showGenerationDetails);
    }//GEN-LAST:event_startButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        closeFileWriter();
        
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
    private javax.swing.JPanel crossoverPanel;
    private javax.swing.JLabel eliteDeathPeriodLabel;
    private javax.swing.JTextField eliteDeathPeriodTextField;
    private javax.swing.JLabel eliteSizeLabel;
    private javax.swing.JTextField eliteSizeTextField;
    private javax.swing.JPanel elitismPanel;
    private javax.swing.JLabel foundSquaresLabel;
    private javax.swing.JTextArea foundSquaresTextArea;
    private javax.swing.JLabel generationCountLabel;
    private javax.swing.JLabel generationHistoryLabel;
    private javax.swing.JTextArea generationLogTextArea;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel maximumCrossoverPointLabel;
    private javax.swing.JTextField maximumCrossoverPointTextField;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel minimumCrossoverPointLabel;
    private javax.swing.JTextField minimumCrossoverPointTextField;
    private javax.swing.JLabel mutationProbabilityLabel;
    private javax.swing.JTextField mutationProbabilityTextField;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JPanel populationPanel;
    private javax.swing.JLabel populationSizeLabel;
    private javax.swing.JTextField populationSizeTextField;
    private javax.swing.JCheckBox saveToFileCheckBox;
    private javax.swing.JCheckBox showGenerationDetailsCheckBox;
    private javax.swing.JLabel squareSizeLabel;
    private javax.swing.JTextField squareSizeTextField;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables
}
