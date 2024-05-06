package it.polimi.ingsw.CS;
import it.polimi.ingsw.Controller.RoomController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


//pensare a come gestire socket+RMI
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
    public void runServer() throws IOException, ClassNotFoundException {
         while(true){
            System.out.println("Waiting for player\n");
            connection = serverSocket.accept();
             //reader e writer
            oos = new ObjectOutputStream(connection.getOutputStream());
            ois = new ObjectInputStream(connection.getInputStream());
             oos.writeBoolean(true);
            //invio stanze
             oos.writeObject(rooms.getRooms());
             Room room;
             boolean b;
             do { // controllo non esistano stanze con lo stesso nome
                 if ((boolean) ois.readObject()) {
                     room = new Room((String) ois.readObject());
                     //controllo nome stanza
                     if (rooms.alredyExist(room.getName())) {
                         oos.writeObject(true);
                         b= true;
                     } else {
                         oos.writeObject(false);
                         b= false;
                         rooms.addRoom(room);
                     }
                 } else {
                     room = rooms.getRoom((String) ois.readObject());
                     if (rooms.alredyExist(room.getName())) {
                         b=true;
                         oos.writeObject(true);
                     } else {
                         b=false;
                         oos.writeObject(false);
                     }
                 }
             }while (b);
             //chiedere nickname
             String nickname;
             do { // cicla finchè il nickname non è unico per la stanza
                 nickname = (String) ois.readObject();
                 if(rooms.alredyInGame(room.getGame(),nickname)){
                    oos.writeObject(false);
                    b=false;
                 }
                 else{
                     oos.writeObject(true);
                     b=true;
                 }
             }while(b);
            //verificare primo player
            firstPlayer(room);
            //craere connessione parallela
            ServerHandlerSocket client = new ServerHandlerSocket(oos,ois,nickname,room.getGame(),room.getRoundController(),connection);
            Thread t = new Thread (client);
            t.start();
        }
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
}
