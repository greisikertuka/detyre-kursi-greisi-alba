package com.example.detyrekursigreisialba.service;

import com.example.detyrekursigreisialba.model.*;

import java.sql.*;
import java.time.LocalDate;
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
                     "INSERT INTO results (quiz_id, username, timestamp) VALUES (?, ?, ?);",
                     Statement.RETURN_GENERATED_KEYS
             )
        ) {
            preparedStatement.setInt(1, result.getQuizId());
            preparedStatement.setString(2, result.getUsername());
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
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

    private void setScoreForResult(int resultId, Connection connection, double score) {
        String sql = "UPDATE results SET score = ? WHERE id = ?";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setDouble(1, score);
            statement.setInt(2, resultId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int saveUserQuizResults(Result result, List<UserAnswer> userAnswers) {
        int resultId = saveResult(result);
        double score = 0;
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO user_answers (result_id, question_id, option_id) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (UserAnswer userAnswer : userAnswers) {
                    System.out.println(userAnswer.isAnswer());
                    if (userAnswer.isAnswer()) {
                        score += 1;
                    }
                    statement.setInt(1, resultId);
                    statement.setInt(2, userAnswer.getQuestionId());
                    statement.setInt(3, userAnswer.getOptionId());
                    statement.addBatch();
                }
                statement.executeBatch();
                score = score / userAnswers.size();
                setScoreForResult(resultId, connection, score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultId;
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

    public Result getResultWithAnswers(String resultId) {
        Result result = new Result();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM results WHERE id = ?")
        ) {
            preparedStatement.setInt(1, Integer.parseInt(resultId));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int quizId = resultSet.getInt("quiz_id");
                    String username = resultSet.getString("username");
                    Date timestamp = resultSet.getDate("timestamp");
                    double score = resultSet.getDouble("score");
                    result = new Result(quizId, username, new ArrayList<>(), timestamp);
                    result.setScore(score);
                    List<UserAnswer> answers = getUserAnswersForQuiz(resultId);
                    result.setUserAnswers(answers);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<UserAnswer> getUserAnswersForQuiz(String resultId) {
        List<UserAnswer> answers = new ArrayList<>();
        String sql = "SELECT a.id, a.question_id, a.option_id, q.name AS question_name, o.value AS option_name, o.is_answer AS is_answer " +
                "FROM user_answers a " +
                "JOIN questions q ON a.question_id = q.id " +
                "JOIN options o ON a.option_id = o.id " +
                "WHERE a.result_id = ?";


        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, Integer.parseInt(resultId));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int questionId = resultSet.getInt("question_id");
                    int optionId = resultSet.getInt("option_id");
                    String questionName = resultSet.getString("question_name");
                    String optionName = resultSet.getString("option_name");
                    boolean isAnswer = resultSet.getBoolean("is_answer");

                    UserAnswer answer = new UserAnswer(Integer.parseInt(resultId), questionId, optionId);
                    answer.setId(id);
                    answer.setQuestionName(questionName);
                    answer.setOptionName(optionName);
                    answer.setAnswer(isAnswer);
                    answers.add(answer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }

    public List<Result> getAllResults(String username) {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT r.id, r.quiz_id, r.timestamp, r.score, q.name AS quiz_name " +
                "FROM results r " +
                "JOIN quizzes q ON r.quiz_id = q.id " +
                "WHERE r.username = ?";


        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int quiz_id = resultSet.getInt("quiz_id");
                    Date timestamp = resultSet.getDate("timestamp");
                    double score = resultSet.getDouble("score");
                    String quiz_name = resultSet.getString("quiz_name");
                    Result result = new Result();
                    result.setId(id);
                    result.setQuizId(quiz_id);
                    result.setTimestamp(timestamp);
                    result.setScore(score);
                    result.setQuizName(quiz_name);
                    results.add(result);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public boolean deleteQuizById(int quizId) {
        try (Connection connection = DatabaseManager.getConnection()) {
            deleteUserAnswersAndResults(connection, quizId);
            String deleteQuizSQL = "DELETE FROM quizzes WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuizSQL)) {
                preparedStatement.setInt(1, quizId);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void deleteUserAnswersAndResults(Connection connection, int quizId) throws SQLException {
        String deleteUserAnswersSql = "DELETE FROM user_answers WHERE result_id IN (SELECT id FROM results WHERE quiz_id = ?)";
        try (PreparedStatement deleteUserAnswersStatement = connection.prepareStatement(deleteUserAnswersSql)) {
            deleteUserAnswersStatement.setInt(1, quizId);
            deleteUserAnswersStatement.executeUpdate();
        }
        String deleteResultsSql = "DELETE FROM results WHERE quiz_id = ?";
        try (PreparedStatement deleteResultsStatement = connection.prepareStatement(deleteResultsSql)) {
            deleteResultsStatement.setInt(1, quizId);
            deleteResultsStatement.executeUpdate();
        }
    }
}
