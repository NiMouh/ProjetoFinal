package com.example.projetofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

// ARX imports
import org.deidentifier.arx.*;

public class SetupController {
    @FXML
    private Label setupTitle;
    @FXML
    private Button setupButton;
    final FileChooser fileChooser = new FileChooser();
    private Data inputData;
    private RadioButton[][] setupButtons;
    @FXML
    private HBox setupButtonContainer;
    private static final String[] dataTypes = {"String", "Integer", "Ordinal", "Date", "Decimal"};

    private static final String[] attributeTypes = {"Identifying", "Sensitive", "Not Sensitive", "Quasi-identifying", "Suppressed"};
    private static final int NUMERO_TIPOS = 5;

    public void initialize() {
        setupButton.setOnAction(event -> {
            if (setupButton.getText().equals("SRC")) { // Open search file window
                File file = fileChooser.showOpenDialog(new Stage());
                if (file != null) {
                    importData(file.getAbsolutePath());
                    dataTypeWindow();
                }
            } else if (setupButton.getText().equals("CONTINUE")) { // Defining data types
                defineDataTypes();
                attributeTypeWindow();

            } else if (setupButton.getText().equals("ACCEPT")) { // Defining attribute types
                defineAttributeTypes();
                newWindow();
            }
        });
    }

    // Function that if the button is pressed the window closes
    @FXML
    protected void onCloseButtonClick() {
        System.exit(0);
    }

    // Function that if the button is pressed the window minimizes
    @FXML
    protected void onMinimizeButtonClick() {
        Stage stage = (Stage) setupTitle.getScene().getWindow();
        stage.setIconified(true);
    }

    // Function that if the button is pressed the window maximizes
    @FXML
    protected void onMaximizeButtonClick() {
        Stage stage = (Stage) setupTitle.getScene().getWindow();
        stage.setMaximized(true);
    }

    // Function that imports the data from the file
    protected void importData(String filePath) {
        System.out.println("Ficheiro a importar" + filePath);
        try {
            inputData = Data.create(filePath, Charset.defaultCharset(), ';');
        } catch (IOException e) {
            System.out.println("Error importing data");
        }
    }

    // Function that opens the window to define the data types
    protected void dataTypeWindow() {
        setupTitle.setText("Escolha o tipo de dados");
        setupButton.setText("CONTINUE");
        int numeroColunas = inputData.getHandle().getNumColumns();
        setupButtons = new RadioButton[numeroColunas][NUMERO_TIPOS];
        for (int indexColuna = 0; indexColuna < numeroColunas; indexColuna++) {
            ToggleGroup buttonGroup = new ToggleGroup();
            for (int indexTipo = 0; indexTipo < NUMERO_TIPOS; indexTipo++) {
                RadioButton button = new RadioButton(dataTypes[indexTipo]);
                // Set style
                button.setStyle("-fx-background-color: #2c2f33; -fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px 10px 10px 10px; -fx-border-color: #ffffff; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                button.setToggleGroup(buttonGroup);
                setupButtons[indexColuna][indexTipo] = button;
            }
            VBox dataTypeColumn = new VBox();
            dataTypeColumn.getChildren().addAll(setupButtons[indexColuna]);
            setupButtonContainer.getChildren().add(dataTypeColumn);
        }
    }

    // Function that defines the data types
    protected void defineDataTypes() {
        int numeroColunas = inputData.getHandle().getNumColumns();
        for (int indexColuna = 0; indexColuna < numeroColunas; indexColuna++) {
            if (setupButtons[indexColuna][0].getToggleGroup().getSelectedToggle() == null) {
                System.out.println("Erro");
                return;
            }
            for (int indexTipo = 0; indexTipo < NUMERO_TIPOS; indexTipo++) {
                if (!setupButtons[indexColuna][indexTipo].isSelected()) {
                    continue;
                }
                switch (indexTipo) {
                    case 0 ->
                            inputData.getDefinition().setDataType(inputData.getHandle().getAttributeName(indexColuna), DataType.STRING);
                    case 1 ->
                            inputData.getDefinition().setDataType(inputData.getHandle().getAttributeName(indexColuna), DataType.INTEGER);
                    case 2 ->
                            inputData.getDefinition().setDataType(inputData.getHandle().getAttributeName(indexColuna), DataType.ORDERED_STRING);
                    case 3 ->
                            inputData.getDefinition().setDataType(inputData.getHandle().getAttributeName(indexColuna), DataType.DATE);
                    case 4 ->
                            inputData.getDefinition().setDataType(inputData.getHandle().getAttributeName(indexColuna), DataType.DECIMAL);
                }
            }
        }
    }

    // Function that opens the window to define the attribute types
    protected void attributeTypeWindow() {
        setupTitle.setText("Escolha o tipo de atributo");
        setupButton.setText("ACCEPT");
        int numeroColunas = inputData.getHandle().getNumColumns();
        for (int indexColuna = 0; indexColuna < numeroColunas; indexColuna++) {
            for (int indexTipo = 0; indexTipo < NUMERO_TIPOS; indexTipo++) {
                setupButtons[numeroColunas][indexTipo].setText(attributeTypes[indexTipo]);
            }
        }
    }

    // Function that defines the attribute types
    protected void defineAttributeTypes() {
        int numeroColunas = inputData.getHandle().getNumColumns();
        for (int indexColuna = 0; indexColuna < numeroColunas; indexColuna++) {
            if (setupButtons[indexColuna][0].getToggleGroup().getSelectedToggle() == null) {
                System.out.println("Erro");
                return;
            }
            for (int indexTipo = 0; indexTipo < NUMERO_TIPOS; indexTipo++) {
                if (!setupButtons[indexColuna][indexTipo].isSelected()) {
                    continue;
                }
                switch (indexTipo) {
                    case 0 ->
                            inputData.getDefinition().setAttributeType(inputData.getHandle().getAttributeName(indexColuna), AttributeType.IDENTIFYING_ATTRIBUTE);
                    case 1 ->
                            inputData.getDefinition().setAttributeType(inputData.getHandle().getAttributeName(indexColuna), AttributeType.SENSITIVE_ATTRIBUTE);
                    case 2, 4 ->
                            inputData.getDefinition().setAttributeType(inputData.getHandle().getAttributeName(indexColuna), AttributeType.INSENSITIVE_ATTRIBUTE);
                    case 3 ->
                            inputData.getDefinition().setAttributeType(inputData.getHandle().getAttributeName(indexColuna), AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
                }
            }
        }
    }

    // Function that opens a new window
    protected void newWindow(){
        Stage stage = new Stage();
        stage.setTitle("New Window");
    }
}