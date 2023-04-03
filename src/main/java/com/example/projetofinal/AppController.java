package com.example.projetofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.deidentifier.arx.*;
import org.deidentifier.arx.criteria.KAnonymity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AppController {
    private final Data inputData = SetupController.inputData;
    @FXML
    private BorderPane mainPage;
    @FXML
    private VBox homeContent;
    @FXML
    private TextField kMin, kStep, kMax;
    @FXML
    private Button minimizeBtn, closeBtn, maximizeBtn;
    @FXML
    private TableView<String> inputTable;

    private double xOffset = 0, yOffset = 0;

    private ArrayList<String> statistics;

    private final int NUMERO_DE_QUASE_IDENTIFICADORES = inputData.getDefinition().getQuasiIdentifyingAttributes().size();

    public void initialize() {
        // Only allow numbers to be typed in the kValue and kStep text fields
        kStep.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));
        kMin.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));
        kMax.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));

        // Header Configuration
        makeStageDraggable();
        closeBtn.setOnMouseClicked(SetupController::onCloseButtonClick);
        minimizeBtn.setOnMouseClicked(SetupController::onMinimizeButtonClick);
        maximizeBtn.setOnMouseClicked(SetupController::onMaximizeButtonClick);

        // Insert the header in the statistics array
        String header = makeHeader();
        statistics.add(header);
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

    public void viewModifications() {
        mainPage.setCenter(homeContent);
    }

    // Function to calculate the K-Anonymity given the values of kMin, kMax and kStep
    public void calculateKAnonymity() throws IOException {
        int minimumK = Integer.parseInt(kMin.getText());
        int maximumK = Integer.parseInt(kMax.getText());
        int step = Integer.parseInt(kStep.getText());
        for (int k = minimumK; k <= maximumK; k += step) {
            DataHandle anonimizedData = anonymizeWithK(inputData, k);
            StatisticsAnonimizedData stats = new StatisticsAnonimizedData(anonimizedData);
            statistics.add(stats.toString());
        }
        saveCSV(statistics, "src/main/resources/statistics.csv");
    }


    // Function to anonymize data using K-Anonymity
    public DataHandle anonymizeWithK(Data dados, int valorK) throws IOException {
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration configuration = ARXConfiguration.create();
        configuration.addPrivacyModel(new KAnonymity(valorK));
        configuration.setSuppressionLimit(0.01d); // 1% de linhas suprimidas
        ARXResult result = anonymizer.anonymize(dados, configuration);
        return result.getOutput();
    }

    // Function to create the header of the CSV file
    public String makeHeader() {
        return "k;" + "gen. intensity;missings;entropy;squared error; ;".repeat(Math.max(0, NUMERO_DE_QUASE_IDENTIFICADORES)) +
                "discernibility;avg. class size;row squared error;prosecutor risk;journalist risk;marketer risk";
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
            System.out.println("Data saved to CSV successfully.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo CSV");
        }
    }

    public void setDataTable() {
        // TODO: Implementar
    }
}
