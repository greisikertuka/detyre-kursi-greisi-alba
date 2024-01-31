package com.example.detyrekursigreisialba.service;

import com.example.detyrekursigreisialba.model.User;
import com.example.detyrekursigreisialba.model.enums.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public UserService() {
    }

    public User getUserById(int userId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT id, username, password, email, role FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        String email = resultSet.getString("email");
                        String role = resultSet.getString("role");

                        User user = new User(username, password, email, UserRole.fromString(role));
                        user.setId(id);
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkPassword(int userId, String providedPassword) {
        User user = getUserById(userId);

        if (user != null) {
            String currentPassword = user.getPassword();
            return currentPassword.equals(providedPassword);
        }
        return false;
    }

    public void updatePassword(int userId, String newPassword) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "UPDATE users SET password = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newPassword);
                statement.setInt(2, userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
