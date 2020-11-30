package com.example.vivlio;

import com.example.vivlio.Controllers.BookDetailFetcher;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BookDetailFetcherTest {

    BookDetailFetcher fetcher = new BookDetailFetcher();

    @Test
    public void requestTest() {
        String validIsbn = "9781569319017";
        String invalidIsbn1 = "eqrgewgwer";
        String invalidIsbn2 = "1234";

        // request function already throws exception if it fails
        fetcher.request(validIsbn);
        fetcher.request(invalidIsbn1);
        fetcher.request(invalidIsbn2);
    }

    @Test
    public void getTitleTest() {
        String validIsbn = "9781569319017";
        fetcher.request(validIsbn);
        assertFalse(fetcher.getTitle().isEmpty());
        assertEquals("One Piece", fetcher.getTitle());

        String invalidIsbn = "8971564654";
        fetcher.request(invalidIsbn);
        assertEquals((String)null, fetcher.getTitle());
    }

    @Test
    public void getAuthorTest() {
        String validIsbn = "9781569319017";
        fetcher.request(validIsbn);
        assertFalse(fetcher.getAuthor().isEmpty());
        assertEquals("Eiichiro Oda", fetcher.getAuthor());

        String invalidIsbn = "8971564654";
        fetcher.request(invalidIsbn);
        assertEquals((String)null, fetcher.getAuthor());
    }

    @Test
    public void isFound() {
        String validIsbn = "9781569319017";
        fetcher.request(validIsbn);
        assertTrue(fetcher.isFound());

        String invalidIsbn = "8971564654";
        fetcher.request(invalidIsbn);
        assertFalse(fetcher.isFound());
    }
}
