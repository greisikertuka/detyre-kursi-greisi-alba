package com.example.detyrekursigreisialba.model;

import java.util.List;

public class Quiz {
    private int id;
    private String name;
    private String description;
    private String image_url;

    public Quiz() {
    }

    public Quiz(String name, List<Question> questions, String description, String image_url) {
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.questions = questions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    private List<Question> questions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
