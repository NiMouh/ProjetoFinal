package com.example.projetofinal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import org.deidentifier.arx.*;
import org.deidentifier.arx.criteria.EDDifferentialPrivacy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DifferencialPrivacyController {

    @FXML
    private TableView<String[]> inputTable;
    @FXML
    private ScrollPane tableBox;

    @FXML
    private TextField minEpsilonField, maxEpsilonField, fixedDeltaField, minDeltaField, stepDeltaField, stepEpsilonField, maxDeltaField, fixedEpsilonField;

    public ToggleGroup vary_group;
    private ArrayList<String> statistics;
    private ArrayList<String> risks;

    public Data inputData = SetupController.getData();
    private final int NUMBER_OF_QUASE_IDENTIFIERS = inputData.getDefinition().getQuasiIdentifyingAttributes().size();
    public static String diferentialfilesPath;

    public void initialize() {
        // Only allow numbers to be typed in the epsilonField and deltaField text fields
        minEpsilonField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*\\.?\\d*") ? change : null));
        maxEpsilonField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*\\.?\\d*") ? change : null));
        fixedDeltaField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*\\.?\\d*") ? change : null));
        stepEpsilonField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*\\.?\\d*") ? change : null));
        minDeltaField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*\\.?\\d*") ? change : null));
        maxDeltaField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*\\.?\\d*") ? change : null));
        fixedEpsilonField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*\\.?\\d*") ? change : null));
        stepDeltaField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*\\.?\\d*") ? change : null));

        // Table Configuration
        inputTable.setEditable(true);
        AppController.setDataTable(inputTable);

        // Fix table size
        inputTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        inputTable.prefWidthProperty().bind(tableBox.widthProperty());
        inputTable.prefHeightProperty().bind(tableBox.heightProperty());

        // Check what option is selected in toggle group and hide the respective fields
        vary_group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                String toggleText = selectedRadioButton.getText(); // Get the text of the selected toggle

                if (toggleText.equals("Vary Epsilon")) {
                    // Disable epsilon fields, enable delta fields
                    minEpsilonField.setEditable(true);
                    maxEpsilonField.setEditable(true);
                    fixedDeltaField.setEditable(true);
                    stepEpsilonField.setEditable(true);
                    minDeltaField.setEditable(false);
                    maxDeltaField.setEditable(false);
                    fixedEpsilonField.setEditable(false);
                    minDeltaField.clear();
                    maxDeltaField.clear();
                    fixedEpsilonField.clear();
                    stepDeltaField.clear();
                } else if (toggleText.equals("Vary Delta")) {
                    // Enable epsilon fields, disable delta fields
                    minEpsilonField.setEditable(false);
                    maxEpsilonField.setEditable(false);
                    fixedDeltaField.setEditable(false);
                    stepEpsilonField.setEditable(false);
                    minEpsilonField.clear();
                    maxEpsilonField.clear();
                    fixedDeltaField.clear();
                    stepEpsilonField.clear();
                    minDeltaField.setEditable(true);
                    maxDeltaField.setEditable(true);
                    fixedEpsilonField.setEditable(true);
                    stepDeltaField.setEditable(true);
                }
            }
        });


        // Initialize statistics and risks
        statistics = new ArrayList<>();
        risks = new ArrayList<>();
    }

    public void calculateDifferencialPrivacy() {
        RadioButton selectedRadioButton = (RadioButton) vary_group.getSelectedToggle();
        String selectedText = selectedRadioButton.getText();
        if (selectedText.equals("Vary Epsilon")) {
            // If the textfields are empty, show an error message
            if (minEpsilonField.getText().isEmpty() || maxEpsilonField.getText().isEmpty() || fixedDeltaField.getText().isEmpty() || stepEpsilonField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Empty Fields");
                alert.setContentText("Please fill all the fields.");
                alert.showAndWait();
                return;
            }

            double minEpsilon = Double.parseDouble(minEpsilonField.getText());
            double maxEpsilon = Double.parseDouble(maxEpsilonField.getText());
            double fixedDelta = Double.parseDouble(fixedDeltaField.getText());
            double stepEpsilon = Double.parseDouble(stepEpsilonField.getText());

            // If the values are invalid, show an error message
            if (minEpsilon > maxEpsilon || minEpsilon < 0.01 || maxEpsilon > 10 || fixedDelta > 0.01 || fixedDelta < 0.00001|| stepEpsilon > maxEpsilon) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Values");
                alert.setContentText("Please enter valid values.");
                alert.showAndWait();
                return;
            }

            // Create the folder to save the files
            createFolder();

            // Insert the header in the statistics array
            String statsHeader = makeStatisticHeader();
            statistics.add(statsHeader);

            // Insert the header in the risk array
            String riskHeader = makeRiskHeader();
            risks.add(riskHeader);

            for (double epsilon = minEpsilon; epsilon <= maxEpsilon; epsilon += stepEpsilon) {
                anonymizeWithDifferencialPrivacy(inputData, epsilon, fixedDelta);
            }

        } else {
            // If the textfields are empty, show an error message
            if (minDeltaField.getText().isEmpty() || maxDeltaField.getText().isEmpty() || fixedEpsilonField.getText().isEmpty() || stepDeltaField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Empty Fields");
                alert.setContentText("Please fill all the fields.");
                alert.showAndWait();
                return;
            }

            double minDelta = Double.parseDouble(minDeltaField.getText());
            double maxDelta = Double.parseDouble(maxDeltaField.getText());
            double fixedEpsilon = Double.parseDouble(fixedEpsilonField.getText());
            double stepDelta = Double.parseDouble(stepDeltaField.getText());

            // If the values are invalid, show an error message
            if (minDelta > maxDelta || minDelta < 0.00001 || maxDelta > 0.01 || fixedEpsilon > 10 || fixedEpsilon < 0.01 || stepDelta > maxDelta) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Values");
                alert.setContentText("Please enter valid values.");
                alert.showAndWait();
                return;
            }

            // Create the folder to save the files
            createFolder();

            // Insert the header in the statistics array
            String statsHeader = makeStatisticHeader();
            statistics.add(statsHeader);

            // Insert the header in the risk array
            String riskHeader = makeRiskHeader();
            risks.add(riskHeader);

            for (double delta = minDelta; delta <= maxDelta; delta += stepDelta) {
                anonymizeWithDifferencialPrivacy(inputData, fixedEpsilon, delta);
            }
        }

        // Show a success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Success anonymizing data");
        alert.setContentText("The data was anonymized successfully.");
        alert.showAndWait();

        // Save the statistics and risks in CSV files
        saveCSV(statistics, diferentialfilesPath + "/diferencial_statistics.csv");
        saveCSV(risks, diferentialfilesPath + "/diferencial_risks.csv");

        // Clear statistics and risks
        statistics.clear();
        risks.clear();
    }

    private void anonymizeWithDifferencialPrivacy(Data data, double epsilon, double delta) {
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration configuration = ARXConfiguration.create();
        configuration.addPrivacyModel(new EDDifferentialPrivacy(epsilon, delta));
        configuration.setHeuristicSearchStepLimit(1000); // Limit the number of iterations
        configuration.setSuppressionLimit(1); // 100% de linhas suprimidas
        data.getHandle().release();
        try {
            ARXResult result = anonymizer.anonymize(data, configuration);
            String firstQuasiIdentifier = data.getDefinition().getQuasiIdentifyingAttributes().iterator().next();
            DataHandle handle = result.getOutput(false);
            if (handle != null){
                handle.sort(true, data.getHandle().getColumnIndexOf(firstQuasiIdentifier));
                handle.save(diferentialfilesPath + "/data_differential_epsilon" + epsilon + "_delta" + delta + ".csv", SetupController.delimiter); // Save the anonymized data in a CSV file
            }
            StatisticsAnonimizedData reviewData = new StatisticsAnonimizedData(result.getOutput(false), epsilon, delta);
            statistics.add(reviewData.getFullStatisticsDiferencial()); // Save the statistics in the statistics array
            risks.add(reviewData.getRiskMeasuresDiferencial()); // Save the risks in the risks array
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Function to create the header of the CSV file (statistics.csv)
    public String makeStatisticHeader() {
        return "epsilon;delta;supressed;" + "gen. intensity;missings;entropy;squared error; ;".repeat(NUMBER_OF_QUASE_IDENTIFIERS) +
                "discernibility;avg. class size;row squared error";
    }

    // Function to create the header of the CSV file (risk.csv)
    public String makeRiskHeader() {
        return "epsilon;delta;prosecutor risk;journalist risk;marketer risk";
    }

    // Function that opens file explorer to create a new folder and save path in filePath variable
    public void createFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(tableBox.getScene().getWindow());
        if (selectedDirectory != null) {
            diferentialfilesPath = selectedDirectory.getAbsolutePath();
        }
    }

    // Function that given a bidimensional array of strings, save in a CSV file
    public void saveCSV(ArrayList<String> data, String path) {
        File file = new File(path);
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            for (String row : data) {
                bw.write(row);
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error saving CSV file");
            alert.setContentText("An error occurred while saving the CSV file.");
            alert.showAndWait();
        }
    }
}
