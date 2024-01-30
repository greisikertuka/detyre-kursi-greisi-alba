<%@ page import="com.example.detyrekursigreisialba.model.Result" %>
<%@ page import="com.example.detyrekursigreisialba.service.QuizService" %>
<%@ page import="java.util.List" %>
<%@ include file="common.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    if (!loggedIn) {
        response.sendRedirect("index.jsp");
    }
    String username = (String) session.getAttribute("username");
    QuizService quizService = new QuizService();
    List<Result> results = quizService.getAllResults(username);
%>
<div class="container mt-5">
    <p><a href="dashboard.jsp" class="btn btn-secondary">Back</a></p>
    <h2>My Quizzes</h2>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">Quiz Name</th>
            <th scope="col">Date</th>
            <th scope="col">Score</th>
        </tr>
        </thead>
        <tbody>
        <% for (Result result : results) { %>
        <tr>
            <td><%=result.getQuizName()%>
            </td>
            <td><%=result.getTimestamp()%>
            </td>
            <td><%=45%>%</td>
        </tr>
        <%}%>
        </tbody>
    </table>
</div>
