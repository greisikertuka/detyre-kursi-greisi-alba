package com.example.detyrekursigreisialba.rmi;

import com.example.detyrekursigreisialba.service.RMIQuizService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteQuizServer {

    public static void main(String[] args) {
        try {
            RemoteQuizService remoteQuizService = new RMIQuizService();
            RemoteQuizService stub = (RemoteQuizService) UnicastRemoteObject.exportObject(remoteQuizService, 0);

            Registry registry = LocateRegistry.createRegistry(1098);
            registry.rebind("RemoteQuizService", stub);

            System.out.println("RemoteQuizServer ready");
        } catch (RemoteException e) {
            System.err.println("RemoteQuizServer exception:");
            e.printStackTrace();
        }
    }
}
