package com.example.detyrekursigreisialba.service;

import com.example.detyrekursigreisialba.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizService {

    public QuizService() {
    }

    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM quizzes")) {

            while (resultSet.next()) {
                Quiz quiz = new Quiz();
                quiz.setId(resultSet.getInt("id"));
                quiz.setName(resultSet.getString("name"));
                quiz.setDescription(resultSet.getString("description"));
                quiz.setImage_url(resultSet.getString("image_url"));
                quizzes.add(quiz);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizzes;
    }

    public int createQuiz(String quizName, String description, String imageURL) {
        int generatedResultId = -1;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO quizzes (name, description, image_url) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, quizName);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, imageURL);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedResultId = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return generatedResultId;
    }

    public List<Question> getQuestionsWithOptions(int quizId) {
        Map<Integer, Question> questionMap = new HashMap<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM questions WHERE quiz_id = ?")
        ) {
            preparedStatement.setInt(1, quizId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int questionId = resultSet.getInt("id");
                    Question question = questionMap.computeIfAbsent(questionId, id -> {
                        int qQuizId = 0;
                        int qIndex = 0;
                        String qName = null;
                        try {
                            qQuizId = resultSet.getInt("quiz_id");
                            qIndex = resultSet.getInt("index");
                            qName = resultSet.getString("name");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return new Question(questionId, qQuizId, qIndex, qName, new ArrayList<>());
                    });
                    List<Option> options = getOptionsForQuestion(questionId);
                    question.getOptions().addAll(options);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(questionMap.values());
    }

    private List<Option> getOptionsForQuestion(int questionId) {
        List<Option> options = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM options WHERE question_id = ?")
        ) {
            preparedStatement.setInt(1, questionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String value = resultSet.getString("value");
                    boolean isAnswer = resultSet.getBoolean("is_answer");
                    int index = resultSet.getInt("index");
                    options.add(new Option(id, questionId, index, value, isAnswer));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return options;
    }

    public int saveResult(Result result) {
        int generatedResultId = -1;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO results (quiz_id, username) VALUES (?, ?);",
                     Statement.RETURN_GENERATED_KEYS
             )
        ) {
            preparedStatement.setInt(1, result.getQuizId());
            preparedStatement.setString(2, result.getUsername());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedResultId = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedResultId;
    }

    public void saveUserQuizResults(Result result, List<UserAnswer> userAnswers) {
        int resultId = saveResult(result);
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO user_answers (result_id, question_id, option_id) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (UserAnswer userAnswer : userAnswers) {
                    statement.setInt(1, resultId);
                    statement.setInt(2, userAnswer.getQuestionId());
                    statement.setInt(3, userAnswer.getOptionId());
                    statement.addBatch();
                }
                statement.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Question> initializeQuestions(int questionsCount, int optionsCount) {
        List<Question> questions = new ArrayList<>();
        for (int index = 0; index < questionsCount; index++) {
            Question question = new Question();
            List<Option> options = new ArrayList<>();
            for (int optionIndex = 0; optionIndex < optionsCount; optionIndex++) {
                Option option = new Option();
                option.setIndex(optionIndex + 1);
                options.add(option);
            }
            question.setIndex(index + 1);
            question.setOptions(options);
            questions.add(question);
        }
        return questions;
    }

    public void saveQuestions(int quizId, List<Question> questions) {
        for (Question question : questions) {
            insertQuestionWithOptions(quizId, question);
        }
    }

    private void insertQuestionWithOptions(int quizId, Question question) {
        int generatedResultId = -1;
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO questions (quiz_id, index, name) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, quizId);
            statement.setInt(2, question.getIndex());
            statement.setString(3, question.getName());
            statement.addBatch();
            statement.executeBatch();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedResultId = generatedKeys.getInt(1);
            }
            insertOptionsForQuestion(generatedResultId, question.getOptions());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertOptionsForQuestion(int question_id, List<Option> options) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO options (question_id, value, is_answer) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            for (Option option : options) {
                statement.setInt(1, question_id);
                statement.setString(2, option.getValue());
                statement.setBoolean(3, option.isCorrectAnswer());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
