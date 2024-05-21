package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.RoomController;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;


public class Server {
    public static void main(String[] args) {
        RoomController rooms = new RoomController();

        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            MyServerSocket ss = new MyServerSocket(59090, rooms);
            Thread t = new Thread(() -> {
                try {
                    ss.runServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
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
