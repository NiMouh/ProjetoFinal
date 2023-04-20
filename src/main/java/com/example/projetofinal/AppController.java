package com.example.projetofinal;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.deidentifier.arx.*;
import org.deidentifier.arx.criteria.KAnonymity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class AppController {
    public static Data inputData = SetupController.getData();
    @FXML
    private BorderPane mainPage;
    @FXML
    private VBox homeContent;
    @FXML
    private TextField kMin, kStep, kMax;
    @FXML
    private Button minimizeBtn, closeBtn, maximizeBtn;
    @FXML
    private TableView<String[]> inputTable;
    @FXML
    private ScrollPane tableBox;

    private double xOffset = 0, yOffset = 0;

    private ArrayList<String> statistics = new ArrayList<>();
    private ArrayList<String> risks = new ArrayList<>();

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

        // Table Configuration
        inputTable.setEditable(true);
        setDataTable();

        // Fix table size
        inputTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        inputTable.prefWidthProperty().bind(tableBox.widthProperty());
        inputTable.prefHeightProperty().bind(tableBox.heightProperty());

        // Insert the header in the statistics array
        String statsHeader = makeStatisticHeader();
        statistics.add(statsHeader);

        // Insert the header in the risk array
        String riskHeader = makeRiskHeader();
        risks.add(riskHeader);
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

    public void viewHieararchy() {
        loadPage("hierarchy-screen");
    }

    public void viewStatistics() {
        loadPage("statistics-screen");
    }

    public void viewRisks() {
        loadPage("risk-screen");
    }

    // Function that given a page name, loads the page section
    public void loadPage(String page) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(page + ".fxml")));
            mainPage.setCenter(root);
        } catch (IOException e) {
            System.out.println("Não foi possível carregar a página " + page);
        }
    }

    // Function to calculate the K-Anonymity given the values of kMin, kMax and kStep
    public void calculateKAnonymity() {
        int minimumK = Integer.parseInt(kMin.getText());
        int maximumK = Integer.parseInt(kMax.getText());
        int step = Integer.parseInt(kStep.getText());
        for (int k = minimumK; k <= maximumK; k += step) {
            anonymizeWithK(inputData, k);
        }
        // Save the statistics and risks in CSV files
        saveCSV(statistics, "statistics.csv");
        saveCSV(risks, "risks.csv");

        // Clear the statistics and risks arrays
        statistics.clear();
        risks.clear();
    }


    // Function to anonymize data using K-Anonymity
    public void anonymizeWithK(Data dados, int valorK) {
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration configuration = ARXConfiguration.create();
        configuration.addPrivacyModel(new KAnonymity(valorK));
        configuration.setSuppressionLimit(0.01d); // 1% de linhas suprimidas
        dados.getHandle().release();
        try {
            ARXResult result = anonymizer.anonymize(dados, configuration);
            result.getOutput(false).save("data_anonymity_" + valorK + ".csv", ';'); // Save the anonymized data in a CSV file
            StatisticsAnonimizedData reviewData = new StatisticsAnonimizedData(result.getOutput(false), valorK);
            statistics.add(reviewData.getFullStatistics()); // Save the statistics in the statistics array
            risks.add(reviewData.getRiskMeasures()); // Save the risks in the risks array
        } catch (IOException e) {
            System.out.println("Erro ao anonimizar os dados");
        }
        System.out.println("Dados Anonimizados com sucesso");
    }

    // Function to create the header of the CSV file (statistics.csv)
    public String makeStatisticHeader() {
        return "k;supressed;" + "gen. intensity;missings;entropy;squared error; ;".repeat(Math.max(0, NUMERO_DE_QUASE_IDENTIFICADORES)) +
                "discernibility;avg. class size;row squared error";
    }

    // Function to create the header of the CSV file (risk.csv)
    public String makeRiskHeader() {
        return "k;prosecutor risk;journalist risk;journalist risk";
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
            System.out.println("Os dados foram salvos com sucesso");
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo CSV");
        }
    }

    // Function that given a CSV file, creates the columns and the rows of a table
    public void setDataTable() {

        try {
            Reader reader = Files.newBufferedReader(Paths.get("data.csv"));
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();

            ObservableList<String[]> data = FXCollections.observableArrayList();
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                data.add(nextLine);
            }

            // Clear existing columns
            inputTable.getColumns().clear();

            String[] columnNames = data.get(0);
            data.remove(0);
            for (int index = 0; index < columnNames.length; index++) {
                TableColumn<String[], String> column = new TableColumn<>(columnNames[index]);
                final int columnIndex = index;
                column.setCellValueFactory(cellData -> {
                    String[] row = cellData.getValue();
                    return new SimpleStringProperty(row[columnIndex]);
                });
                column.setCellFactory(tableColumn -> new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            int rowIndex = getIndex();
                            if (rowIndex % 2 == 0) {
                                setStyle("-fx-background-color: #232132; -fx-text-fill: #FFFFFF;");
                            } else {
                                setStyle("-fx-background-color: #2C2B3F; -fx-text-fill: #FFFFFF;");
                            }
                            setText(item);
                        }
                    }
                });
                column.setStyle("-fx-background-color: #FFFFFF;");
                inputTable.getColumns().add(column);
            }


            // Set the items property of the TableView to an ObservableList of the data from the CSV file
            inputTable.setItems(data);
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
