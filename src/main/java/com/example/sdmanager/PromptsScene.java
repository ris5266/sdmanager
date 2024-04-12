package com.example.sdmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class PromptsScene extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    File imagepath;

    GridPane imagesGrid;
    VBox imagesVbox;
    Label nocharacters;
    FlowPane nocharacterspane;

    private Text amountText;

    int characteramount = 0;
    Prompt[] promptsObject;
    Scene modalScene;
    Scene scene;
    HBox teiler;
    Stage stage;


    public PromptsScene() throws IOException {
        try {
            promptsObject = ConfigReader.readPromptsInformation();
        } catch (JSONException e) {
            promptsObject = null;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        load();
    }


    private void createCollectionModal() {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);

        HBox profilePictureInputBox = new HBox();
        TextField profilePictureInput = new TextField();
        profilePictureInput.setPromptText("Profile Picture URL");
        Button profilePictureButton = new Button("Select Image");

        VBox layout = new VBox();
        TextField posPrompt = new TextField();
        posPrompt.setPromptText("Positive Prompt");

        VBox layout2 = new VBox();
        TextField negPrompt = new TextField();
        negPrompt.setPromptText("Negative Prompt");


        profilePictureButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(modal);
            if (file != null) {
                profilePictureInput.setText(file.toURI().toString());
            }
        });

        profilePictureInputBox.getChildren().addAll(profilePictureInput, profilePictureButton);

        Button submit = new Button("Create Prompt");
        submit.setOnAction(e -> {
            String pPrompt = posPrompt.getText();
            String nPrompt = negPrompt.getText();
            String profilePictureUrl = profilePictureInput.getText();

            if (!profilePictureUrl.isEmpty() && !pPrompt.isEmpty() && !nPrompt.isEmpty()) {
                Prompt finalprompt = new Prompt(pPrompt, profilePictureUrl, nPrompt);
                try {
                    ConfigReader.writePromptsInformation(finalprompt);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                modal.close();
            } else {
            }
        });

        layout.getChildren().addAll(posPrompt, negPrompt, profilePictureInputBox, submit);

        modalScene = new Scene(layout);
        modal.setScene(modalScene);
        modal.showAndWait();
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public void load() {
        // teilter teilt in linke und rechte hälfte
        teiler = new HBox();
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

        add.setOnAction(e -> {
            createCollectionModal();
        });

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

        home.setOnAction(e -> {
            GalleryScene galleryScene = null;
            try {
                galleryScene = new GalleryScene();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            galleryScene.start(stage);
        });

        images.setOnAction(e -> {
            ImagesScene imageScene = null;
            try {
                imageScene = new ImagesScene();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            imageScene.start(stage);
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

        settings.setOnAction(e -> {
            SettingsScene settingsScene = null;
            try {
                settingsScene = new SettingsScene(stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            settingsScene.start(stage);
            stage.showAndWait();
        });
        header.getChildren().addAll(softwarepane, inputpane, buttonpane, settingspane);
        // rechte hälfte mit amountnamen und images

        VBox imagesVbox = new VBox();
        imagesVbox.setPrefHeight(800);
        imagesVbox.setPrefWidth(980);
        FlowPane amountPane = new FlowPane();

        amountPane.setPrefHeight(92);
        amountPane.setPrefWidth(924);
        amountText = new Text("");
        amountText.setFont(new Font(14));
        amountPane.getChildren().add(amountText);

        HBox hboxright = new HBox();
        hboxright.setPadding( new Insets(12, 0, 0, 0));
        hboxright.setPrefWidth(400);
        hboxright.setPrefHeight(54);
        hboxright.getChildren().addAll(amountPane, add);



        if (promptsObject == null || promptsObject.length == 0) {
            nocharacterspane = new FlowPane();
            nocharacters = new Label("no prompts found ☹\uFE0F");
            nocharacters.setAlignment(Pos.CENTER);
            nocharacterspane.setAlignment(Pos.CENTER);
            nocharacterspane.setPrefHeight(1167);
            nocharacterspane.setPrefWidth(568);
            nocharacterspane.getChildren().add(nocharacters);
            imagesVbox.getChildren().addAll(hboxright, nocharacterspane);

            teiler.getChildren().add(imagesVbox);

        } else {
            amountText = new Text(promptsObject.length + " prompts added to viewport");
            amountPane.getChildren().add(amountText);

            amountPane.setPrefHeight(92);
            amountPane.setPrefWidth(924);
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
            hboxright.setPadding( new Insets(12, 0, 0, 0));
            hboxright.setPrefWidth(400);
            hboxright.setPrefHeight(54);

            imagesVbox.getChildren().addAll(hboxright, scrollPane);
            teiler.getChildren().add(imagesVbox);



            int row = 0;
            int column = 0;
            for (Prompt coll : promptsObject) {
                ImageView profilePicture = new ImageView(coll.getProfilePicture());
                profilePicture.setFitHeight(300);
                profilePicture.setFitWidth(300);

                Label posPrompt = new Label(coll.getPosPrompt());
                posPrompt.setTextFill(Color.WHITE);


                Rectangle overlay = new Rectangle(300, 300, Color.color(0, 0, 0, 0.5));

                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(profilePicture, overlay, posPrompt);

                imagesGrid.add(stackPane, column, row); // Add to the calculated cell of the grid

                // Update row and column for next collection
                column++;
                if (column > 2) { // If column is more than 2, reset it to 0 and increase row by 1
                    column = 0;
                    row++;
                }
            }

            imagesVbox.getChildren().add(imagesGrid);
        }
        Scene scene = new Scene(teiler, 1200, 800);
        stage.setResizable(false);

        stage.setScene(scene);

        stage.setTitle("Diffusion Depot");
        stage.show();


    }
}
