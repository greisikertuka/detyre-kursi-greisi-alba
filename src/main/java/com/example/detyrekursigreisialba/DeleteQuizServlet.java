package com.example.detyrekursigreisialba;

import com.example.detyrekursigreisialba.service.QuizService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/delete-quiz")
public class DeleteQuizServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int quizId = Integer.parseInt(request.getParameter("quizId"));
        QuizService quizService = new QuizService();
        if (quizService.deleteQuizById(quizId)) {
            response.sendRedirect("dashboard.jsp");
        }
    }
}

