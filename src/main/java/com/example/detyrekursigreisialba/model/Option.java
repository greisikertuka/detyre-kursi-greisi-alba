package com.example.detyrekursigreisialba.model;

public class Option {
    private int id;
    private int questionId;
    private int index;
    private String value;
    private boolean isAnswer;

    public Option(int id, int questionId, int index, String value, boolean isAnswer) {
        this.id = id;
        this.questionId = questionId;
        this.index = index;
        this.value = value;
        this.isAnswer = isAnswer;
    }

    public boolean isCorrectAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getId() {
        return id;
    }

    public Option() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
