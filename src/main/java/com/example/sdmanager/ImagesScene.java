package com.example.sdmanager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

public class ImagesScene extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    ImageView pictures;
    GridPane imagesGrid;
    File imagepath = null;
    int imageamout;

    public ImagesScene(File imagepath) {
        this.imagepath = imagepath;
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
        searchinput.setDisable(true);
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
            PromptsScene promptsScene = new PromptsScene(imagepath);
            promptsScene.start(primaryStage);
        });

        home.setOnAction(e -> {
            GalleryScene promptsScene = new GalleryScene(imagepath);
            promptsScene.start(primaryStage);
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

        // rechte hälfte mit amountnamen und images
        VBox imagesVbox = new VBox();
        FlowPane amountPane = new FlowPane();
        Text amountText = new Text(imageamout + " images added to viewport");
        amountPane.getChildren().add(amountText);
        amountPane.setMargin(amountText, new Insets(12, 0, 5, 0));
        amountPane.setPrefHeight(54);
        amountPane.setPrefWidth(400);
        amountText.setFont(new Font(14));



        imagesGrid = new GridPane();
        imagesGrid.setHgap(20);
        imagesGrid.setVgap(20);
        imagesGrid.setPrefHeight(365);
        imagesGrid.setPrefWidth(400);

        imagesVbox.getChildren().addAll(amountPane, imagesGrid);
        teiler.getChildren().add(imagesVbox);

        settings.setOnAction(e -> {
            SettingsScene settingsScene = new SettingsScene(imagepath);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            settingsScene.start(stage);
            stage.showAndWait();
        });
        sort();
        header.getChildren().addAll(softwarepane, inputpane, buttonpane, settingspane);
        Scene scene = new Scene(teiler, 1200, 800);
        primaryStage.setResizable(false);

        primaryStage.setScene(scene);

        primaryStage.setTitle("Diffusion Depot");
        primaryStage.show();


    }

    public void sort() {
        int j = 0;
        String[] imageExtensions = new String[]{"jpg", "png", "gif", "bmp", "jpeg"};
        File[] files = imagepath.listFiles();


        if (files == null) {
            System.out.println("No files found");
            imageamout = 0;
            return;
        }

        imageamout = files.length;
        for(int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            for (String extensions : imageExtensions) {
                if (fileName.endsWith("." + extensions)) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(files[i]);
                        Image image = new Image(fileInputStream);
                        pictures = new ImageView(image);
                        pictures.setFitHeight(300);
                        pictures.setFitWidth(300);
                        imagesGrid.add(pictures, i % 3, j);
                        if (i % 3 == 2) {
                            j++;
                        }
                    } catch (IOException e) {
                    }
                    break;
                }
            }
        }
    }
}
