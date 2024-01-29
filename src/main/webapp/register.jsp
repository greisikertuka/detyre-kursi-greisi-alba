<%@ include file="common.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>
    function validateForm() {
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        const email = document.getElementById("email").value;

        if (username.length < 8) {
            document.getElementById("usernameError").innerHTML = "Username must be at least 8 characters.";
            return false;
        } else {
            document.getElementById("usernameError").innerHTML = "";
        }
        if (password.length < 8) {
            document.getElementById("passwordError").innerHTML = "Password must be at least 8 characters.";
            return false;
        } else if (!password.match(".*[a-zA-Z].*")) {
            document.getElementById("passwordError").innerHTML = "Password must contain letters.";
            return false;
        } else if (!password.match(".*\\d.*")) {
            document.getElementById("passwordError").innerHTML = "Password must contain numbers";
            return false;
        } else {
            document.getElementById("passwordError").innerHTML = "";
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
    <p><a href="dashboard.jsp" class="btn btn-secondary">Back</a></p>
    <form action="auth?action=register" method="post" onsubmit="return validateForm()">
        <div class="mb-3">
            <label for="username" class="form-label">Username:</label>
            <input type="text" id="username" name="username" class="form-control">
            <div id="usernameError" style="color: red;"></div>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Password:</label>
            <input type="password" id="password" name="password" class="form-control">
            <div id="passwordError" style="color: red;"></div>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email:</label>
            <input type="text" id="email" name="email" class="form-control">
            <div id="emailError" style="color: red;"></div>
            <c:if test="${not empty requestScope.registerError}">
                <div class="error-message" style="color: red;">${requestScope.registerError}</div>
            </c:if>
        </div>
        <button type="submit" class="btn btn-primary">Register</button>
    </form>
</div>
