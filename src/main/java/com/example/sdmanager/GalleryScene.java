package com.example.sdmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.File;
import java.io.FileReader;



public class GalleryScene extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    GridPane imagesGrid;
    VBox imagesVbox;

    File folderpath = null;
    int characteramount = 0;
    Label nocharacters;
    FlowPane nocharacterspane;

    public GalleryScene() {
        folderpath = new File("/Users/rich/Desktop/images");
    }

    public GalleryScene(File folderpath) {
        this.folderpath = folderpath;
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

        prompts.setOnAction(e -> {
            PromptsScene promptsScene = new PromptsScene(folderpath);
            promptsScene.start(primaryStage);
        });

        images.setOnAction(e -> {
            ImagesScene imageScene = new ImagesScene(folderpath);
            imageScene.start(primaryStage);
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
            settings.setStyle("-fx-background-color: #f2ce6b");
        });
        settings.setOnMouseExited(e -> {
            settings.setStyle("-fx-background-color: white");
        });

        // rechte hälfte mit amountnamen und images
         imagesVbox = new VBox();
        imagesVbox.setPrefHeight(803);
        imagesVbox.setPrefWidth(1008);
        HBox amountHbox = new HBox();
        imagesVbox.getChildren().add(amountHbox);

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

        settings.setOnAction(e -> {
            SettingsScene settingsScene = new SettingsScene(folderpath);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            settingsScene.start(stage);
            stage.showAndWait();
        });

        FlowPane amountPane = new FlowPane();
        Text amountText = new Text(characteramount + " characters added to viewport");
        amountPane.getChildren().add(amountText);
        amountHbox.setPadding( new Insets(12, 0, 0, 0));
        amountHbox.setPrefWidth(400);
        amountHbox.setPrefHeight(65);

        amountPane.setPrefHeight(92);
        amountPane.setPrefWidth(924);
        amountText.setFont(new Font(14));
        amountHbox.getChildren().addAll(amountPane, add);


        teiler.getChildren().add(imagesVbox);
        load();



        header.getChildren().addAll(softwarepane, inputpane, buttonpane, settingspane);
        Scene scene = new Scene(teiler, 1200, 800);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Diffusion Depot");
        primaryStage.show();
    }

    public void load() {




        // txt file laden
        if(characteramount == 0) {
            nocharacterspane = new FlowPane();
            nocharacters = new Label("no characters found ☹\uFE0F");
            nocharacters.setAlignment(Pos.CENTER);
            nocharacterspane.setAlignment(Pos.CENTER);
            nocharacterspane.setPrefHeight(1167);
            nocharacterspane.setPrefWidth(568);
            nocharacterspane.getChildren().add(nocharacters);
            imagesVbox.getChildren().add(nocharacterspane);
        } else {
             imagesGrid = new GridPane();
            imagesGrid.setHgap(20);
            imagesGrid.setVgap(20);
            imagesGrid.setPrefHeight(365);
            imagesGrid.setPrefWidth(400);

            imagesVbox.getChildren().add(imagesGrid);
        }

    }
}
