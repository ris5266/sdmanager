package com.example.sdmanager;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
import java.util.Arrays;

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
    Stage stage;


    @Override
    public void start(Stage stage) {
        this.stage = stage;
        grid = new GridPane();
        hbox = new HBox();
        hbox.getChildren().add(grid);
        sort();

        Text settings = new Text("Settings:");
        settings.setFont(new Font(20));

        textpane = new FlowPane();
        textpane.setAlignment(Pos.CENTER);
        textpane.getChildren().add(settings);
        textpane.setHgap(50);
        HBox searchHbox = new HBox();
        Text searchText = new Text("Search through prompts:");
        TextField searchfield = new TextField();
        Button searchButton = new Button("Search");
        searchHbox.getChildren().addAll(searchfield, searchButton);

        rightvbox = new VBox();
        rightvbox.getChildren().addAll(textpane, searchText, searchHbox);
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
