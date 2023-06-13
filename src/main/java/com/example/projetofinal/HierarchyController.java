package com.example.projetofinal;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.Data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;

public class HierarchyController {
    public Data inputData = SetupController.getData();
    @FXML
    private HBox hierarchyContainer;

    final FileChooser fileChooser = new FileChooser();

    public void initialize() {
        hierarchyWindow(); // Set hierarchyWindow
    }

    // Shows the hierarchy UI
    public void hierarchyWindow() {
        Set<String> attributes = inputData.getDefinition().getQuasiIdentifyingAttributes();
        for (int index = 0; index < attributes.size(); index++) {
            String attribute = (String) attributes.toArray()[index];
            VBox column = new VBox();
            column.setStyle("-fx-alignment: center; -fx-padding: 10px;");
            Label attributeLabel = new Label(attribute);
            attributeLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-family: 'Yu Gothic Medium'; -fx-font-size: 13px; -fx-pref-width: 100px;-fx-alignment: center;-fx-padding: 15px 5px 15px 5px;");
            column.getChildren().add(attributeLabel);
            Button importHierarchy = new Button("Importar Hierarquia");
            importHierarchy.setStyle("-fx-text-fill: white; -fx-background-color: #6794B5;-fx-padding: 5px 10px 5px 10px;");
            importHierarchy.setOnMouseClicked(e -> importHierarchy(attribute));
            column.getChildren().add(importHierarchy);
            hierarchyContainer.getChildren().add(column);
        }
    }

    // Function that imports a hierarchy from a file given a column index for an attribute
    public void importHierarchy(String attributeName) {
        try {
            File file = fileChooser.showOpenDialog(new Stage());
            if (file == null) return;
            AttributeType.Hierarchy hierarchy = AttributeType.Hierarchy.create(file, Charset.defaultCharset(), ';');
            inputData.getDefinition().setHierarchy(attributeName, hierarchy);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Hierarchy imported successfully");
            alert.setContentText("The hierarchy file was imported successfully.");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error importing hierarchy");
            alert.setContentText("The hierarchy file is not valid.");
            alert.showAndWait();
        }
    }
}
