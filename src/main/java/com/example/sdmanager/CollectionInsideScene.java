package com.example.sdmanager;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CollectionInsideScene extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    private String positivePrompt;
    private String negativePrompt;
    private ImageView pictures;
    private GridPane imagesGrid;
    private File imagepath;
    private int imageamout;
    private Text amountText;
    private Stage primaryStage;
    private Collection collection;

    // Constructor that takes a Collection object as a parameter to display the images in the collection
    public CollectionInsideScene(Collection collection) throws IOException {
        this.collection = collection;
        imagepath = new File(collection.getImageFolderPath());
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // right side with amountcount and images
        VBox imagesVbox = new VBox();
        FlowPane amountPane = new FlowPane();
        amountText = new Text(imageamout + " images added to collection");
        amountPane.getChildren().add(amountText);
        amountPane.setMargin(amountText, new Insets(7, 0, 0, 15));
        amountPane.setPrefHeight(0);
        amountPane.setPrefWidth(400);
        amountText.setFont(new Font(14));

        // ScrollPane for the GridPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(700);
        scrollPane.setPrefWidth(982);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-focus-color: transparent");
        scrollPane.setStyle("-fx-faint-focus-color: transparent");
        scrollPane.setStyle("-fx-background-color: transparent");

        // Create a GridPane to hold the images
        imagesGrid = new GridPane();
        scrollPane.setContent(imagesGrid);
        imagesGrid.setPrefHeight(700);
        imagesGrid.setPrefWidth(982);
        imagesGrid.setHgap(20);
        imagesGrid.setVgap(20);
        imagesGrid.setPadding(new Insets(20, 20, 20, 20));

        imagesVbox.getChildren().addAll(amountPane, scrollPane);

        sort();
        Scene scene = new Scene(imagesVbox, 1000, 720);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Diffusion Depot");
        primaryStage.getIcons().add(new Image("icon.jpg"));
        primaryStage.show();
    }

    // Method to read out the images from the folder and display them in the GridPane with the ability to show the metadata and copy the prompts or open the image
    public void sort() {
        int j = 0;
        String[] imageExtensions = new String[]{"jpg", "png", "gif", "bmp", "jpeg"};
        File[] files = imagepath.listFiles();

        // no images found
        if (files == null) {
            System.out.println("No files found");
            imageamout = 0;
            amountText.setText(imageamout + " images added to the collection");
            return;
        }
        int imageCount = 0;

        // Loop through the files in the folder and add them to the gridpane
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            for (String extensions : imageExtensions) {
                if (fileName.endsWith("." + extensions)) {
                    imageCount++;
                    try {
                        FileInputStream fileInputStream = new FileInputStream(files[i]);
                        Image image = new Image(fileInputStream);
                        pictures = new ImageView(image);
                        pictures.setPreserveRatio(true);

                        // view images only in 300x300
                        double width = image.getWidth();
                        double height = image.getHeight();
                        double boxRatio = 300.0 / 300.0;
                        double imageAspect = width / height;

                        if (imageAspect > boxRatio) {
                            // if image is wider than the desired aspect ratio, the width needs to be cropped
                            double newWidth = height * boxRatio;
                            double xOffset = (width - newWidth) / 2;
                            pictures.setViewport(new Rectangle2D(xOffset, 0, newWidth, height));
                        } else {
                            // if image is taller than the desired aspect ratio, the height needs to be cropped
                            double newHeight = width / boxRatio;
                            double yOffset = (height - newHeight) / 2;
                            pictures.setViewport(new Rectangle2D(0, yOffset, width, newHeight));
                        }
                        pictures.setFitHeight(300);
                        pictures.setFitWidth(300);

                        // metadata
                        Rectangle overlay = new Rectangle(300, 300, Color.color(0, 0, 0, 0.5));
                        Label overlayText = new Label();
                        Metadata metadata = ImageMetadataReader.readMetadata(files[i]);

                        StringBuilder output = new StringBuilder();

                        System.out.println(overlayText.getText());

                        overlayText.setMaxWidth(pictures.getFitWidth());
                        overlayText.setTextFill(Color.WHITE);
                        overlayText.setAlignment(Pos.CENTER);
                        overlayText.setPadding(new Insets(10, 10, 10, 10));

                        // hide overlay and text at the beginning
                        overlay.setVisible(false);
                        overlayText.setVisible(false);

                        // show metadata if the user clicks on a image
                        StackPane stackPane = new StackPane();
                        stackPane.getChildren().addAll(pictures, overlay, overlayText);

                        stackPane.setOnMouseClicked(event -> {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                // Toggle the visibility of the overlay and the text
                                boolean isVisible = overlay.isVisible();
                                overlay.setVisible(!isVisible);
                                overlayText.setVisible(!isVisible);

                                if (overlay.isVisible()) {
                                    output.setLength(0);
                                    // stable diffusion web ui saves the parameter [PNG-tEXt] in the metadata
                                    // so we need to go through all metadata tags and check if the tag contains this parameter
                                    for (Directory directory : metadata.getDirectories()) {
                                        for (Tag tag : directory.getTags()) {
                                            if (tag.toString().contains("[PNG-tEXt]")) {
                                                System.out.println(tag.toString());
                                                String parameters = tag.toString();
                                                // parameter is currently a string that contains all the parameters so we need to split it to get the individual parameters
                                                positivePrompt = null;
                                                if (parameters.contains("Negative prompt")) {
                                                    positivePrompt = parameters.split("parameters: ")[1].split("Negative prompt")[0];
                                                } else if (parameters.contains("Steps")) {
                                                    positivePrompt = parameters.split("parameters: ")[1].split("Steps")[0];
                                                }
                                                System.out.println(positivePrompt);

                                                negativePrompt = null;
                                                if (parameters.contains("Negative prompt")) {
                                                    negativePrompt = parameters.split("Negative prompt: ")[1].split("Steps")[0];
                                                }
                                                String steps = parameters.split("Steps: ")[1].split(",")[0];
                                                String sampler = parameters.split("Sampler: ")[1].split(",")[0];
                                                String CFGscale = parameters.split("CFG scale: ")[1].split(",")[0];
                                                String seed = parameters.split("Seed: ")[1].split(",")[0];
                                                String facerestoration = parameters.split("Face restoration: ")[1].split(",")[0];
                                                String size =  parameters.split("Size: ")[1].split(",")[0];
                                                String model = parameters.split("Model: ")[1].split(",")[0];

                                                // display the metadata sorted
                                                overlayText.setText("Positive Prompt: " + positivePrompt +
                                                        "Negative Prompt: " + negativePrompt + "\n" +
                                                        "Size: " + size + "\n" +
                                                        "Model: " + model + "\n" +
                                                        "Steps: " + steps + "\n" +
                                                        "Sampler: " + sampler + "\n" +
                                                        "CFG scale: " + CFGscale + "\n" +
                                                        "Seed: " + seed + "\n" +
                                                        "Face restoration: " + facerestoration);
                                            }

                                        }

                                    }
                                }

                            }

                        });

                        // menuitems for copying the prompts
                        ContextMenu contextMenu = new ContextMenu();

                        MenuItem copyPosPromptItem = new MenuItem("Copy Positive Prompt");
                        copyPosPromptItem.setOnAction(event -> {
                            Clipboard clipboard = Clipboard.getSystemClipboard();
                            ClipboardContent content = new ClipboardContent();
                            String posPrompt = getPositivePrompt(metadata);
                            content.putString(posPrompt);
                            clipboard.setContent(content);
                        });

                        MenuItem copyNegPromptItem = new MenuItem("Copy Negative Prompt");
                        copyNegPromptItem.setOnAction(event -> {
                            Clipboard clipboard = Clipboard.getSystemClipboard();
                            ClipboardContent content = new ClipboardContent();
                            String negPrompt = getNegativePrompt(metadata);
                            content.putString(negPrompt);
                            clipboard.setContent(content);
                        });

                        // menuitem for opening the image
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
                        contextMenu.getItems().addAll(openImageItem, copyPosPromptItem, copyNegPromptItem);

                        stackPane.setOnContextMenuRequested(event -> {
                            contextMenu.show(stackPane, event.getScreenX(), event.getScreenY());
                        });

                        // add the image to the gridpane
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
        imageamout = imageCount;
        amountText.setText(imageamout + " images added to the collection");
    }

    // since sort() shouldnt be called everytime the user wants to copy the prompts
    public String getPositivePrompt(Metadata metadata2) {
                for (Directory directory : metadata2.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        if (tag.toString().contains("[PNG-tEXt]")) {
                            System.out.println(tag.toString());
                            String parameters = tag.toString();
                            positivePrompt = null;
                            if (parameters.contains("Negative prompt")) {
                                positivePrompt = parameters.split("parameters: ")[1].split("Negative prompt")[0];
                            } else if (parameters.contains("Steps")) {
                                positivePrompt = parameters.split("parameters: ")[1].split("Steps")[0];
                            }
                            negativePrompt = null;
                            if (parameters.contains("Negative prompt")) {
                                negativePrompt = parameters.split("Negative prompt: ")[1].split("Steps")[0];
                            }
                        }
                    }
                }
        return positivePrompt;
    }

    public String getNegativePrompt(Metadata metadata2) {
        for (Directory directory : metadata2.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if (tag.toString().contains("[PNG-tEXt]")) {
                    System.out.println(tag.toString());
                    String parameters = tag.toString();
                    negativePrompt = null;
                    if (parameters.contains("Negative prompt")) {
                        negativePrompt = parameters.split("Negative prompt: ")[1].split("Steps")[0];
                    }
                }
            }
        }
        return negativePrompt;
    }
}