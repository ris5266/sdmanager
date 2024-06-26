package com.example.sdmanager;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BootupScene extends Application {
    // components
    private Button submit;
    private GridPane pane;
    private Text tutorial;
    private TextField path;
    private Button inputButton;
    private File folderpath;
    // layouts
    private VBox vbox;
    private GridPane inputField;
    private VBox submitVbox;
    private VBox mainVbox;
    private Scene scene;
    private Stage stage;
    private Image icon;
    private ImageView iconView;
    private Text title;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        pane = new GridPane();
        vbox = new VBox();
        mainVbox = new VBox();
        submitVbox = new VBox();
        path = new TextField();
        inputButton = new Button("+");
        inputField = new GridPane();
        submit = new Button("Submit");
        icon = new Image("icon.jpg");
        iconView = new ImageView(icon);
        title = new Text("Diffusion Depot");
        tutorial = new Text("please select your sd output folder:");

        // styling
        inputButton.setStyle("-fx-background-color: white");
        iconView.setFitHeight(150);
        iconView.setFitWidth(150);
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-size: 30px");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        submit.setStyle("-fx-background-color: #white");
        tutorial.setFill(Color.WHITE);
        pane.setAlignment(Pos.CENTER);

        // disable submit button
        submit.setDisable(true);
        path.setEditable(false);

        // hover effects
        submit.setOnMouseEntered(e -> {
            submit.setStyle("-fx-background-color: #b6b6b6");
        });
        submit.setOnMouseExited(e -> {
            submit.setStyle("-fx-background-color: #white");
        });

        // action events
        DirectoryChooser directorychooser = new DirectoryChooser();
        inputButton.setOnAction(e -> {
            folderpath = directorychooser.showDialog(stage);
            if(folderpath != null) {
                path.setText(folderpath.getAbsolutePath());
                submit.setDisable(false);
            }
        });

        submit.setOnAction(e -> {
            try {
                submit();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Check if config file exists
        if (Files.exists(Paths.get(("config.json")
        ))) {
            File savedPath = ConfigReader.returnImagePath();
            if (savedPath != null) {
                GalleryScene gallery = new GalleryScene();
                gallery.start(stage);
                return;
            }
        }

        // field + button
        inputField.add(path, 0, 0);
        inputField.add(inputButton, 1,  0);
        inputField.setAlignment(Pos.CENTER);

        // submit button
        submitVbox.getChildren().add(submit);
        submitVbox.setAlignment(Pos.CENTER);

        // combine layouts
        vbox.getChildren().addAll(title, iconView, tutorial);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(3);

        pane.add(vbox, 0, 0);
        pane.add(inputField, 0, 1);
        pane.add(submitVbox, 0, 5);
        pane.setVgap(15);
        mainVbox.getChildren().addAll(pane, submitVbox);
        mainVbox.setSpacing(30);
        mainVbox.setAlignment(Pos.CENTER);

        scene = new Scene(mainVbox, 800, 600);
        pane.setStyle("-fx-background-color: #744237");
        vbox.setStyle("-fx-background-color: #744237");
        mainVbox.setStyle("-fx-background-color: #744237");

        stage.setTitle("Diffusion Depot");
        stage.getIcons().add(new Image("icon.jpg"));
        stage.setScene(scene);

        stage.show();
    }

    private void submit() throws IOException {
        if(folderpath != null) {
            ConfigReader.writeImagePath(folderpath.getAbsolutePath());
            GalleryScene gallery = null;
            gallery = new GalleryScene();
            gallery.start(stage);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
