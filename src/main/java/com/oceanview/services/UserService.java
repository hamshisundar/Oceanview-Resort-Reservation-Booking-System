package com.oceanview.services;

import com.oceanview.dao.UserDAO;
import com.oceanview.models.User;

import java.util.Optional;

/**
 * Business logic for user authentication and registration.
 */
public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public Optional<User> login(String emailOrUsername, String password) {
        if (emailOrUsername == null || emailOrUsername.trim().isEmpty() || password == null)
            return Optional.empty();
        return userDAO.login(emailOrUsername.trim(), password);
    }

    public boolean register(User user) {
        if (user == null || user.getName() == null || user.getEmail() == null || user.getPassword() == null)
            return false;
        if (user.getName().trim().isEmpty() || user.getEmail().trim().isEmpty() || user.getPassword().isEmpty())
            return false;
        if (userDAO.findByEmail(user.getEmail().trim()).isPresent())
            return false;
        return userDAO.register(user);
    }

    public Optional<User> findById(int userId) {
        return userDAO.findById(userId);
    }

    public Optional<User> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) return Optional.empty();
        return userDAO.findByEmail(email.trim());
    }
}
