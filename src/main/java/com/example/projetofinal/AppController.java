package com.example.projetofinal;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.deidentifier.arx.*;
import org.deidentifier.arx.criteria.KAnonymity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class AppController {
    @FXML
    private BorderPane mainPage;
    @FXML
    private VBox homeContent,navigationBar;
    @FXML
    private TextField kMin, kStep, kMax;
    @FXML
    private Button minimizeBtn, closeBtn, maximizeBtn;
    @FXML
    private TableView<String[]> inputTable;
    @FXML
    private ScrollPane tableBox;

    private double xOffset = 0, yOffset = 0;

    public Data inputData = SetupController.getData();
    private static ArrayList<String> statistics;
    private static ArrayList<String> risks;
    private final int NUMERO_DE_QUASE_IDENTIFICADORES = inputData.getDefinition().getQuasiIdentifyingAttributes().size();
    public static String filesPath;

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
        setDataTable(inputTable);

        // Fix table size
        inputTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        inputTable.prefWidthProperty().bind(tableBox.widthProperty());
        inputTable.prefHeightProperty().bind(tableBox.heightProperty());

        // Initialize the statistics array and the risks array
        statistics = new ArrayList<>();
        risks = new ArrayList<>();
    }

    // Function that makes the page draggable
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

    // Function that changes the page to the setup page
    public void setupModifications() throws IOException {
        Stage stage = (Stage) mainPage.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("setup-screen.fxml"))));
        stage.setScene(scene);
    }

    // Function that changes the page to the home page
    public void viewModifications() {
        mainPage.setCenter(homeContent);
    }

    // Function that changes the page to the hierarchy page
    public void viewHieararchy() {
        loadPage("hierarchy-screen");
    }

    // Function that changes the page to the statistics page
    public void viewStatistics() {
        loadPage("statistics-screen");
    }

    // Function that changes the page to the risks page
    public void viewRisks() {
        loadPage("risk-screen");
    }

    // Function that changes the page to the differencial privacy page
    public void viewDifferencialPrivacy() {
        loadPage("differencial-privacy-screen");
    }

    // Function that changes the page to the differencial statistics page
    public void viewDifferencialStatistics() {
        loadPage("differencial-statistics-screen");
    }

    // Function that changes the page to the differencial risks page
    public void viewDifferencialRisks() {
        loadPage("differencial-risk-screen");
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

    // Function that handles the burger menu
    public void handleBurgerMenu() {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), navigationBar);

        if (navigationBar.isVisible()) {
            transition.setToX(-navigationBar.getWidth());
            transition.setOnFinished(event -> {
                navigationBar.setVisible(false);
                navigationBar.setManaged(false);
            });
        } else {
            navigationBar.setVisible(true);
            navigationBar.setManaged(true);
            transition.setToX(0);
        }

        transition.play();
    }

    // Function to calculate the K-Anonymity given the values of kMin, kMax and kStep
    public void calculateKAnonymity() {
        // If the textfields are empty, show an error message
        if (kMin.getText().isEmpty() || kMax.getText().isEmpty() || kStep.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error calculating K-Anonymity");
            alert.setContentText("The values of K parameters are empty. Please, try again.");
            alert.showAndWait();
            return;
        }

        int minimumK = Integer.parseInt(kMin.getText());
        int maximumK = Integer.parseInt(kMax.getText());
        int step = Integer.parseInt(kStep.getText());

        // If the values are invalid, show an error message
        if (minimumK < 2 || maximumK < minimumK || step < 1 || step > maximumK) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error calculating K-Anonymity");
            alert.setContentText("The values of kMin, kMax and kStep are invalid. Please, try again.");
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

        // For each value of k, create a thread to anonymize the data
        for (int k = minimumK; k <= maximumK; k += step) {
            anonymizeWithK(inputData, k);
        }

        // Show a success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Success anonymizing data");
        alert.setContentText("The data was anonymized successfully.");
        alert.showAndWait();

        // Save the statistics and risks in CSV files
        saveCSV(statistics, filesPath + "/statistics.csv");
        saveCSV(risks, filesPath + "/risks.csv");

        // Clear the statistics and risks arrays
        statistics.clear();
        risks.clear();
    }

    // Function to anonymize data using K-Anonymity
    public void anonymizeWithK(Data dados, int valorK) {
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration configuration = ARXConfiguration.create();
        configuration.addPrivacyModel(new KAnonymity(valorK));
        configuration.setSuppressionLimit(1); // 100% de linhas suprimidas
        dados.getHandle().release();
        try {
            ARXResult result = anonymizer.anonymize(dados, configuration);
            String firstQuasiIdentifier = inputData.getDefinition().getQuasiIdentifyingAttributes().iterator().next();
            DataHandle handle = result.getOutput(false);
            handle.sort(true, dados.getHandle().getColumnIndexOf(firstQuasiIdentifier));
            handle.save(filesPath + "/data_anonymity_" + valorK + ".csv", SetupController.delimiter); // Save the anonymized data in a CSV file
            StatisticsAnonimizedData reviewData = new StatisticsAnonimizedData(result.getOutput(false), valorK);
            statistics.add(reviewData.getFullStatistics()); // Save the statistics in the statistics array
            risks.add(reviewData.getRiskMeasures()); // Save the risks in the risks array
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Dados Anonimizados com sucesso");
    }

    // Function to create the header of the CSV file (statistics.csv)
    public String makeStatisticHeader() {
        return "k;supressed;" + "gen. intensity;missings;entropy;squared error; ;".repeat(NUMERO_DE_QUASE_IDENTIFICADORES) +
                "discernibility;avg. class size;row squared error";
    }

    // Function to create the header of the CSV file (risk.csv)
    public String makeRiskHeader() {
        return "k;prosecutor risk;journalist risk;marketer risk";
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
            System.out.println("Erro ao salvar arquivo CSV");
        }
    }

    // Function that opens file explorer to create a new folder and save path in filePath variable
    public void createFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(mainPage.getScene().getWindow());
        if (selectedDirectory != null) {
            filesPath = selectedDirectory.getAbsolutePath();
        }
    }

    // Function that given a CSV file, creates the columns and the rows of a table
    public static void setDataTable(TableView<String[]> table) {

        try {
            Reader reader = Files.newBufferedReader(Paths.get(SetupController.DATA_SOURCE_PATH));
            CSVParser parser = new CSVParserBuilder().withSeparator(SetupController.delimiter).build();
            CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();

            ObservableList<String[]> data = FXCollections.observableArrayList();
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                data.add(nextLine);
            }

            // Clear existing columns
            table.getColumns().clear();

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
                table.getColumns().add(column);
            }


            // Set the items property of the TableView to an ObservableList of the data from the CSV file
            table.setItems(data);
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
