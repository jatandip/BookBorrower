package com.example.vivlio;

import android.content.Context;

import java.util.ArrayList;

public class BookList {

    public ArrayList<Book> books;
    public Context context;


    public BookList(ArrayList<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    public BookList(Context context) {
        this.context = context;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
