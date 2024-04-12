module com.example.sdmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires metadata.extractor;
    requires jdk.jsobject;
    requires org.json;


    opens com.example.sdmanager to javafx.fxml;
    exports com.example.sdmanager;
}