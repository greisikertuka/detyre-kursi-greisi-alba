package com.example.detyrekursigreisialba;

import com.example.detyrekursigreisialba.service.QuizService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/add-quiz")
public class AddQuizServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String quizName = request.getParameter("quizName");
        String description = request.getParameter("description");
        String imageURL = request.getParameter("imageURL");
        String questionsCount = request.getParameter("questionsCount");
        String optionsCount = request.getParameter("optionsCount");
        QuizService quizService = new QuizService();
        int quizId = quizService.createQuiz(quizName, description, imageURL);
        response.sendRedirect("add-questions.jsp?quizId=" + quizId + "&questionsCount=" + questionsCount + "&optionsCount=" + optionsCount);
    }
}

