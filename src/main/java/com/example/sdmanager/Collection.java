package com.example.sdmanager;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

// This class is a model class that represents a collection of images.
public class Collection {
    private String name;
    private String profilePicture;
    private String imagefolderpath;

    public Collection(String name, String profilePicture, String imagefolderpath) {
        this.name = name;
        this.profilePicture = profilePicture;
        this.imagefolderpath = imagefolderpath;
    }

    // This method creates a node that represents the collection.
    public Node createNode() {
        VBox box = new VBox();
        Label nameLabel = new Label(name);
        ImageView profilePictureView = new ImageView(profilePicture);
        box.getChildren().addAll(nameLabel, profilePictureView);
        return box;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getImageFolderPath() {
        return imagefolderpath;
    }

    public String getName() {
        return name;
    }

    public void setImageFolderPath(String imagefolderpath) {
        this.imagefolderpath = imagefolderpath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

}