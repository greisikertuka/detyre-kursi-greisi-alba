<%@ include file="common.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>
    function validateForm() {
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

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
        return true;
    }
</script>

<div class="container mt-5">
    <p><a href="dashboard.jsp" class="btn btn-secondary">Back</a></p>
    <c:if test="${param.error ne null}">
        <p class="alert alert-danger">Invalid username or password. Please try again.</p>
    </c:if>
    <form action="auth?action=login" method="post" onsubmit="return validateForm()">
        <div class="mb-3">
            <label for="username" class="form-label">Username:</label>
            <input type="text" id="username" name="username" class="form-control" required>
            <div id="usernameError" style="color: red;"></div>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Password:</label>
            <input type="password" id="password" name="password" class="form-control" required>
            <div id="passwordError" style="color: red;"></div>
        </div>
        <button type="submit" class="btn btn-dark">Login</button>
    </form>
</div>
