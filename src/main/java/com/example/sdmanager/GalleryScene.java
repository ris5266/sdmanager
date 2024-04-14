package com.example.sdmanager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
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
import org.json.JSONException;
import java.io.File;
import java.io.IOException;

public class GalleryScene extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    GridPane imagesGrid;
    Label nocharacters;
    FlowPane nocharacterspane;
    TextField searchinput;
    private Text amountText;
    Collection[] collection;
    Scene modalScene;
    HBox teiler;
    Stage stage;

    // read out collection information
    public GalleryScene() throws IOException {
        try {
            collection = ConfigReader.readCollectionInformation();
        } catch (
                JSONException e) {
            collection = null;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        load();
    }

    // Create a modal window to create a new collection
    private void createCollectionModal() {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);

        VBox layout = new VBox();
        TextField nameInput = new TextField();
        nameInput.setPromptText("Collection Name");

        HBox profilePictureInputBox = new HBox();
        TextField profilePictureInput = new TextField();
        profilePictureInput.setPromptText("Profile Picture URL");
        Button profilePictureButton = new Button("Select Image");

        profilePictureButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(modal);
            if (file != null) {
                profilePictureInput.setText(file.toURI().toString());
            }
        });

        profilePictureInputBox.getChildren().addAll(profilePictureInput, profilePictureButton);

        HBox imagesInputBox = new HBox();
        TextField imagesInput = new TextField();
        imagesInput.setPromptText("Image Folder");
        Button imagesButton = new Button("Select Folder");

        imagesButton.setStyle("-fx-background-color: white");
        profilePictureButton.setStyle("-fx-background-color: white");

        imagesInputBox.getChildren().addAll(imagesInput, imagesButton);
        profilePictureInput.setEditable(false);
        imagesInput.setEditable(false);

        Button submit = new Button("Create Collection");
        submit.setStyle("-fx-background-color: #cc7a68");

        imagesButton.setOnMouseEntered(e -> {
            imagesButton.setStyle("-fx-background-color: #e98c78");
        });

        imagesButton.setOnMouseExited(e -> {
            imagesButton.setStyle("-fx-background-color: white");
        });

        profilePictureButton.setOnMouseEntered(e -> {
            profilePictureButton.setStyle("-fx-background-color: #e98c78");
        });

        profilePictureButton.setOnMouseExited(e -> {
                    profilePictureButton.setStyle("-fx-background-color: white");
                }
        );

        imagesButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File directory = directoryChooser.showDialog(modal);
            if (directory != null) {
                imagesInput.setText(directory.getAbsolutePath());
            }
        });

        submit.setOnMouseEntered(e -> {
            submit.setStyle("-fx-background-color: #e98c78");
        });

        submit.setOnMouseExited(e -> {
            submit.setStyle("-fx-background-color: #cc7a68");
        });

        submit.setOnAction(e -> {
            String name = nameInput.getText();
            String profilePictureUrl = profilePictureInput.getText();
            String imageUrls = imagesInput.getText();

            File imageDirectory = new File(imageUrls);

            if (!profilePictureUrl.isEmpty() && !imageUrls.isEmpty() && !name.isEmpty() && profilePictureUrl.startsWith("file:/") && imageDirectory.isDirectory()) {
                Collection collection = new Collection(name, profilePictureUrl, imageUrls);
                try {
                    ConfigReader.writeCollectionInformations(collection);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                modal.close();
                try {
                    GalleryScene galleryScene = new GalleryScene();
                    galleryScene.start(stage);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                if(name.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Collection Name must not be empty.");
                    alert.showAndWait();
                }
                // show an error message if the profile picture URL does not start with "file:/"
                if (!profilePictureUrl.startsWith("file:/")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Profile picture must contain a image from your computer.");
                    alert.showAndWait();
                }
                // show an error message if the imageUrls does not represent a valid directory
                if (!imageDirectory.isDirectory()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Image Folder must be a valid directory.");
                    alert.showAndWait();
                }
            }
        });
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setSpacing(5);
        Text title = new Text("Create Collection");
        title.setFont(new Font(20));
        layout.getChildren().add(title);
        layout.getChildren().addAll(nameInput, profilePictureInputBox, imagesInputBox, submit);

        modalScene = new Scene(layout);
        modal.setResizable(false);
        modal.setScene(modalScene);
        modal.showAndWait();
    }

    public void load() {
        // hbox for the left and right side
        teiler = new HBox();
        teiler.setPrefHeight(800);
        teiler.setPrefWidth(1200);

        // header for the left side
        VBox header = new VBox();
        teiler.getChildren().add(header);
        header.setPrefHeight(400);
        header.setPrefWidth(201);
        teiler.setAlignment(Pos.TOP_LEFT);

        // software name + input
        FlowPane softwarepane = new FlowPane();
        Text softwarename = new Text("Diffusion Depot");
        softwarepane.getChildren().add(softwarename);
        softwarepane.setPadding(new Insets(10, 0, 0, 0));
        softwarename.setFont(new Font(19));
        softwarepane.setAlignment(Pos.CENTER);
        softwarepane.setPrefHeight(39);
        softwarepane.setPrefWidth(222);

        FlowPane inputpane = new FlowPane();
        searchinput = new TextField();
        inputpane.setAlignment(Pos.CENTER);
        searchinput.setDisable(true);
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
        home.setStyle("-fx-background-color: #cc7a68");
        images.setStyle("-fx-background-color: white");
        prompts.setStyle("-fx-background-color: white");

        add.setOnMouseEntered(e -> {
            add.setStyle("-fx-background-color: #e98c78");
        });
        add.setOnMouseExited(e -> {
            add.setStyle("-fx-background-color: #cc7a68");
        });

        add.setOnAction(e -> {
            createCollectionModal();
        });

        images.setOnMouseEntered(e -> {
            images.setStyle("-fx-background-color: #e98c78");
        });
        images.setOnMouseExited(e -> {
            images.setStyle("-fx-background-color: white");
        });

        prompts.setOnMouseEntered(e -> {
            prompts.setStyle("-fx-background-color: #e98c78");
        });
        prompts.setOnMouseExited(e -> {
            prompts.setStyle("-fx-background-color: white");
        });

        // switch windows
        prompts.setOnAction(e -> {
            PromptsScene galleryScene = null;
            try {
                galleryScene = new PromptsScene();
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

        // right side layout
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
        softwarename.setFont(Font.font("Verdana", FontWeight.BOLD, 17));

        // if theres no collections found a different layout will be shown instead of the collections
        if (collection == null || collection.length == 0) {
            nocharacterspane = new FlowPane();
            nocharacters = new Label("no collections found ☹\uFE0F");
            nocharacters.setAlignment(Pos.CENTER);
            nocharacterspane.setAlignment(Pos.CENTER);
            nocharacterspane.setPrefHeight(1167);
            nocharacterspane.setPrefWidth(568);
            nocharacterspane.getChildren().add(nocharacters);
            imagesVbox.getChildren().addAll(hboxright, nocharacterspane);
            teiler.getChildren().add(imagesVbox);
        } else {
            amountText = new Text(collection.length + " collections added to viewport");
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

            // go through the collections and display them with the menu option to delete it
            int row = 0;
            int column = 0;
            for (Collection coll : collection) {
                ImageView profilePicture = new ImageView(coll.getProfilePicture());
                profilePicture.setFitHeight(300);
                profilePicture.setFitWidth(300);

                Label collectionName = new Label(coll.getName());
                collectionName.setTextFill(Color.WHITE);

                Rectangle overlay = new Rectangle(300, 300, Color.color(0, 0, 0, 0.45));

                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(profilePicture, overlay, collectionName);

                // Add an EventHandler for left click
                stackPane.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        // Create a new Stage for the modal window
                        Stage modal = new Stage();
                        modal.initModality(Modality.APPLICATION_MODAL);
                        modal.initOwner(stage);

                        // Create a new instance of CollectionInsideScene and start it in the modal window
                        CollectionInsideScene collectionInsideScene = null;
                        try {
                            collectionInsideScene = new CollectionInsideScene(coll);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        collectionInsideScene.start(modal);
                    }
                });

                ContextMenu contextMenu = new ContextMenu();
                MenuItem deleteCollection = new MenuItem("Delete Collection");
                contextMenu.getItems().addAll(deleteCollection);

                deleteCollection.setOnAction(e -> {
                    try {
                        ConfigReader.deleteCollectionInformation(coll);
                        // Reload the scene to reflect the changes
                        GalleryScene galleryscene = new GalleryScene();
                        galleryscene.start(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                stackPane.setOnContextMenuRequested(event -> contextMenu.show(stackPane, event.getScreenX(), event.getScreenY()));
                imagesGrid.add(stackPane, column, row);

                column++;
                if (column > 2) {
                    column = 0;
                    row++;
                }
            }
            imagesVbox.getChildren().add(imagesGrid);
        }

        Scene scene = new Scene(teiler, 1200, 800);
        stage.setResizable(false);
        stage.getIcons().add(new Image("icon.jpg"));
        stage.setScene(scene);
        stage.setTitle("Diffusion Depot");
        stage.show();
    }
}
