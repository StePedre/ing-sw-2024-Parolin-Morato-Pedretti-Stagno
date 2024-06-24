package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.RoomController;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * The class Server contains only the main method.
 * It is called as the first method in order to set up the server that will contain all the data of the game.
 */

public class Server {

    /**
     * This method sets up the connection (Socket and RMI) needed with the clients.
     *
     * @param args are the parameters passed from the command line.
     */
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
            serverRMI.checkClients();
        } catch (Exception e) {
            System.err.println("Server RMI exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
