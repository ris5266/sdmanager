package com.example.sdmanager;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;

public class Collection {
    private String name;
    private String profilePicture;
    private String images;

    public Collection(String name, String profilePicture, String images) {
        this.name = name;
        this.profilePicture = profilePicture;
        this.images = images;
    }


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

    public String getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

}