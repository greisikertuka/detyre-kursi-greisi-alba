<%@ page import="java.util.Objects" %>
<%@ include file="common.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    session = request.getSession();
    String id = (String) session.getAttribute("id");
    String username = (String) session.getAttribute("username");
    String email = (String) session.getAttribute("email");
    String role = (String) session.getAttribute("role");
    if (Objects.isNull(username)) {
        response.sendRedirect("index.jsp");
    }
%>
<script>
    function validateForm() {
        const username = document.getElementById("username").value;
        const email = document.getElementById("email").value;

        if (username.length < 8) {
            document.getElementById("usernameError").innerHTML = "Username must be at least 8 characters.";
            return false;
        } else {
            document.getElementById("usernameError").innerHTML = "";
        }
        if (!email.match("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            document.getElementById("emailError").innerHTML = "Please provide a valid email.";
            return false;
        } else {
            document.getElementById("emailError").innerHTML = "";
        }
        return true;
    }
</script>
<div class="container mt-5">
    <h2 class="header-panel">Profile</h2><br><br>
    <div class="row">
        <a href="dashboard.jsp" class="btn btn-secondary mr-3">Back</a>
        <a href="change-password.jsp" class="btn btn-warning">Change password</a>
    </div>
    <br>
    <form action="profile" method="post" onsubmit="return validateForm()">
        <input type="hidden" id="id" value="<%=id%>" name="id" class="form-control">
        <input type="hidden" id="role" value="<%=role%>" name="role" class="form-control">
        <div class="mb-3">
            <label for="username" class="form-label">Username:</label>
            <input type="text" id="username" value="<%=username%>" name="username" class="form-control">
            <div id="usernameError" style="color: red;"></div>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email:</label>
            <input type="text" id="email" name="email" value="<%=email%>" class="form-control">
            <div id="emailError" style="color: red;"></div>
            <c:if test="${not empty requestScope.registerError}">
                <div class="error-message" style="color: red;">${requestScope.registerError}</div>
            </c:if>
        </div>
        <button type="submit" class="btn btn-dark">Update Profile</button>
    </form>
</div>
