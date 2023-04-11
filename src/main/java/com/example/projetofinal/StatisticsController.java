package com.example.projetofinal;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class StatisticsController {

    @FXML
    private TableView<String[]> inputTable;
    @FXML
    private ScrollPane tableBox;
    @FXML
    private VBox messageBox;
    @FXML
    private Button searchStats;

    final FileChooser fileChooser = new FileChooser();

    public void initialize() {
        ObservableList<String[]> data = readCSV("statistics.csv");

        if (data == null) { // If it's null, it will hide the table and show a message
            tableBox.setVisible(false);
            messageBox.setVisible(true);
        } else { // If it's not null, it will show the table and show the statistics
            tableBox.setVisible(true);
            messageBox.setVisible(false);
            statisticsWindow(data);
        }

        searchStats.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                tableBox.setVisible(true);
                messageBox.setVisible(false);
                ObservableList<String[]> searched = readCSV(file.getAbsolutePath());
                statisticsWindow(searched);
            }
        });
    }

    public void statisticsWindow(ObservableList<String[]> data) {
        if (data == null) {
            return;
        }

        // Clear existing columns
        inputTable.getColumns().clear();

        String[] header = data.get(0);
        for (int index = 0; index < header.length; index++) {
            TableColumn<String[], String> column = new TableColumn<>(header[index]);
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

        // Set the data to the table
        inputTable.setItems(data);
    }

    public ObservableList<String[]> readCSV(String path) {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(path));
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                data.add(nextLine);
            }
        } catch (NoSuchFileException e) {
            // Handle the exception gracefully, for example by displaying a message to the user
            System.err.println("File not found: " + path);
        } catch (IOException e) {
            // Handle any other IO exceptions in a similar way
            System.err.println("An IO exception occurred while reading the file: " + e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            System.err.println("An unexpected exception occurred: " + e.getMessage());
        }
        return data;
    }
}
