<%@ include file="common.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    if (!loggedIn) {
        response.sendRedirect("index.jsp");
    } else if (!role.equals("ADMIN")) {
        response.sendRedirect("dashboard.jsp");
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
        <button type="submit" class="btn btn-dark mb-4">Save Quiz</button>
    </form>
</div>
