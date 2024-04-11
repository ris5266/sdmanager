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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class SettingsScene extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    File imagepath = null;
    public SettingsScene(File imagepath) {
        this.imagepath = imagepath;
    }

    @Override
    public void start(Stage stage) {
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



        Text path = new Text("Change folder path: ");
        TextField inputpath = new TextField();
        inputpath.setPrefHeight(26);
        inputpath.setPrefWidth(250);
        inputpath.setText(imagepath.getAbsolutePath());

        Text safe = new Text("Safe mode: ");
        RadioButton safebutton = new RadioButton();
        safe.setTextAlignment(TextAlignment.CENTER);



        FlowPane applypane = new FlowPane();
        Button apply = new Button("Apply");
        apply.setStyle("-fx-background-color: #ffbe0b");
        applypane.setAlignment(Pos.BOTTOM_CENTER);
        applypane.setPrefWidth(602);
        applypane.setPrefHeight(532);
        applypane.getChildren().add(apply);
        applypane.setPadding(new Insets(0, 0, 10, 0));

        main.setAlignment(Pos.CENTER);
        settings.add(path, 0, 1);
        settings.add(inputpath, 1, 1);
        settings.add(safe, 0, 2);
        settings.add(safebutton, 1, 2);
        settings.setAlignment(Pos.TOP_CENTER);
        settings.setVgap(10);
        settings.setHgap(10);

        apply.setOnAction(e -> {
            stage.close();
        });




        main.getChildren().addAll(headerpane, settings, applypane);

        stage.setResizable(false);

        stage.setTitle("Settings");
        Scene scene = new Scene(main, 600, 400);
        stage.setScene(scene);


    }
}