package com.example.vivlio;

import com.example.vivlio.Models.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

    private User user;

    @Test
    public void testGet() {
        user = new User("test name", "test username", "test@test.com", "1111111111");

        assertEquals("test name", user.getName());
        assertEquals("test username", user.getUsername());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("1111111111", user.getPhonenumber());
    }

    @Test
    public void testSet() {
        user = new User("test name", "test username", "test@test.com", "1111111111");

        user.setName("new name");
        user.setUsername("new username");
        user.setEmail("new@test.com");
        user.setPhonenumber("2222222222");

        assertEquals("new name", user.getName());
        assertEquals("new username", user.getUsername());
        assertEquals("new@test.com", user.getEmail());
        assertEquals("2222222222", user.getPhonenumber());
    }


}
