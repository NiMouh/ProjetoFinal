package com.example.projetofinal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.ArrayList;

public class DifferencialPrivacyController {

    @FXML
    private TableView<String[]> inputTable;
    @FXML
    private ScrollPane tableBox;

    @FXML
    private TextField minEpsilonField, maxEpsilonField, fixedDeltaField, minDeltaField, maxDeltaField, fixedEpsilonField;

    public ToggleGroup vary_group;
    private ArrayList<String> statistics;
    private ArrayList<String> risks;

    public static String diferentialfilesPath;

    public void initialize() {
        // Only allow numbers to be typed in the epsilonField and deltaField text fields
        minEpsilonField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));
        maxEpsilonField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));
        fixedDeltaField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));
        minDeltaField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));
        maxDeltaField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));
        fixedEpsilonField.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));

        // Table Configuration
        inputTable.setEditable(true);
        AppController.setDataTable(inputTable);

        // Fix table size
        inputTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        inputTable.prefWidthProperty().bind(tableBox.widthProperty());
        inputTable.prefHeightProperty().bind(tableBox.heightProperty());

        // Check what option is selected in toggle group and hide the respective fields
        vary_group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getUserData().equals("Vary Epsilon")) {
                minEpsilonField.setVisible(true);
                maxEpsilonField.setVisible(true);
                fixedDeltaField.setVisible(true);
                minDeltaField.setVisible(false);
                maxDeltaField.setVisible(false);
                fixedEpsilonField.setVisible(false);
            } else {
                minEpsilonField.setVisible(false);
                maxEpsilonField.setVisible(false);
                fixedDeltaField.setVisible(false);
                minDeltaField.setVisible(true);
                maxDeltaField.setVisible(true);
                fixedEpsilonField.setVisible(true);
            }
        });

        // Initialize statistics and risks
        statistics = new ArrayList<>();
        risks = new ArrayList<>();
    }

    public void calculateDifferencialPrivacy() {
        if (vary_group.getSelectedToggle().getUserData().equals("Vary Epsilon")) {
            // TODO: Vary Epsilon
            // If the textfields are empty, show an error message
            if (minEpsilonField.getText().isEmpty() || maxEpsilonField.getText().isEmpty() || fixedDeltaField.getText().isEmpty()) {
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

            // If the values are invalid, show an error message
            if (minEpsilon > maxEpsilon || minEpsilon < 0.01 || maxEpsilon > 10 || fixedDelta > 0.01 || fixedDelta < 0.00001) {
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
            String statsHeader = makeStatisticHeader(); // TODO
            statistics.add(statsHeader);

            // Insert the header in the risk array
            String riskHeader = makeRiskHeader(); // TODO
            risks.add(riskHeader);

            for (double epsilon = minEpsilon; epsilon <= maxEpsilon; epsilon += 1) {
                // anonymizeWithDifferencialPrivacy(epsilon, fixedDelta);
            }

        } else {
            // TODO: Vary Delta
            // If the textfields are empty, show an error message
            if (minDeltaField.getText().isEmpty() || maxDeltaField.getText().isEmpty() || fixedEpsilonField.getText().isEmpty()) {
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

            // If the values are invalid, show an error message
            if (minDelta > maxDelta || minDelta < 0.00001 || maxDelta > 0.01 || fixedEpsilon > 10 || fixedEpsilon < 0.01) {
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
            String statsHeader = makeStatisticHeader(); // TODO
            statistics.add(statsHeader);

            // Insert the header in the risk array
            String riskHeader = makeRiskHeader(); // TODO
            risks.add(riskHeader);

            for (double delta = minDelta; delta <= maxDelta; delta += 0.0001) {
                // anonymizeWithDifferencialPrivacy(fixedEpsilon, delta);
            }
        }

        // Show a success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Success anonymizing data");
        alert.setContentText("The data was anonymized successfully.");
        alert.showAndWait();

        // Save the statistics and risks in CSV files
        // saveCSV(statistics, filesPath + "/statistics.csv");
        // saveCSV(risks, filesPath + "/risks.csv");

        // Clear statistics and risks
        statistics.clear();
        risks.clear();
    }

    // TODO: anonymizeWithDifferencialPrivacy(epsilon, fixedDelta)

    // Function to create the header of the CSV file (statistics.csv)
    public String makeStatisticHeader() {
        return "k;supressed;" + "gen. intensity;missings;entropy;squared error; ;".repeat(1) +
                "discernibility;avg. class size;row squared error";
    }

    // Function to create the header of the CSV file (risk.csv)
    public String makeRiskHeader() {
        return "k;prosecutor risk;journalist risk;marketer risk";
    }

    // Function that opens file explorer to create a new folder and save path in filePath variable
    public void createFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(tableBox.getScene().getWindow());
        if (selectedDirectory != null) {
            diferentialfilesPath = selectedDirectory.getAbsolutePath();
        }
    }
}
