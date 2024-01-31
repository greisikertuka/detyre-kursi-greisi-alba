package com.example.detyrekursigreisialba.service;

import com.example.detyrekursigreisialba.model.Quiz;
import com.example.detyrekursigreisialba.rmi.RemoteQuizService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RMIQuizService extends UnicastRemoteObject implements RemoteQuizService {

    public RMIQuizService() throws RemoteException {
        super();
    }

    @Override
    public List<Quiz> getAllQuizzes() throws RemoteException {
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
}
