package com.example.sdmanager;

// This class is a model class that represents a prompt object
public class Prompt {
    private String posPrompt;
    private String negPrompt;
    private String profilePicture;

    Prompt(String posPrompt, String negPrompt, String profilePicture) {
        this.posPrompt = posPrompt;
        this.negPrompt = negPrompt;
        this.profilePicture = profilePicture;
    }

    public String getNegPrompt() {
        return negPrompt;
    }

    public String getPosPrompt() {
        return posPrompt;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setNegPrompt(String negPrompt) {
        this.negPrompt = negPrompt;
    }

    public void setPosPrompt(String posPrompt) {
        this.posPrompt = posPrompt;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
