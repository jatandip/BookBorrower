package com.example.vivlio.Models;

/**
 * Upload class used to upload image
 */
public class Upload {
    private String name;
    private String imageURL;


    /**
     * constructor for image with no passed in values
     */
    public Upload(){}

    /**
     * constructor using name and imageURL
     * @param name
     * @param imageURL
     */
    public Upload(String name, String imageURL){
        if (name.trim().equals("")) {
            this.name = "No Name";
        }
        this.name = name;
        this.imageURL = imageURL;
    }

    /**
     * get name constructor
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * set the name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /** getting the image url
     *
     * @return
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * setting the image url
     * @param imageURL
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
