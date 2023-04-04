module com.example.projetofinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires libarx;
    requires com.opencsv;


    opens com.example.projetofinal to javafx.fxml;
    exports com.example.projetofinal;
}