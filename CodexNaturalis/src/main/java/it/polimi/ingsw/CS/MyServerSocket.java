package it.polimi.ingsw.CS;
import it.polimi.ingsw.Controller.RoomController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


//pensare a come gestire socket+RMI
public class MyServerSocket {
    private ServerSocket serverSocket = null;
    private Socket connection = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private RoomController rooms = null;
    public MyServerSocket(int port,RoomController rooms) throws IOException {
        serverSocket = new ServerSocket(port);
        this.rooms = rooms;
    }
    public void runServer() throws IOException, ClassNotFoundException {
         do{
            System.out.println("Waiting for player\n");
            connection = serverSocket.accept();
             //reader e writer
            oos = new ObjectOutputStream(connection.getOutputStream());
            ois = new ObjectInputStream(connection.getInputStream());
             oos.writeBoolean(true);
            //invio stanze
             oos.writeObject(rooms.getRooms());
             Room room;
             if((boolean)ois.readObject()){
                 room = new Room((String) ois.readObject());
                 rooms.addRoom(room);
             }
             else{
                 room = rooms.getRoom((String) ois.readObject());
             }
             //chiedere nickname
            String nickname=(String) ois.readObject();
            //verificare primo player
            firstPlayer(room);
            //craere connessione parallela
            ServerHandlerSocket client = new ServerHandlerSocket(oos,ois,nickname,room.getGame(),room.getRoundController());
            Thread t = new Thread (client);
            t.start();
        }while(true);
    }
    private void firstPlayer(Room room) throws IOException, ClassNotFoundException {// implementare nella stanza
        if(room.getGame().isFirst()) {
            //chiedere num exp player
            oos.writeObject(true);
            int n =(int) ois.readObject();
            room.getGame().setExpPlayers(n);
        }
        else{
            oos.writeBoolean(false);
        }
    }
    public void close() throws IOException {
        connection.close();
        serverSocket.close();
    }
}
