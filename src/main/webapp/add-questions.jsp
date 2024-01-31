<%@ include file="common.jsp" %>
<%@ page import="com.example.detyrekursigreisialba.model.Question" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.detyrekursigreisialba.service.QuizService" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    if (!loggedIn) {
        response.sendRedirect("index.jsp");
    } else if (!role.equals("ADMIN")) {
        response.sendRedirect("dashboard.jsp");
    }
    int quizId = Integer.parseInt(request.getParameter("quizId"));
    int questionsCount = Integer.parseInt(request.getParameter("questionsCount"));
    int optionsCount = Integer.parseInt(request.getParameter("optionsCount"));
    QuizService quizService = new QuizService();
    List<Question> questions = quizService.initializeQuestions(questionsCount, optionsCount);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Quiz</title>
</head>
<body>
<div class="container mt-5">
    <p><a href="add-quiz.jsp" class="btn btn-secondary">Back</a></p>
    <form action="quiz-questions" method="post">
        <input type="hidden" name="quizId" value="<%=quizId%>">
        <input type="hidden" name="questionsCount" value="<%=questionsCount%>">
        <input type="hidden" name="optionsCount" value="<%=optionsCount%>">
        <c:forEach var="question" items="<%=questions%>">
            <label for="question${question.index}"> Question ${question.index} name:
                <input type="text" name="question${question.index}" id="question${question.index}" required>
            </label>
            <c:forEach var="option" items="${question.options}">
                <div class="px-3">
                    <label for="optionName${question.index}${option.index}"> Option ${option.index} name
                        <input type="text" name="optionName${question.index}${option.index}"
                               id="optionName${question.index}${option.index}" required>
                    </label>
                    <label>
                        <input type="radio" name="optionRadio${question.index}" value="${option.index}" required>
                    </label>
                        ${option.value}
                </div>
            </c:forEach>
            <br><br>
        </c:forEach>
        <div>
            <button type="submit" class="btn btn-dark">Save Questions</button>
        </div>
    </form>
</div>
</body>
</html>
