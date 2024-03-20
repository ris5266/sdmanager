package com.example.sdmanager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class ImageScene extends Application {
    File path;

    public ImageScene(File path) {
        this.path = path;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        sort();
        VBox main = new VBox();
        Button test = new Button("test");
        main.getChildren().add(test);


        Scene galleryScene = new Scene(main, 1200, 720);
        stage.setScene(galleryScene);

        stage.setTitle("Gallery");
        stage.show();

    }


    public void sort() {

        File[] files = path.listFiles();
        for (File file : files) {
            String[] imageExtensions = new String[]{"jpg", "png", "gif", "bmp", "jpeg"};
            String fileName = file.getName().toLowerCase();
            for (String extension : imageExtensions) {
                if (fileName.endsWith(extension)) {

                }
            }
        }
    }
}
