package it.polimi.ingsw.CS;
import it.polimi.ingsw.Controller.RoomController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {
    private final ServerSocket serverSocket;
    private Socket connection = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private final RoomController rooms;
    public MyServerSocket(int port,RoomController rooms) throws IOException {
            serverSocket = new ServerSocket(port);
            this.rooms = rooms;
    }
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
