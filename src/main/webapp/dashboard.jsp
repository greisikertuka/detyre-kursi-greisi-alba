<%@ include file="common.jsp" %>
<%@ page import="java.util.Objects" %>
<%@ page import="com.example.detyrekursigreisialba.model.Quiz" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.detyrekursigreisialba.service.QuizService" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    session = request.getSession();
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
    if (Objects.isNull(username)) {
        response.sendRedirect("index.jsp");
    }

    QuizService quizService = new QuizService();
    List<Quiz> quizzes = quizService.getAllQuizzes();
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
    <h3>Quizzes</h3>
    <div>
        <%if ("ADMIN".equals(role)) { %>
        <a href="add-quiz.jsp" class="btn btn-outline-dark">Add quiz</a>
        <%}%>
    </div>
    <div class="row">
        <% for (Quiz quiz : quizzes) { %>
        <div class="card mx-2 my-3 shadow-sm" style="width: 18rem;">
            <div class="card-body">
                <h5 class="card-title"><%= quiz.getName() %>
                </h5>
                <p class="card-text"><%= quiz.getDescription() %>
                </p>
                <div style="width: 70px; height: 70px">
                    <img style="width: auto; height: 100%; display:block;" src="<%= quiz.getImage_url() %>"
                         alt=""/>
                </div>
                <br>
                <a class="btn btn-dark" href="quiz.jsp?selectedQuizId=<%=quiz.getId()%>">Start Quiz
                </a>
            </div>
        </div>
        <% } %>
    </div>
</div>
</body>
</html>
