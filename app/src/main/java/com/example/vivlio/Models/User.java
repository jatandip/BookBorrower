package com.example.vivlio.Models;

import java.io.Serializable;

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

    public User(String name, String username, String email, String phonenumber) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public User(String name, String username, String email, String phonenumber, String isbn, String borrower) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phonenumber = phonenumber;
        this.isbn = isbn;
        this.borrower = borrower;
    }

    public User(String uid, String name, String username, String ownedBookStatus, String photoUrl) {
        this.uid = uid;
        this.name = name;
        this.username = username;
        this.ownedBookStatus = ownedBookStatus;
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getOwnedBookStatus() {
        return ownedBookStatus;
    }

    public void setOwnedBookStatus(String ownedBookStatus) {
        this.ownedBookStatus = ownedBookStatus;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
