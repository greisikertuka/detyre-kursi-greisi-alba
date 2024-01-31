package com.example.detyrekursigreisialba;

import com.example.detyrekursigreisialba.model.User;
import com.example.detyrekursigreisialba.model.enums.UserRole;
import com.example.detyrekursigreisialba.service.DatabaseManager;
import org.postgresql.util.PSQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/auth")
public class AuthenticationServlet extends HttpServlet {
    boolean userNameExists = false;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        if ("register".equals(action)) {
            registerUser(request, response);
        } else if ("login".equals(action)) {
            loginUser(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        User newUser = new User(username, password, email, UserRole.USER);

        try {
            if (saveUserToDatabase(newUser)) {
                response.sendRedirect("login.jsp");
            } else {
                if (userNameExists) {
                    request.setAttribute("registerError", "Username " + username + " already exists");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
                    dispatcher.forward(request, response);
                } else {
                    response.sendRedirect("register-failure.jsp");
                }
            }
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private boolean saveUserToDatabase(User user) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getRole().getValue());
                statement.executeUpdate();
                return true;

            }
        } catch (PSQLException exception) {
            userNameExists = true;
            return false;
        } catch (SQLException exception) {
            return false;
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        HttpSession session = request.getSession(true);
                        session.setAttribute("id", "" + resultSet.getInt("id"));
                        session.setAttribute("username", username);
                        session.setAttribute("email", resultSet.getString("email"));
                        session.setAttribute("role", resultSet.getString("role"));
                        response.sendRedirect("dashboard.jsp");
                    } else {
                        response.sendRedirect("login.jsp?error=1");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login-failure.jsp");
        }
    }
}
