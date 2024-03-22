package com.example.sdmanager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.IOException;

public class ImageScene extends Application {
    File folderpath;

    public ImageScene(File folderpath) {
        this.folderpath = folderpath;
    }

    public static void main(String[] args) {
        launch(args);
    }

    HBox hbox;
    ImageView pictures;
    VBox rightvbox;
    GridPane grid;
    FlowPane textpane;
    DatePicker datePicker;
    Stage stage;
    CheckBox safe;


    @Override
    public void start(Stage stage) {
        this.stage = stage;
        grid = new GridPane();
        hbox = new HBox();
        ScrollPane scrollPane = new ScrollPane();
// Set the GridPane as the content of the ScrollPane
        scrollPane.setContent(grid);
        hbox.getChildren().add(scrollPane);
        sort();

        Text settings = new Text("Settings:");
        settings.setFont(new Font(20));

        textpane = new FlowPane();
        textpane.setAlignment(Pos.CENTER);
        textpane.getChildren().add(settings);
        textpane.setHgap(50);
        textpane.setPadding(new Insets(10, 10, 0, 10));
        HBox searchHbox = new HBox();
        Text searchText = new Text("Search through prompts:");
        TextField searchfield = new TextField();
        Button searchButton = new Button("Search");
        searchHbox.getChildren().addAll(searchfield, searchButton);
        VBox searchvbox = new VBox();
        searchvbox.getChildren().addAll(searchText, searchHbox);

        rightvbox = new VBox();

        Text datetext = new Text("Date:");
        datePicker = new DatePicker();


        FlowPane safepane = new FlowPane();
        Text safetext = new Text(" Safe");
        safe = new CheckBox();
        safe.setSelected(true);
        safepane.getChildren().addAll(safe, safetext);



        Text dimensionstext = new Text("Dimensions:");
        ChoiceBox<String> dimensions = new ChoiceBox<>();
        dimensions.getItems().addAll("All", "300x300", "600x600", "900x900");
        dimensions.setValue("All");

        rightvbox.getChildren().addAll(textpane, searchText, searchvbox, datetext, datePicker, dimensionstext, dimensions, safepane);
        rightvbox.setSpacing(10);
        rightvbox.setPadding(new Insets(0, 10, 0, 10));
        hbox.getChildren().add(rightvbox);


        Scene galleryScene = new Scene(hbox, 1200, 720);

        stage.setScene(galleryScene);

        stage.setTitle("Gallery");
        stage.show();

    }


    public void sort() {
        int j = 0;
        String[] imageExtensions = new String[]{"jpg", "png", "gif", "bmp", "jpeg"};
        File[] files = folderpath.listFiles();

        if (files == null) {
            System.out.println("No files found");
            return;
        }

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
                        grid.add(pictures, i % 3, j);
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
