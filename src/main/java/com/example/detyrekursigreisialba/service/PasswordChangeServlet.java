package com.example.detyrekursigreisialba.service;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/change-password")
public class PasswordChangeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        String password = request.getParameter("password");
        String newPassword = request.getParameter("newPassword");
        UserService userService = new UserService();

        try {
            if (userService.checkPassword(id, password)) {
                userService.updatePassword(id, newPassword);
                response.sendRedirect("profile.jsp");
            } else {
                request.setAttribute("error", "Wrong password!");
                RequestDispatcher dispatcher = request.getRequestDispatcher("change-password.jsp");
                dispatcher.forward(request, response);
            }
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
