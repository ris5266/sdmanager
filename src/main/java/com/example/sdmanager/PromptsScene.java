package com.example.sdmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

        profilePictureInput.setEditable(false);

        profilePictureButton.setStyle("-fx-background-color: white");
        posPrompt.setStyle("-fx-background-color: white");
        negPrompt.setStyle("-fx-background-color: white");



        profilePictureButton.setOnMouseEntered(e -> {
            profilePictureButton.setStyle("-fx-background-color: #e98c78");
        });

        profilePictureButton.setOnMouseExited(e -> {
                    profilePictureButton.setStyle("-fx-background-color: white");
                }
        );

        Button submit = new Button("Create Prompt");
        submit.setStyle("-fx-background-color: #cc7a68");

        submit.setOnMouseEntered(e -> {
            submit.setStyle("-fx-background-color: #e98c78");
        });

        submit.setOnMouseExited(e -> {
            submit.setStyle("-fx-background-color: #cc7a68");
        });

        submit.setOnAction(e -> {
            String pPrompt = posPrompt.getText();
            String nPrompt = negPrompt.getText();
            String profilePictureUrl = profilePictureInput.getText();

            if (!profilePictureUrl.isEmpty() && !pPrompt.isEmpty() && !nPrompt.isEmpty()) {
                Prompt finalprompt = new Prompt(pPrompt, nPrompt, profilePictureUrl);
                try {
                    ConfigReader.writePromptsInformation(finalprompt);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                modal.close();
                try {
                    PromptsScene promptScene = new PromptsScene();
                    promptScene.start(stage);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                if(pPrompt.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Positive Prompt must not be empty.");
                    alert.showAndWait();
                }
                if(nPrompt.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Negative Prompt must not be empty.");
                    alert.showAndWait();
                }
                // Show an error message if the profile picture URL does not start with "file:/"
                if (!profilePictureUrl.startsWith("file:/")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Profile picture must contain a image from your computer.");
                    alert.showAndWait();
                }
            }
        });
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setSpacing(5);
        Text title = new Text("Create Prompt");
        title.setFont(new Font(20));

        layout.getChildren().add(title);
        layout.getChildren().addAll(posPrompt, negPrompt, profilePictureInputBox, submit);

        modalScene = new Scene(layout);
        modal.setResizable(false);

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

        softwarename.setFont(Font.font("Verdana", FontWeight.BOLD, 17));


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

        Button add = new Button("Add");
        add.setStyle("-fx-background-color: #cc7a68");
        add.setPrefWidth(70);
        add.setPrefHeight(26);

        add.setOnMouseEntered(e -> {
            add.setStyle("-fx-background-color: #e98c78");
        });
        add.setOnMouseExited(e -> {
            add.setStyle("-fx-background-color: #cc7a68");
        });

        add.setOnAction(e -> {
            createCollectionModal();
        });

        home.setStyle("-fx-background-color: white");
        images.setStyle("-fx-background-color: white");
        prompts.setStyle("-fx-background-color: #cc7a68");

        home.setOnMouseEntered(e -> {
            home.setStyle("-fx-background-color: #e98c78");
        });
        home.setOnMouseExited(e -> {
            home.setStyle("-fx-background-color: white");

        });

        images.setOnMouseEntered(e -> {
            images.setStyle("-fx-background-color: #e98c78");
        });
        images.setOnMouseExited(e -> {
            images.setStyle("-fx-background-color: white");
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
            settings.setStyle("-fx-background-color: #e98c78");
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
            scrollPane.setStyle("-fx-focus-color: transparent");
            scrollPane.setStyle("-fx-faint-focus-color: transparent");
            scrollPane.setStyle("-fx-background-color: transparent");

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
                posPrompt.setWrapText(true); // Enable text wrapping
                posPrompt.setMaxWidth(300); // Set the maximum width to the width of the image
                posPrompt.setPadding(new Insets(10)); // Add a margin of 10 pixels on all sides
                posPrompt.setAlignment(Pos.CENTER);

                Rectangle overlay = new Rectangle(300, 300, Color.color(0, 0, 0, 0.45));

                overlay.setOpacity(0);
                posPrompt.setOpacity(0);


                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(profilePicture, overlay, posPrompt);

                // When the mouse enters the image area, set the opacity of the overlay and the prompt text back to its original value
                stackPane.setOnMouseEntered(event -> {
                    overlay.setOpacity(0.8);
                    posPrompt.setOpacity(1);
                });

                // When the mouse exits the image area, set the opacity of the overlay and the prompt text to 0
                stackPane.setOnMouseExited(event -> {
                    overlay.setOpacity(0);
                    posPrompt.setOpacity(0);
                });

                // Create context menu
                ContextMenu contextMenu = new ContextMenu();

                // Create menu items
                MenuItem copyPosPrompt = new MenuItem("Copy Positive Prompt");
                MenuItem copyNegPrompt = new MenuItem("Copy Negative Prompt");
                MenuItem deletePrompt = new MenuItem("Delete Prompt");

                // Add menu items to context menu
                contextMenu.getItems().addAll(copyPosPrompt, copyNegPrompt, deletePrompt);

                // Set actions for menu items
                copyPosPrompt.setOnAction(e -> {
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(coll.getPosPrompt());
                    clipboard.setContent(content);
                });

                copyNegPrompt.setOnAction(e -> {
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(coll.getNegPrompt());
                    clipboard.setContent(content);
                });

                deletePrompt.setOnAction(e -> {
                    try {
                        ConfigReader.deletePromptInformation(coll);
                        // Reload the scene to reflect the changes
                        PromptsScene promptsScene = new PromptsScene();
                        promptsScene.start(stage);

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                // Set context menu on stack pane
                stackPane.setOnContextMenuRequested(event -> contextMenu.show(stackPane, event.getScreenX(), event.getScreenY()));

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
