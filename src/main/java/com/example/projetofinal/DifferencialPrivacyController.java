package com.example.projetofinal;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class DifferencialPrivacyController {

    @FXML
    private TableView<String[]> inputTable;
    @FXML
    private ScrollPane tableBox;

    @FXML
    private TextField epsilonValue, deltaValue;

    private ArrayList<String> statistics;
    private ArrayList<String> risks;

    public void initialize() {
        // Table Configuration
        inputTable.setEditable(true);
        inputTable.prefWidthProperty().bind(tableBox.widthProperty());
        inputTable.prefHeightProperty().bind(tableBox.heightProperty());
        AppController.setDataTable(inputTable);

        // Initialize statistics and risks
        statistics = new ArrayList<>();
        risks = new ArrayList<>();
    }

    public void calculateDifferencialPrivacy(){
        // Get the values of epsilon and delta
        double epsilon = Double.parseDouble(epsilonValue.getText());
        double delta = Double.parseDouble(deltaValue.getText());

        // Hadd header to the statistics and risks

        // Calculate the differencial privacy (Class EDDifferentialPrivacy)

        // Get the statistics and risks
        // statistics = differencialPrivacy.getStatistics();
        // risks = differencialPrivacy.getRisks();

        // Clear statistics and risks
        statistics.clear();
        risks.clear();
    }
}
