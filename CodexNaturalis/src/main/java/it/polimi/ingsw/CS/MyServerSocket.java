package it.polimi.ingsw.CS;
import it.polimi.ingsw.Controller.InitGameController;
import it.polimi.ingsw.Controller.RoundController;
import it.polimi.ingsw.Model.Game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


//pensare a come gestire socket+RMI
public class MyServerSocket {
    private ServerSocket serverSocket = null;
    private Socket connection = null;
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    //condividere exp e num con RMI
    Game game=null;
    public MyServerSocket(int port,Game game) throws IOException {
        serverSocket = new ServerSocket(port);
        this.game=game;//trovare porta su cui lavorare e aggiungere try-catch
    }
    public void runServer() throws IOException, ClassNotFoundException {
         do{ //finchè ho 0 giocatori o ho meno giocatori di quelli attesi, come sincronizzo con RMI??
            connection = serverSocket.accept();
            //reader e writer
            oos= new ObjectOutputStream(connection.getOutputStream());
            ois = new ObjectInputStream(connection.getInputStream());
            //raggiunto numero gioactori
            if(game.getNumPlayer() == game.getExpPlayers()) {
                oos.writeChars("reached max numbers of player\n");
                //lanciare eccezione numero giocatori
                break;
            }
            //chiedere nickname
            oos.writeChars("Insert your nickname :\n");
            String nickname=(String) ois.readObject();
            //verificare primo player
            firstPlayer();
            //craere connessione parallela
            ServerHandlerSocket client = new ServerHandlerSocket(connection,nickname,game);
            Thread t = new Thread (client);
            t.start();
            notifyAll();
        }while(game.getNumPlayer()==0 || game.getNumPlayer()< game.getExpPlayers());
    }
    private void firstPlayer() throws IOException {
        if(game.isFirst()) {
            //chiedere num exp player
            oos.writeChars("You are the first player, how many others do you want to play with?");
            game.setExpPlayers(ois.readInt());
        }
    }
    public void close() throws IOException {
        connection.close();
        serverSocket.close();
    }
}
