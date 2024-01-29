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
import java.sql.SQLException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    boolean userNameExists = false;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        UserRole role = UserRole.fromString(request.getParameter("role"));
        int id = Integer.parseInt(request.getParameter("id"));

        User newUser = new User(username, "", email, UserRole.USER);

        try {
            if (saveProfileChanges(id, newUser)) {
                HttpSession session = request.getSession(true);
                session.removeAttribute("id");
                session.removeAttribute("username");
                session.removeAttribute("email");
                session.removeAttribute("role");
                session.setAttribute("id", "" + id);
                session.setAttribute("username", username);
                session.setAttribute("email", email);
                session.setAttribute("role", role.getValue());

                response.sendRedirect("dashboard.jsp");
            } else {
                request.setAttribute("registerError", "Username " + username + " is taken");
                RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
                dispatcher.forward(request, response);
            }
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private boolean saveProfileChanges(int id, User newUser) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "UPDATE users " +
                    "SET username = ?, " +
                    "email = ? " +
                    "WHERE id = ?;";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newUser.getUsername());
                statement.setString(2, newUser.getEmail());
                statement.setInt(3, id);  // Use setInt for the ID
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
}