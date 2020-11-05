package com.example.vivlio;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String username;
    private String email;
    private String phonenumber;
    private String isbn;
    private String borrower;

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

}
