<%@ page import="java.util.Objects" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    session = request.getSession();
    boolean loggedOut = Objects.isNull(session.getAttribute("username"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
    <title>Quizzes Online</title>
</head>
<body>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.1/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<header class="bg-dark p-2">
    <nav class="navbar navbar-expand-sm navbar-dark bg-dark">
        <a class="navbar-brand" href="dashboard.jsp">Quizzes Online</a>
        <%if (!loggedOut) { %>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="profile.jsp"><i class="fas fa-user"></i> Profile</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="logout"><i class="fas fa-sign-out-alt"></i> Log Out</a>
            </li>
        </ul>
        <%}%>
        <%if (loggedOut) { %>
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="login.jsp"><i class="fas fa-sign-in-alt"></i> Log In</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="register.jsp"><i class="fas fa-user"></i> Register</a>
            </li>
        </ul>
        <%}%>
    </nav>
</header>
<div class="card">
</div>
</body>
</html>
