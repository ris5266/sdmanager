module com.example.sdmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires metadata.extractor;



    opens com.example.sdmanager to javafx.fxml;
    exports com.example.sdmanager;
}