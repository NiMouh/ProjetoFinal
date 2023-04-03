package com.example.projetofinal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.stream.IntStream;

public class SetupController {
    @FXML
    private Label setupTitle;

    @FXML
    private Button setupButton;

    @FXML
    private RadioButton[][] setupButtons;
    @FXML
    private Button closeBtn, minimizeBtn, maximizeBtn;
    @FXML
    private HBox setupButtonContainer;
    @FXML
    private BorderPane mainPage;
    private static double xOffset = 0, yOffset = 0;

    final FileChooser fileChooser = new FileChooser();
    public static Data inputData;
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
                try {
                    newWindow(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        closeBtn.setOnMouseClicked(SetupController::onCloseButtonClick);
        minimizeBtn.setOnMouseClicked(SetupController::onMinimizeButtonClick);
        maximizeBtn.setOnMouseClicked(SetupController::onMaximizeButtonClick);
        makeStageDraggable();
    }

    // Function that if the button is pressed the window closes
    @FXML
    public static void onCloseButtonClick(MouseEvent event) {
        System.exit(0);
    }

    // Function that if the button is pressed the window minimizes
    @FXML
    public static void onMinimizeButtonClick(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }

    // Function that if the button is pressed the window maximizes
    @FXML
    public static void onMaximizeButtonClick(MouseEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setFullScreenExitHint("");
        stage.setFullScreen(!stage.isFullScreen());
    }

    // Function that allows the window to be dragged
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

    // Function that imports the data from the file
    protected void importData(String filePath) {
        System.out.println("Ficheiro a importar" + filePath);
        try {
            inputData = Data.create(filePath, Charset.defaultCharset(), ';');
        } catch (IOException e) {
            System.out.println("Error importing data");
        }
    }

    // Function that defines the data types
    protected void dataTypeWindow() {
        setupTitle.setText("Data Type");
        setupButton.setText("CONTINUE");
        int numeroColunas = inputData.getHandle().getNumColumns();
        setupButtons = new RadioButton[numeroColunas][NUMERO_TIPOS];
        for (int indexColuna = 0; indexColuna < numeroColunas; indexColuna++) {
            Label columnName = new Label(inputData.getHandle().getAttributeName(indexColuna));
            columnName.setStyle("-fx-text-fill: #FFFFFF; -fx-font-family: 'Yu Gothic Medium'; -fx-font-size: 13px; -fx-pref-width: 100px;-fx-alignment: center;");
            VBox dataTypeColumn = new VBox();
            dataTypeColumn.getChildren().addAll(columnName);
            ToggleGroup buttonGroup = new ToggleGroup();
            for (int indexTipo = 0; indexTipo < NUMERO_TIPOS; indexTipo++) {
                RadioButton button = createRadioButton(dataTypes[indexTipo], buttonGroup);
                setupButtons[indexColuna][indexTipo] = button;
                dataTypeColumn.getChildren().add(button);
            }
            setupButtonContainer.getChildren().add(dataTypeColumn);
        }
    }

    // Function that creates the radio buttons
    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup) {
        RadioButton button = new RadioButton(text);
        button.setStyle("-fx-background-color: #29273D; -fx-padding: 10px; -fx-border-insets: 5px; -fx-background-insets: 5px; -fx-background-radius: 5px; -fx-text-fill: #FFFFFF; -fx-font-family: 'Yu Gothic Medium'; -fx-font-size: 13px; -fx-pref-width: 100px;");
        button.setToggleGroup(toggleGroup);
        button.selectedProperty().addListener(event -> {
            if (button.isSelected()) {
                button.setStyle("-fx-background-color: #6794B5; -fx-padding: 10px; -fx-border-insets: 5px; -fx-background-insets: 5px; -fx-background-radius: 5px; -fx-text-fill: #FFFFFF; -fx-font-family: 'Yu Gothic Medium'; -fx-font-size: 13px; -fx-pref-width: 100px;");
            } else {
                button.setStyle("-fx-background-color: #29273D; -fx-padding: 10px; -fx-border-insets: 5px; -fx-background-insets: 5px; -fx-background-radius: 5px; -fx-text-fill: #FFFFFF; -fx-font-family: 'Yu Gothic Medium'; -fx-font-size: 13px; -fx-pref-width: 100px;");
            }
        });
        return button;
    }


    // Function that defines the data types
    protected void defineDataTypes() {
        int numeroColunas = inputData.getHandle().getNumColumns();
        for (int indexColuna = 0; indexColuna < numeroColunas; indexColuna++) {
            if (setupButtons[indexColuna][0].getToggleGroup().getSelectedToggle() == null) {
                System.out.println("Erro");
                return;
            }
            if (setupButtons[indexColuna][0].isSelected()){
                inputData.getDefinition().setDataType(inputData.getHandle().getAttributeName(indexColuna), DataType.STRING);
            }else if (setupButtons[indexColuna][1].isSelected()){
                inputData.getDefinition().setDataType(inputData.getHandle().getAttributeName(indexColuna), DataType.INTEGER);
            }else if (setupButtons[indexColuna][2].isSelected()){
                inputData.getDefinition().setDataType(inputData.getHandle().getAttributeName(indexColuna), DataType.ORDERED_STRING);
            }else if (setupButtons[indexColuna][3].isSelected()){
                inputData.getDefinition().setDataType(inputData.getHandle().getAttributeName(indexColuna), DataType.DATE);
            }else if (setupButtons[indexColuna][4].isSelected()){
                inputData.getDefinition().setDataType(inputData.getHandle().getAttributeName(indexColuna), DataType.DECIMAL);
            }
        }
    }

    // Function that opens the window to define the attribute types
    protected void attributeTypeWindow() {
        setupTitle.setText("Attribute Type");
        setupButton.setText("ACCEPT");
        IntStream.range(0, inputData.getHandle().getNumColumns()).forEach(indexColuna -> IntStream.range(0, NUMERO_TIPOS).forEach(indexTipo -> {
            setupButtons[indexColuna][indexTipo].setSelected(false);
            setupButtons[indexColuna][indexTipo].setText(attributeTypes[indexTipo]);
        }));
    }


    // Function that defines the attribute types
    protected void defineAttributeTypes() {
        int numeroColunas = inputData.getHandle().getNumColumns();
        for (int indexColuna = 0; indexColuna < numeroColunas; indexColuna++) {
            if (setupButtons[indexColuna][0].getToggleGroup().getSelectedToggle() == null) {
                System.out.println("Erro");
                return;
            }
            if (setupButtons[indexColuna][0].isSelected()) {
                inputData.getDefinition().setAttributeType(inputData.getHandle().getAttributeName(indexColuna), AttributeType.IDENTIFYING_ATTRIBUTE);
            } else if (setupButtons[indexColuna][1].isSelected()) {
                inputData.getDefinition().setAttributeType(inputData.getHandle().getAttributeName(indexColuna), AttributeType.SENSITIVE_ATTRIBUTE);
            } else if (setupButtons[indexColuna][2].isSelected() || setupButtons[indexColuna][4].isSelected()) {
                inputData.getDefinition().setAttributeType(inputData.getHandle().getAttributeName(indexColuna), AttributeType.INSENSITIVE_ATTRIBUTE);
            } else if (setupButtons[indexColuna][3].isSelected()) {
                inputData.getDefinition().setAttributeType(inputData.getHandle().getAttributeName(indexColuna), AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
            }
        }
    }

    // Function that opens a new window
    protected void newWindow(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("program-screen.fxml"))));
        stage.setScene(scene);
    }
}