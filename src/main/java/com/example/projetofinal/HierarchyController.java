package com.example.projetofinal;

import javafx.fxml.FXML;
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
    public Data inputData = SetupController.inputData;
    @FXML
    private HBox hierarchyContainer;

    final FileChooser fileChooser = new FileChooser();

    public void initialize() {
        // Set hierarchyWindow
        hierarchyWindow();
    }

    public void hierarchyWindow() {
        Set<String> attributes = inputData.getDefinition().getQuasiIdentifyingAttributes();
        for (int index = 0; index < attributes.size(); index++) {
            String attribute = (String) attributes.toArray()[index];
            VBox column = new VBox();
            column.setStyle("-fx-alignment: center; -fx-padding: 10px;");
            Label attributeLabel = new Label(attribute);
            attributeLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-family: 'Yu Gothic Medium'; -fx-font-size: 13px; -fx-pref-width: 100px;-fx-alignment: center;");
            column.getChildren().add(attributeLabel);
            Button importHierarchy = new Button("Importar Hierarquia");
            importHierarchy.setStyle("-fx-text-fill: white; -fx-background-color: #6794B5;-fx-padding: 5px 10px 5px 10px");
            final int hierarchyIndex = index;
            importHierarchy.setOnMouseClicked(e -> importHierarchy(inputData, hierarchyIndex));
            column.getChildren().add(importHierarchy);
            hierarchyContainer.getChildren().add(column);
        }
    }

    public void importHierarchy(Data data, int indexColumn) {
        try {
            File fileName = fileChooser.showOpenDialog(new Stage());
            if (fileName == null) return;
            AttributeType.Hierarchy hierarchy = AttributeType.Hierarchy.create(fileName, Charset.defaultCharset(), ';');
            data.getDefinition().setHierarchy(data.getHandle().getAttributeName(indexColumn), hierarchy);
        } catch (IOException e) {
            System.out.println("Erro ao importar a hierarquia.");
        }
    }
}
