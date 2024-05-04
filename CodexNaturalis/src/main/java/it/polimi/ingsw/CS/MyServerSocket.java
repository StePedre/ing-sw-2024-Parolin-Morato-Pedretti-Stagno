package it.polimi.ingsw.CS;
import it.polimi.ingsw.Controller.InitGameController;
import it.polimi.ingsw.Controller.RoundController;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


//pensare a come gestire socket+RMI
public class MyServerSocket {
    private ServerSocket serverSocket = null;
    private Socket connection = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private InitGameController controller;
    Game game=null;
    public MyServerSocket(int port,Game game, InitGameController c) throws IOException {
        serverSocket = new ServerSocket(port);
        this.game=game;
        this.controller=c;
    }
    public void runServer() throws IOException, ClassNotFoundException {
         do{
            System.out.println("Waiting for player\n");
            connection = serverSocket.accept();
             //reader e writer
            oos= new ObjectOutputStream(connection.getOutputStream());
            ois = new ObjectInputStream(connection.getInputStream());
            //raggiunto numero gioactori
             if(game.getNumPlayer() == game.getExpPlayers()) {
                 oos.writeBoolean(false);
                 break;
             }
             oos.writeBoolean(true);
            //chiedere nickname
            String nickname=(String) ois.readObject();
            //verificare primo player
            firstPlayer();
            //craere connessione parallela
            ServerHandlerSocket client = new ServerHandlerSocket(oos,ois,nickname,game,controller);
            Thread t = new Thread (client);
            t.start();
            //game.notifyAll();
        }while(game.getNumPlayer()< game.getExpPlayers());
    }
    private void firstPlayer() throws IOException, ClassNotFoundException {
        if(game.isFirst()) {
            //chiedere num exp player
            oos.writeBoolean(true);
            int n =(int) ois.readObject();
            game.setExpPlayers(n);
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
