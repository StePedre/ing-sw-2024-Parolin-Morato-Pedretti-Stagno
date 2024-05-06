package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.RoomController;

import java.io.IOException;


public class Server {
    public static void main(String[] args) {
        RoomController rooms = new RoomController(); // passarlo ai server

        //c.InitializeGame();
        try {
            MyServerSocket ss = new MyServerSocket(59090, rooms);
            ss.runServer();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
