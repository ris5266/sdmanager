package com.example.sdmanager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SettingsScene extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    File selectedDirectory;
    File imagepath;
    Stage stage;
    public SettingsScene(Stage stage) throws IOException {
        imagepath = ConfigReader.returnImagePath();
        this.stage = stage;
    }

    @Override
    public void start(Stage primarystage) {
        VBox main = new VBox();
        FlowPane headerpane = new FlowPane();
        headerpane.setAlignment(Pos.CENTER);
        Text header = new Text("Settings");
        headerpane.setPadding(new Insets(15, 0, 5, 0));
        headerpane.setPrefHeight(58);
        headerpane.setPrefWidth(602);

        header.setFont(new Font(18));
        headerpane.getChildren().add(header);


        GridPane settings = new GridPane();



        Text path = new Text("Folder path: ");

        TextField inputpath = new TextField();
        inputpath.setEditable(false);
        inputpath.setPrefHeight(26);
        inputpath.setPrefWidth(250);
        inputpath.setText(imagepath.getAbsolutePath());

        Button inputButton = new Button("+");

        inputButton.setPrefHeight(26);
        inputButton.setPrefWidth(26);
        inputButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Folder");
            selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null) {
                inputpath.setText(selectedDirectory.getAbsolutePath());
            }
        });


        // Text safe = new Text("Safe mode: ");
        // RadioButton safebutton = new RadioButton();
        // safe.setTextAlignment(TextAlignment.CENTER);



        FlowPane applypane = new FlowPane();
        Button apply = new Button("Apply");
        apply.setStyle("-fx-background-color: #ffbe0b");
        applypane.setAlignment(Pos.BOTTOM_CENTER);
        applypane.setPrefWidth(602);
        applypane.setPrefHeight(532);
        applypane.getChildren().add(apply);
        applypane.setPadding(new Insets(0, 0, 10, 0));

        apply.setOnMouseEntered(e -> {
            apply.setStyle("-fx-background-color: #f2ce6b");
        });
        apply.setOnMouseExited(e -> {
            apply.setStyle("-fx-background-color: #ffbe0b");

        });

        main.setAlignment(Pos.CENTER);
        settings.add(path, 0, 1);
        settings.add(inputpath, 1, 1);
        settings.add(inputButton, 2, 1);

        // settings.add(safe, 0, 2);
        // settings.add(safebutton, 1, 2);

        settings.setAlignment(Pos.TOP_CENTER);
        settings.setVgap(10);
        settings.setHgap(10);

        apply.setOnAction(e -> {
            if(selectedDirectory == null) {
                primarystage.close();
            }
            try {
                if(imagepath.getAbsolutePath().equals(selectedDirectory.getAbsolutePath())) {
                    primarystage.close();
                }
            } catch (NullPointerException ex) {
                throw new NullPointerException("no changes made");
            }



            try {
                ConfigReader.writeImagePath(selectedDirectory.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            primarystage.close();

        });

        main.getChildren().addAll(headerpane, settings, applypane);

        primarystage.setResizable(false);

        primarystage.setTitle("Settings");
        Scene scene = new Scene(main, 600, 400);
        primarystage.setScene(scene);
    }
}
