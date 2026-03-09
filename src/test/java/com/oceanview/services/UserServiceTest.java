package com.oceanview.services;

import com.oceanview.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserService validation logic (no database).
 * Login/register with DB are covered by integration/manual tests.
 */
class UserServiceTest {

    @Test
    @DisplayName("Login with null email returns empty")
    void testLoginNullEmail() {
        UserService service = new UserService();
        assertTrue(service.login(null, "password").isEmpty());
    }

    @Test
    @DisplayName("Login with empty email returns empty")
    void testLoginEmptyEmail() {
        UserService service = new UserService();
        assertTrue(service.login("", "password").isEmpty());
        assertTrue(service.login("   ", "password").isEmpty());
    }

    @Test
    @DisplayName("Login with null password returns empty")
    void testLoginNullPassword() {
        UserService service = new UserService();
        assertTrue(service.login("test@example.com", null).isEmpty());
    }

    @Test
    @DisplayName("Register with null user returns false")
    void testRegisterNullUser() {
        UserService service = new UserService();
        assertFalse(service.register(null));
    }

    @Test
    @DisplayName("Register with null name returns false")
    void testRegisterNullName() {
        UserService service = new UserService();
        User user = new User(null, "test@example.com", "", "pass123");
        assertFalse(service.register(user));
    }

    @Test
    @DisplayName("Register with empty email returns false")
    void testRegisterEmptyEmail() {
        UserService service = new UserService();
        User user = new User("Test", "", "", "pass123");
        assertFalse(service.register(user));
    }

    @Test
    @DisplayName("Register with null password returns false")
    void testRegisterNullPassword() {
        UserService service = new UserService();
        User user = new User("Test", "test@example.com", "", null);
        assertFalse(service.register(user));
    }
}
