package com.example.projetofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.deidentifier.arx.*;
import org.deidentifier.arx.criteria.KAnonymity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class AppController {
    private final Data inputData = SetupController.inputData;
    @FXML
    private BorderPane mainPage;
    @FXML
    private VBox homeContent;
    @FXML
    private TextField kValue, kStep;

    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize() {
        // Only allow numbers to be typed in the kValue and kStep text fields
        kValue.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                kValue.setText(newValue.replaceAll("\\D", ""));
            }
        });
        kStep.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                kStep.setText(newValue.replaceAll("\\D", ""));
            }
        });
        makeStageDraggable();
    }

    @FXML
    protected void makeStageDraggable() {
        mainPage.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        mainPage.setOnMouseDragged(event -> {
            Stage stage = (Stage) mainPage.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
            stage.setOpacity(0.8f);
        });

        mainPage.setOnDragDone(event -> {
            Stage stage = (Stage) mainPage.getScene().getWindow();
            stage.setOpacity(1.0f);
        });

        mainPage.setOnMouseReleased(event -> {
            Stage stage = (Stage) mainPage.getScene().getWindow();
            stage.setOpacity(1.0f);
        });
    }

    public void viewModifications(MouseEvent event) {
        mainPage.setCenter(homeContent);
    }

    public void loadPage(String pageName) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(pageName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPage.setCenter(root);
    }

    public void calculateKAnonymity(MouseEvent event) {
        int k = Integer.parseInt(kValue.getText());
        int step = Integer.parseInt(kStep.getText());
        for (int index = step; index <= k; index += step) {
            try {
                DataHandle anonimizedData = anonimizeWithK(inputData, index);
                System.out.println(anonimizedData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Function to anonymize data using K-Anonymity
    public DataHandle anonimizeWithK(Data dados, int valorK) throws IOException {
        ARXAnonymizer anonimizer = new ARXAnonymizer();
        ARXConfiguration configuration = ARXConfiguration.create();
        configuration.addPrivacyModel(new KAnonymity(valorK));
        configuration.setSuppressionLimit(0.01d); // 1% de linhas suprimidas
        ARXResult result = anonimizer.anonymize(dados, configuration);
        return result.getOutput();
    }

    // Functions that given the data and the k fold used, it will make the statistics and save it in a CSV file
    public void saveStatistics(DataHandle[] data, int k, int step) {
        int sizeOfstatistics = k/step;
        String[] statistics = new String[sizeOfstatistics];
        statistics[0] = "k;precision;recall;specificity;f1"; // Header of the CSV file
        for (int index = 1; index <= k; index += step) {
            StatisticsAnonimizedData statisticsAnonimizedData = new StatisticsAnonimizedData(data[index]);
            statistics[index] = statisticsAnonimizedData.toString();
        }
        saveCSV(statistics, "statistics.csv");
    }

    // Function that given a bidimensional array of strings, save in a CSV file
    public void saveCSV(String[] data, String path) {
        File file = new File(path);
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            for (String row : data) {
                bw.write(row);
                bw.newLine();
            }

            bw.close();
            System.out.println("Data saved to CSV successfully.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo CSV");
        }
    }
}
