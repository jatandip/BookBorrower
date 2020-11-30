package com.example.vivlio;

import com.example.vivlio.Models.Book;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BookTest {

    private Book book;

    @Test
    public void testGet() {
        book = new Book("test title", "test author", "1234", "available", "test owner", "test owner", "link");

        assertEquals("test title", book.getTitle());
        assertEquals("test author", book.getAuthor());
        assertEquals("1234", book.getISBN());
        assertEquals("available", book.getStatus());
        assertEquals("test owner", book.getOwner());
        assertEquals("test owner", book.getCurrentOwner());
        assertEquals("link", book.getPhotoURL());
    }

    @Test
    public void testSet() {
        book = new Book("test title", "test author", "1234", "available", "test owner", "test owner", "link");

        book.setTitle("new title");
        book.setAuthor("new author");
        book.setISBN("5678");
        book.setStatus("pending");
        book.setOwner("new owner");
        book.setCurrentOwner("new owner");
        book.setPhotoURL("new link");

        assertEquals("new title", book.getTitle());
        assertEquals("new author", book.getAuthor());
        assertEquals("5678", book.getISBN());
        assertEquals("pending", book.getStatus());
        assertEquals("new owner", book.getOwner());
        assertEquals("new owner", book.getCurrentOwner());
        assertEquals("new link", book.getPhotoURL());

    }
}
