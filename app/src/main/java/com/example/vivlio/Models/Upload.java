package com.example.vivlio.Models;

public class Upload {
    private String name;
    private String imageURL;

    public Upload(){}

    public Upload(String name, String imageURL){
        if (name.trim().equals("")) {
            this.name = "No Name";
        }
        this.name = name;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
