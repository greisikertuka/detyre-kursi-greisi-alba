<%@ page import="com.example.detyrekursigreisialba.model.Result" %>
<%@ page import="com.example.detyrekursigreisialba.service.QuizService" %>
<%@ page import="com.example.detyrekursigreisialba.model.UserAnswer" %>
<%@ include file="common.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    if (!loggedIn) {
        response.sendRedirect("index.jsp");
    }
    String resultId = request.getParameter("resultId");
    QuizService quizService = new QuizService();
    Result result = quizService.getResultWithAnswers(resultId);
    double score = 0.0;
    if (!result.getUserAnswers().isEmpty()) {
        for (int index = 0; index < result.getUserAnswers().size(); index++) {
            if (result.getUserAnswers().get(index).isAnswer()) {
                score += 1;
            }
        }
        score = (score / result.getUserAnswers().size()) * 100.0;
    }

%>
<div class="container mt-5">
    <p><a href="dashboard.jsp" class="btn btn-secondary">Back</a></p>
    <h3>Quiz Results</h3>
    <h4>Your score:</h4>
    <h4 class="text-success"><%=("" + score).substring(0, 4)%>%</h4>
    <div class="column">
        <% for (UserAnswer userAnswer : result.getUserAnswers()) { %>

        <%if (!userAnswer.isAnswer()) { %>
        <div class="d-block p-2 border border-danger rounded-right"><%= userAnswer.getQuestionName() %>
        </div>
        <div class="text-danger"><%=userAnswer.getOptionName()%>
        </div>
        <%}%>

        <%if (userAnswer.isAnswer()) { %>
        <div class="d-block p-2 border border-success rounded-right"><%= userAnswer.getQuestionName() %>
        </div>
        <div class="text-success"><%=userAnswer.getOptionName()%>
        </div>
        <%}%>
        <br>
        <%}%>
    </div>
    <a href="my-quizzes.jsp" class="btn btn-dark">My Quizzes</a>
</div>
