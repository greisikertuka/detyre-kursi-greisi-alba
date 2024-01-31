package com.example.detyrekursigreisialba.model;

import java.util.Date;
import java.util.List;

public class Result {

    private int id;
    private int quizId;
    private String username;
    private List<UserAnswer> userAnswers;

    private Date timestamp;

    private double score;
    private String quiz_name;

    public Result(int quizId, String username, List<UserAnswer> answers, Date timestamp) {
        this.quizId = quizId;
        this.username = username;
        this.userAnswers = answers;
        this.timestamp = timestamp;
    }

    public Result() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getUsername() {
        return username;
    }

    public List<UserAnswer> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(List<UserAnswer> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuizName() {
        return quiz_name;
    }

    public void setQuizName(String quiz_name) {
        this.quiz_name = quiz_name;
    }
}
