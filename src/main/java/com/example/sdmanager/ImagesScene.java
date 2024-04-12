package com.example.sdmanager;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataReader;
import com.drew.metadata.Tag;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import java.awt.Desktop;

public class ImagesScene extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private ImageView pictures;
    private GridPane imagesGrid;
    private File imagepath;
    private int imageamout;
    private Text amountText;
    private Stage primaryStage;
    private String positivePrompt;
    private String negativePrompt;


    public ImagesScene() throws IOException {
        imagepath = ConfigReader.returnImagePath();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        // teilter teilt in linke und rechte hälfte
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
            PromptsScene promptsScene = null;
            try {
                promptsScene = new PromptsScene();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            promptsScene.start(primaryStage);
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
        amountText = new Text(imageamout + " images added to viewport");
        amountPane.getChildren().add(amountText);
        amountPane.setMargin(amountText, new Insets(12, 0, 5, 0));
        amountPane.setPrefHeight(54);
        amountPane.setPrefWidth(400);
        amountText.setFont(new Font(14));


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(700);
        scrollPane.setPrefWidth(982);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        imagesGrid = new GridPane();
        scrollPane.setContent(imagesGrid);

        imagesGrid.setPrefHeight(700);
        imagesGrid.setPrefWidth(982);
        imagesGrid.setHgap(20);
        imagesGrid.setVgap(20);


        imagesVbox.getChildren().addAll(amountPane, scrollPane);
        teiler.getChildren().add(imagesVbox);

        settings.setOnAction(e -> {
            SettingsScene settingsScene = null;
            try {
                settingsScene = new SettingsScene(primaryStage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

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
            amountText.setText(imageamout + " images added to viewport");
            return;
        }

        imageamout = files.length;
        amountText.setText(imageamout + " images added to viewport");
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            for (String extensions : imageExtensions) {
                if (fileName.endsWith("." + extensions)) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(files[i]);
                        Image image = new Image(fileInputStream);
                        pictures = new ImageView(image);

                        // Set the ImageView's preserveRatio property to true
                        pictures.setPreserveRatio(true);

                        // Set the ImageView's viewport to display a portion of the image
                        double width = image.getWidth();
                        double height = image.getHeight();
                        double boxRatio = 300.0 / 300.0;
                        double imageAspect = width / height;

                        if (imageAspect > boxRatio) {
                            // The image is wider than the desired aspect ratio, so we need to crop the width
                            double newWidth = height * boxRatio;
                            double xOffset = (width - newWidth) / 2;
                            pictures.setViewport(new Rectangle2D(xOffset, 0, newWidth, height));
                        } else {
                            // The image is taller than the desired aspect ratio, so we need to crop the height
                            double newHeight = width / boxRatio;
                            double yOffset = (height - newHeight) / 2;
                            pictures.setViewport(new Rectangle2D(0, yOffset, width, newHeight));
                        }

                        pictures.setFitHeight(300);
                        pictures.setFitWidth(300);




                        Rectangle overlay = new Rectangle(300, 300, Color.color(0, 0, 0, 0.5));
                        Label overlayText = new Label();
                        Metadata metadata = ImageMetadataReader.readMetadata(files[i]);

                        StringBuilder output = new StringBuilder();

                        System.out.println(overlayText.getText());

                        overlayText.setMaxWidth(pictures.getFitWidth());
                        overlayText.setTextFill(Color.WHITE);

                        // Initially hide the overlay and the text
                        overlay.setVisible(false);
                        overlayText.setVisible(false);

                        // Create a StackPane to hold the ImageView, Rectangle and Label
                        StackPane stackPane = new StackPane();
                        stackPane.getChildren().addAll(pictures, overlay, overlayText);



// Add an EventHandler to the ImageView
                        stackPane.setOnMouseClicked(event -> {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                // Toggle the visibility of the overlay and the text
                                boolean isVisible = overlay.isVisible();
                                overlay.setVisible(!isVisible);
                                overlayText.setVisible(!isVisible);

                                if (overlay.isVisible()) {
                                    output.setLength(0);
                                    for (Directory directory : metadata.getDirectories()) {
                                        if (directory.getTags().toArray()[0].toString().contains("Image Width")) {
                                            output.append("Image Width: " + directory.getTags().toArray()[0].toString().split("- ")[1] + "\n");
                                            output.append("Image Height: " + directory.getTags().toArray()[1].toString().split("- ")[1] + "\n");
                                            output.append("\n");
                                        }
                                        if (directory.getTags().toArray()[0].toString().contains("prompt")) {
                                            String prompt = directory.getTags().toArray()[0].toString();
                                            System.out.println(prompt);


                                            output.append("Prompt: " + prompt.split("parameters: ")[1].split("Negative prompt")[0]);
                                            positivePrompt = prompt.split("parameters: ")[1].split("Negative prompt")[0];

                                            prompt = prompt.split("parameters: ")[1];
                                            output.append("Negative prompt: " + prompt.split("Negative prompt: ")[1].split("Steps:")[0] + "\n");
                                            negativePrompt = prompt.split("Negative prompt: ")[1].split("Steps:")[0];
                                            prompt = prompt.split("Negative prompt: ")[1];
                                            output.append("Steps: " + prompt.split("Steps: ")[1].split(", Sampler")[0] + "\n");
                                            output.append("Sampler: " + prompt.split(", Sampler: ")[1].split(", CFG scale")[0] + "\n");
                                            prompt = prompt.split(", Sampler: ")[1];
                                            output.append("CFG scale: " + prompt.split("CFG scale: ")[1].split(", Seed")[0] + "\n");
                                            prompt = prompt.split("CFG scale: ")[1];

                                            prompt = prompt.split("Seed: ")[1];
                                            output.append("Seed: " + prompt.split(", Face restoration")[0] + "\n");
                                            output.append("Face Restoration: " + prompt.split("Face restoration: ")[1].split(", Size: ")[0] + "\n");
                                            prompt = prompt.split("Model: ")[1];
                                            output.append("Model: " + prompt.split(", Clip skip: ")[0] + "\n");
                                            prompt = prompt.split("Clip skip: ")[1];
                                            output.append("Clip Skip: " + prompt.split(", Version: ")[0] + "\n");
                                        }

                                    }
                                    overlayText.setText(output.toString());
                                } else {
                                    overlayText.setText("");
                                }
                            }
                        });


                        // Create a ContextMenu
                        ContextMenu contextMenu = new ContextMenu();

                        // Create a MenuItem for copying the prompt
                        MenuItem copyPosPromptItem = new MenuItem("Copy Positive Prompt");
                        copyPosPromptItem.setOnAction(event -> {
                            Clipboard clipboard = Clipboard.getSystemClipboard();
                            ClipboardContent content = new ClipboardContent();
                            content.putString(positivePrompt);
                            clipboard.setContent(content);
                        });

                        MenuItem copyNegPromptItem = new MenuItem("Copy Negative Prompt");
                        copyNegPromptItem.setOnAction(event -> {
                            Clipboard clipboard = Clipboard.getSystemClipboard();
                            ClipboardContent content = new ClipboardContent();
                            content.putString(negativePrompt);
                            clipboard.setContent(content);
                        });

                        // Create a MenuItem for opening the image
                        MenuItem openImageItem = new MenuItem("Open Image");
                        final File file = files[i];
                        openImageItem.setOnAction(event -> {
                            if (Desktop.isDesktopSupported()) {
                                try {
                                    Desktop.getDesktop().open(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        // Add the MenuItems to the ContextMenu
                        contextMenu.getItems().addAll(openImageItem, copyPosPromptItem, copyNegPromptItem);

                        // Add the ContextMenu to the StackPane
                        stackPane.setOnContextMenuRequested(event -> {
                            contextMenu.show(stackPane, event.getScreenX(), event.getScreenY());
                        });


// Add the StackPane to the GridPane instead of the ImageView
                        imagesGrid.add(stackPane, i % 3, j);
                        if (i % 3 == 2) {
                            j++;
                        }

                    } catch (IOException e) {
                    } catch (ImageProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }
    }
}