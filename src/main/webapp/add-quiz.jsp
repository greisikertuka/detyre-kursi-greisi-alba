<%@ page import="java.util.Objects" %>
<%@ include file="common.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    session = request.getSession();
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
    if (Objects.isNull(username) || !role.equals("ADMIN")) {
        response.sendRedirect("index.jsp");
    }
%>

<div class="container mt-5">
    <h2 class="header-panel">Add New Quiz</h2>
    <p><a href="dashboard.jsp" class="btn btn-secondary">Back</a></p>
    <form action="add-quiz" method="post">
        <div class="mb-3">
            <label for="quizName" class="form-label">Quiz Name:</label>
            <input type="text" id="quizName" name="quizName" class="form-control" required>
        </div>
        <div class="mb-3">
            <label for="description" class="form-label">Description:</label>
            <input type="text" id="description" name="description" class="form-control" required>
        </div>
        <div class="mb-3">
            <label for="imageURL" class="form-label">Image URL:</label>
            <input type="text" id="imageURL" name="imageURL" class="form-control" required>
        </div>
        <div class="mb-3">
            <label for="questionsCount" class="form-label">Questions Count</label>
            <input type="number" id="questionsCount" name="questionsCount" class="form-control" required>
        </div>
        <div class="mb-3">
            <label for="optionsCount" class="form-label">Number of options for questions:</label>
            <input type="number" id="optionsCount" name="optionsCount" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-primary mb-4">Save Quiz</button>
    </form>
</div>
