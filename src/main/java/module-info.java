module com.example.projetofinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires libarx;


    opens com.example.projetofinal to javafx.fxml;
    exports com.example.projetofinal;
}