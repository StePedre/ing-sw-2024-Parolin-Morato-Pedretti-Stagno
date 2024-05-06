package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.RoomController;

import java.io.IOException;


public class Server {
    public static void main(String[] args) {
        RoomController rooms = new RoomController(); // passarlo ai server

        //c.InitializeGame();
        try {
            MyServerSocket ss = new MyServerSocket(59090, rooms);
            Thread t = new Thread(() -> {
                try {
                    ss.runServer();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
            t.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            ServerRMIInterface serverRMI = new MyServerRMI(1099, rooms);
            serverRMI.runServer();
        } catch (Exception e) {
            System.err.println("Server RMI exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
