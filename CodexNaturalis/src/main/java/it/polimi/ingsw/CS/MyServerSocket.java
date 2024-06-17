package it.polimi.ingsw.CS;
import it.polimi.ingsw.Controller.RoomController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The class MyServerSocket manages the connection of new players once the room is created.
 * It has a ServerSocket and a Socket in order to establish the connection, an ObjectOutputStream and an ObjectInputStream
 * to send and receives Objects to and from the client.
 * It also has a RoomController to manage the rooms.
 */

public class MyServerSocket {
    private final ServerSocket serverSocket;
    private Socket connection = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private final RoomController rooms;

    /**
     * Class constructor.
     * It sets the server socket and the rooms.
     *
     * @param port is the port number of the server.
     * @param rooms is the rooms to set.
     * @throws IOException if the position of the card does not belong to available positions set.
     */
    public MyServerSocket(int port,RoomController rooms) throws IOException {
            serverSocket = new ServerSocket(port);
            this.rooms = rooms;
    }

    /**
     * This method accepts a player whenever it tries to connect to the server.
     * Once the connection is established, it calls the server handler socket that manages all the game flow.
     * If there is any problem, it closes the connection.
     *
     * @throws IOException if there has been problems regarding input or output.
     */
    public void runServer() throws IOException {
         try {
             while (true) {
                 System.out.println("Waiting for player\n");
                 connection = serverSocket.accept();
                 connection.setSoTimeout(0);
                 oos = new ObjectOutputStream(connection.getOutputStream());
                 ois = new ObjectInputStream(connection.getInputStream());
                 ServerHandlerSocket client = new ServerHandlerSocket(oos, ois, rooms, connection);
                 Thread t = new Thread(client);
                 t.start();
             }
         }
         catch (IOException e) {
             connection.close();
             throw e;
         }
    }
}
