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
    //condividere exp e num con RMI
    Game game=null;
    public MyServerSocket(int port,Game game, InitGameController c) throws IOException {
        serverSocket = new ServerSocket(port);
        this.game=game;//trovare porta su cui lavorare e aggiungere try-catch
        this.controller=c;
    }
    public void runServer() throws IOException, ClassNotFoundException {
         do{
             if(game.getNumPlayer() == game.getExpPlayers()) {
             oos.writeObject("reached max numbers of player\n");
             //lanciare eccezione numero giocatori
             break;
             } //finchè ho 0 giocatori o ho meno giocatori di quelli attesi, come sincronizzo con RMI??
            System.out.println("Waiting for player\n");
            connection = serverSocket.accept();
            //reader e writer
            oos= new ObjectOutputStream(connection.getOutputStream());
            ois = new ObjectInputStream(connection.getInputStream());
            //raggiunto numero gioactori

            //chiedere nickname
            oos.writeObject("Insert your nickname :\n");
            String nickname=(String) ois.readObject();
            //verificare primo player
            firstPlayer();
            //craere connessione parallela
            ServerHandlerSocket client = new ServerHandlerSocket(oos,ois,nickname,game,controller);
            Thread t = new Thread (client);
            t.start();
            //notifyAll();
        }while(game.getNumPlayer()< game.getExpPlayers());
    }
    private void firstPlayer() throws IOException, ClassNotFoundException {
        if(game.isFirst()) {
            //chiedere num exp player
            oos.writeBoolean(true);
            oos.writeObject("You are the first player, how many others do you want to play with?");
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
