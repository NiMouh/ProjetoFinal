package com.example.projetofinal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.deidentifier.arx.*;
import org.deidentifier.arx.criteria.KAnonymity;

import java.io.IOException;
import java.util.Objects;

public class AppController {
    private final Data inputData = SetupController.inputData;
    @FXML
    private BorderPane mainPage;
    @FXML
    private VBox homeContent;
    @FXML
    private TextField kValue,kStep;

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
                DataHandle anonimizedData = anonimizarPorK(inputData, index);
                System.out.println(anonimizedData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public DataHandle anonimizarPorK(Data dados, int valorK) throws IOException {
        ARXAnonymizer anonimizer = new ARXAnonymizer();
        ARXConfiguration configuracoes = ARXConfiguration.create();
        configuracoes.addPrivacyModel(new KAnonymity(valorK));
        configuracoes.setSuppressionLimit(0.01d); // 1% de linhas suprimidas
        ARXResult result = anonimizer.anonymize(dados, configuracoes);
        return result.getOutput();
    }
}
