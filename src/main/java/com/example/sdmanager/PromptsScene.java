package com.example.sdmanager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class PromptsScene extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    File imagepath = null;
    int promptamount = 0;

    public PromptsScene() {
    }

    @Override
    public void start(Stage primaryStage) {
        // teilter teilt in linke und rechte hölfte
        HBox teiler = new HBox();
        teiler.setPrefHeight(800);
        teiler.setPrefWidth(1200);

        // header ist die linke hälfte
        VBox header = new VBox();
        teiler.getChildren().add(header);
        header.setPrefHeight(400);
        header.setPrefWidth(201);
        teiler.setAlignment(Pos.TOP_LEFT);

        // programname + input
        FlowPane softwarepane = new FlowPane();
        Text softwarename = new Text("Diffusion Depot");
        softwarepane.getChildren().add(softwarename);
        softwarepane.setPadding(new Insets(10, 0, 0, 0));
        softwarename.setFont(new Font(19));
        softwarepane.setAlignment(Pos.CENTER);
        softwarepane.setPrefHeight(39);
        softwarepane.setPrefWidth(222);

        FlowPane inputpane = new FlowPane();
        TextField searchinput = new TextField();
        inputpane.setAlignment(Pos.CENTER);
        inputpane.getChildren().add(searchinput);
        inputpane.setPrefWidth(201);
        inputpane.setPrefHeight(44);

        // buttons
        VBox buttonpane = new VBox();
        buttonpane.setAlignment(Pos.TOP_CENTER);
        Button home = new Button("\uD83C\uDFE0 Home");
        Button images = new Button("\uD83D\uDDBC\uFE0F Images");
        Button prompts = new Button("\uD83D\uDCAC Prompts");

        buttonpane.getChildren().addAll(home, images, prompts);
        buttonpane.setPrefWidth(201);
        buttonpane.setPrefHeight(681);
        home.setPrefWidth(201);
        images.setPrefWidth(201);
        prompts.setPrefWidth(201);

        home.setStyle("-fx-background-color: white");
        images.setStyle("-fx-background-color: white");
        prompts.setStyle("-fx-background-color: white");

        home.setOnMouseEntered(e -> {
            home.setStyle("-fx-background-color: #ffbe0b");
        });
        home.setOnMouseExited(e -> {
            home.setStyle("-fx-background-color: white");
        });

        images.setOnMouseEntered(e -> {
            images.setStyle("-fx-background-color: #ffbe0b");
        });
        images.setOnMouseExited(e -> {
            images.setStyle("-fx-background-color: white");
        });

        prompts.setOnMouseEntered(e -> {
            prompts.setStyle("-fx-background-color: #ffbe0b");
        });
        prompts.setOnMouseExited(e -> {
            prompts.setStyle("-fx-background-color: white");
        });


        // settings button
        FlowPane settingspane = new FlowPane();
        settingspane.setAlignment(Pos.CENTER);
        Button settings = new Button("Settings");
        settings.setStyle("-fx-background-color: white");
        settingspane.getChildren().add(settings);
        settingspane.setPrefHeight(33);
        settingspane.setPrefWidth(201);
        settings.setPrefWidth(201);

        settings.setOnMouseEntered(e -> {
            settings.setStyle("-fx-background-color: #ffbe0b");
        });
        settings.setOnMouseExited(e -> {
            settings.setStyle("-fx-background-color: white");
        });

        home.setOnAction(e -> {
            GalleryScene promptsScene = null;
            try {
                promptsScene = new GalleryScene();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            promptsScene.start(primaryStage);
        });

        images.setOnAction(e -> {
            ImagesScene imagesScene = null;
            try {
                imagesScene = new ImagesScene();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            imagesScene.start(primaryStage);
        });

        // rechte hälfte mit amountnamen und images
        VBox imagesVbox = new VBox();
        HBox amountHbox = new HBox();
        imagesVbox.getChildren().add(amountHbox);
        imagesVbox.setPrefHeight(803);
        imagesVbox.setPrefWidth(1008);
        Button add = new Button("Add");
        add.setStyle("-fx-background-color: #ffbe0b");
        add.setPrefWidth(70);
        add.setPrefHeight(26);

        add.setOnMouseEntered(e -> {
            add.setStyle("-fx-background-color: #f2c035");
        });
        add.setOnMouseExited(e -> {
            add.setStyle("-fx-background-color: #ffbe0b");
        });

        FlowPane amountPane = new FlowPane();
        Text amountText = new Text(promptamount + " prompts added to viewport");
        amountPane.getChildren().add(amountText);
        amountHbox.setPadding( new Insets(12, 0, 0, 0));
        amountHbox.setPrefWidth(400);
        amountHbox.setPrefHeight(65);

        amountPane.setPrefHeight(92);
        amountPane.setPrefWidth(924);
        amountText.setFont(new Font(14));
        amountHbox.getChildren().addAll(amountPane, add);

        settings.setOnAction(e -> {
            SettingsScene settingsScene = new SettingsScene();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            settingsScene.start(stage);
            stage.showAndWait();
        });

        GridPane imagesGrid = new GridPane();
        imagesGrid.setHgap(20);
        imagesGrid.setVgap(20);
        imagesGrid.setPrefHeight(365);
        imagesGrid.setPrefWidth(400);
        imagesVbox.getChildren().add(imagesGrid);

        teiler.getChildren().add(imagesVbox);
        header.getChildren().addAll(softwarepane, inputpane, buttonpane, settingspane);
        Scene scene = new Scene(teiler, 1200, 800);
        primaryStage.setResizable(false);

        primaryStage.setScene(scene);

        primaryStage.setTitle("Diffusion Depot");
        primaryStage.show();


    }
}
