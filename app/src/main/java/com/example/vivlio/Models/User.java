package com.example.vivlio.Models;

import java.io.Serializable;



/**
 * This is the user class; it contains the following information about the book
 */

public class User implements Serializable {
    private String name;
    private String username;
    private String email;
    private String phonenumber;
    private String isbn;
    private String borrower;
    private String ownedBookStatus;
    private String uid;
    private String photoUrl;

    /**
     * Constructor for the user class
     * @param name
     * @param username
     * @param email
     * @param phonenumber
     */
    public User(String name, String username, String email, String phonenumber) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    /**
     * Constructor for user class but this one has additional information
     * @param name
     * @param username
     * @param email
     * @param phonenumber
     * @param isbn
     * @param borrower
     */
    public User(String name, String username, String email, String phonenumber, String isbn, String borrower) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.isbn = isbn;
        this.borrower = borrower;
    }


    /**
     * Constructor for the user class
     * @param uid
     * @param name
     * @param username
     * @param ownedBookStatus
     * @param photoUrl
     */
    public User(String uid, String name, String username, String ownedBookStatus, String photoUrl) {
        this.uid = uid;
        this.name = name;
        this.username = username;
        this.ownedBookStatus = ownedBookStatus;
        this.photoUrl = photoUrl;
    }

    /**
     * returns photo url
     * @return
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    /**
     * set the photo url
     * @param photoUrl
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * get the borrowers
     * @return
     */
    public String getBorrower() {
        return borrower;
    }


    /**
     * set the borrowers
     * @param borrower
     */
    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    /**
     * get the isbn
     * @return
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * set the isbn
     * @param isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * get the name of the user
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * set the name of the user
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * get the username
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * set the username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get the email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * set the email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get the phonenumber
     * @return
     */
    public String getPhonenumber() {
        return phonenumber;
    }

    /**
     * set the phonenumber
     * @param phonenumber
     */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }


    /**
     * get the status of the book
     * @return
     */
    public String getOwnedBookStatus() {
        return ownedBookStatus;
    }

    /**
     * set the book status
     * @param ownedBookStatus
     */
    public void setOwnedBookStatus(String ownedBookStatus) {
        this.ownedBookStatus = ownedBookStatus;
    }

    /**
     * get the uid of the user
     * @return
     */
    public String getUid() {
        return uid;
    }

    /**
     * set the uid
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
}
