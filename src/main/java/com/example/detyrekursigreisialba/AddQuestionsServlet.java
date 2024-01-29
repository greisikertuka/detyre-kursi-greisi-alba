package com.example.detyrekursigreisialba;

import com.example.detyrekursigreisialba.model.Option;
import com.example.detyrekursigreisialba.model.Question;
import com.example.detyrekursigreisialba.service.QuizService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/quiz-questions")
public class AddQuestionsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int quizId = Integer.parseInt(request.getParameter("quizId"));
        int questionsCount = Integer.parseInt(request.getParameter("questionsCount"));
        int optionsCount = Integer.parseInt(request.getParameter("optionsCount"));
        QuizService quizService = new QuizService();
        List<Question> questions = quizService.initializeQuestions(questionsCount, optionsCount);
        for (Question question : questions) {
            question.setName(request.getParameter("question" + question.getIndex()));
            for (int optionIndex = 0; optionIndex < question.getOptions().size(); optionIndex++) {
                Option option = question.getOptions().get(optionIndex);
                option.setValue(request.getParameter("optionName" + question.getIndex() + option.getIndex()));
                String radioParam = request.getParameter("optionRadio" + question.getIndex());
                boolean isAnswer = radioParam != null && radioParam.equals("" + option.getIndex());
                option.setAnswer(isAnswer);
            }
        }
        quizService.saveQuestions(quizId, questions);
        response.sendRedirect("dashboard.jsp");
    }
}

