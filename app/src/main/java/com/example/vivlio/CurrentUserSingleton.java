package com.example.vivlio;

public class CurrentUserSingleton {

    public User user;
    public BookList currentBooks;


    public CurrentUserSingleton(User user, BookList currentBooks) {
        this.user = user;
        this.currentBooks = currentBooks;
    }


    public CurrentUserSingleton(User user) {
        this.user = user;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BookList getCurrentBooks() {
        return currentBooks;
    }

    public void setCurrentBooks(BookList currentBooks) {
        this.currentBooks = currentBooks;
    }
}
