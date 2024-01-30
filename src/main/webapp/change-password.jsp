<%@ include file="common.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String id = (String) session.getAttribute("id");
    if (!loggedIn) {
        response.sendRedirect("index.jsp");
    }
%>
<script>
    function validateForm() {
        const password = document.getElementById("password").value;
        const newPassword = document.getElementById("newPassword").value;
        const confirmPassword = document.getElementById("confirmPassword").value;
        if (password.length < 8) {
            document.getElementById("passwordError").innerHTML = "Password must be at least 8 characters.";
            return false;
        } else {
            document.getElementById("passwordError").innerHTML = "";
        }
        if (newPassword.length < 8) {
            document.getElementById("newPasswordError").innerHTML = "Password must be at least 8 characters.";
            return false;
        } else if (!newPassword.match(".*[a-zA-Z].*")) {
            document.getElementById("newPasswordError").innerHTML = "Password must contain letters.";
            return false;
        } else if (!newPassword.match(".*\\d.*")) {
            document.getElementById("newPasswordError").innerHTML = "Password must contain numbers";
            return false;
        } else {
            document.getElementById("newPasswordError").innerHTML = "";
        }
        if (confirmPassword.length < 8) {
            document.getElementById("confirmPasswordError").innerHTML = "Password must be at least 8 characters.";
            return false;
        } else if (!confirmPassword.match(".*[a-zA-Z].*")) {
            document.getElementById("confirmPasswordError").innerHTML = "Password must contain letters.";
            return false;
        } else if (!confirmPassword.match(".*\\d.*")) {
            document.getElementById("confirmPasswordError").innerHTML = "Password must contain numbers";
            return false;
        } else {
            document.getElementById("confirmPasswordError").innerHTML = "";
        }
        if (newPassword !== confirmPassword) {
            document.getElementById("error").innerHTML = "Passwords should match!";
            return false;
        } else {
            document.getElementById("error").innerHTML = "";
        }
        return true;
    }
</script>
<div class="container mt-5">
    <h2 class="header-panel">Change password</h2><br>
    <p><a href="profile.jsp" class="btn btn-secondary">Back</a></p><br>
    <form action="change-password" method="post" onsubmit="return validateForm()">
        <input type="hidden" id="id" value="<%=id%>" name="id" class="form-control">
        <div class="mb-3">
            <label for="password" class="form-label">Password:</label>
            <input type="password" id="password" name="password" class="form-control">
            <div id="passwordError" style="color: red;"></div>
        </div>
        <div class="mb-3">
            <label for="newPassword" class="form-label">New Password:</label>
            <input type="password" id="newPassword" name="newPassword" class="form-control">
            <div id="newPasswordError" style="color: red;"></div>
        </div>
        <div class="mb-3">
            <label for="confirmPassword" class="form-label">Confirm Password:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control">
            <div id="confirmPasswordError" style="color: red;"></div>
        </div>
        <div id="error" style="color: red;"></div>
        <button type="submit" class="btn btn-dark">Update Profile</button>
        <br>
    </form>
    <c:if test="${not empty requestScope.error}">
        <div class="error-message" style="color: red;">${requestScope.error}</div>
    </c:if>
</div>
