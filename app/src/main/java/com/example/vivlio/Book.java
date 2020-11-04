package com.example.vivlio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the book class; it contains the following information about the book:
 * title, author, ISBN, status, owner (lender), and currentOwner (borrower/lender)
 */
public class Book implements Serializable {
    private String title;
    private String author;
    private String ISBN;
    private String status;
    private String owner;
    private String currentOwner;
    private ArrayList<String> currentOwners;
    private String photoURL;

    public Book(String title, String author, String ISBN, String status, String owner, String currentOwner, String photoURL){
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.status = status;
        this.owner = owner;
        this.currentOwner = currentOwner;
        this.photoURL = photoURL;
    }

    //for Borrow Task
    public Book(String title, String author, String owner){
        this.title = title;
        this.author = author;
        this.owner = owner;
    }

    /**
     * Constructor for Book in the Search Fragment
     * @param title title of the book
     * @param author book's author
     * @param ISBN book's ISBN
     * @param currentOwners list of all current owners
     */
    public Book(String title, String author, String ISBN, ArrayList<String> currentOwners) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.currentOwners = currentOwners;
    }

    public Book() {}

    /**
     * getter for Title
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * setter for Title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * getter for Author
     * @return String
     */
    public String getAuthor() {
        return author;
    }

    /**
     * setter for Author
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * getter for ISBN
     * @return String
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * setter for ISBN
     * @param ISBN
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * getter for Status
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     * setter for Status
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * getter for Owner
     * @return User
     */
    public String getOwner() {
        return owner;
    }

    /**
     * setter for Owner
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * getter for currentOwner
     * @return User
     */
    public String getCurrentOwner() {
        return currentOwner;
    }

    /**
     * setter for currentOwner
     * @param currentOwner
     */
    public void setCurrentOwner(String currentOwner) {
        this.currentOwner = currentOwner;
    }

    /**
     * getter for photoURL
     * @return String
     */
    public String getPhotoURL(){
        return photoURL;
    }

    /**
     * setter for photoURL
     * @param photoURL
     */
    public void setPhotoURL(String photoURL){
        this.photoURL = photoURL;
    }

    /**
     * getter for current Owner list
     * @return currentOwners
     */
    public ArrayList<String> getCurrentOwners() {
        return currentOwners;
    }

    /**
     * setter for Current owners list
     * @param currentOwners
     */
    public void setCurrentOwners(ArrayList<String> currentOwners) {
        this.currentOwners = currentOwners;
    }

    /**
     * returns description of book
     * @return List
     */
    public List<String> getBookInfo(){
        return Arrays.asList(title, author, ISBN, status);
    }
}
