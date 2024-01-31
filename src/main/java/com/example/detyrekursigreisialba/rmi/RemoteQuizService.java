package com.example.detyrekursigreisialba.rmi;

import com.example.detyrekursigreisialba.model.Quiz;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteQuizService extends Remote {
    List<Quiz> getAllQuizzes() throws RemoteException;
}
