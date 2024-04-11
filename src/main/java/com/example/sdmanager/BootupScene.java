package com.example.sdmanager;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BootupScene extends Application {
    // components
    Button submit;
    GridPane pane;
    Text welcome;
    Text tutorial;
    TextField path;
    Button inputButton;
    File folderpath;
    // layouts
    VBox vbox;
    GridPane inputField;
    VBox submitVbox;
    VBox mainVbox;

    Scene scene;
    Stage stage;

    private static final String CONFIG_FILE_PATH = "config.txt";

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        pane = new GridPane();
        vbox = new VBox();
        mainVbox = new VBox();
        submitVbox = new VBox();
        path = new TextField();
        inputButton = new Button("+");
        inputField = new GridPane();
        submit = new Button("Submit");



        welcome = new Text("Welcome");
        tutorial = new Text("please select your sd output folder:");

        submit.setDisable(true);

        DirectoryChooser directorychooser = new DirectoryChooser();
        inputButton.setOnAction(e -> {
            folderpath = directorychooser.showDialog(stage);
            if(folderpath != null) {
                path.setText(folderpath.getAbsolutePath());
                submit.setDisable(false);
            }
        });

        // Check if config file exists
        if (Files.exists(Paths.get(CONFIG_FILE_PATH))) {
            String savedPath = readConfigFile();
            if (savedPath != null) {
                folderpath = new File(savedPath);
                GalleryScene gallery = new GalleryScene(folderpath);
                gallery.start(stage);
                return;
            }
        }

        submit.setOnAction(e -> {
            submit();
            try {
                savePathToConfigFile(folderpath.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        pane.setAlignment(Pos.CENTER);

        // field + button
        inputField.add(path, 0, 0);
        inputField.add(inputButton, 1,  0);
        inputField.setAlignment(Pos.CENTER);

        // submit button
        submitVbox.getChildren().add(submit);
        submitVbox.setAlignment(Pos.CENTER);

        // layouts kombinieren
        vbox.getChildren().addAll(welcome, tutorial);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(3);

        pane.add(vbox, 0, 0);
        pane.add(inputField, 0, 1);
        pane.add(submitVbox, 0, 5);
        pane.setVgap(15);
        mainVbox.getChildren().addAll(pane, submitVbox);
        mainVbox.setSpacing(30);
        mainVbox.setAlignment(Pos.CENTER);


        scene = new Scene(mainVbox, 800, 600);
        stage.setTitle("sdmanager");
        stage.setScene(scene);
        stage.show();
    }

    public void submit() {
            // change bootup scene to gallery scene
            if(folderpath != null) {
                GalleryScene gallery = new GalleryScene(folderpath);
                gallery.start(stage);
            }
    }

    private void savePathToConfigFile(String path) throws IOException {
        Files.write(Paths.get(CONFIG_FILE_PATH), path.getBytes(StandardCharsets.UTF_8));
    }

    private String readConfigFile() throws IOException {
        return new String(Files.readAllBytes(Paths.get(CONFIG_FILE_PATH)), StandardCharsets.UTF_8);
    }


    public static void main(String[] args) {
        launch();
    }
}
