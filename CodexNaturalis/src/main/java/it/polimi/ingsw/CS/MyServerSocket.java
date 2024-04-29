package it.polimi.ingsw.CS;
import it.polimi.ingsw.Controller.InitGameController;
import it.polimi.ingsw.Controller.RoundController;

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
    private int expPlayer=0;
    private int numPlayer=0;
    public MyServerSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port); //trovare porta su cui lavorare e aggiungere try-catch
    }
    public void runServer() throws IOException, ClassNotFoundException {
        while(numPlayer==0 || numPlayer<expPlayer) { //finchè ho 0 giocatori o ho meno giocatori di quelli attesi, come sincronizzo con RMI??
            connection = serverSocket.accept();
            //reader e writer
            oos= new ObjectOutputStream(connection.getOutputStream());
            ois = new ObjectInputStream(connection.getInputStream());
            //raggiunto numero gioactori
            if(numPlayer==expPlayer) {
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
            newConnection client = new newConnection(connection,nickname);
            Thread t = new Thread (client);
            t.start();
            numPlayer++;
            notifyAll();
        }
    }
    private void firstPlayer() throws IOException {
        if(numPlayer == 0) {
            //chiedere num exp player
            oos.writeChars("You are the first player, how many others do you want to play with?");
            expPlayer = ois.readInt();
        }
    }
    public void close() throws IOException {
        connection.close();
        serverSocket.close();
    }


    private class newConnection implements Runnable{
        private Socket client= null;
        private String nickname = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        public newConnection(Socket connection,String nickname) throws IOException {
            client = connection;
            this.nickname = nickname;
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        }
        @Override
        public void run() {
            //funzioni sul client
            try {
                while (numPlayer != expPlayer) {
                    wait();
                }
            }//togliere quando tolto commento
                //recuperare game
                /*InitGameController c = new InitGameController();
                out.writeObject(c.getGame()); //inviare istanza game
                RoundController rc = new RoundController(c.getGame().getPlayers(),expPlayer);creare round controller
                rc.setFirstPlayer()
                String currTurn = rc.getCurrentPlayer().getNickname();
                while(end){
                    while(currTurn!=this.nickname){
                        currTurn=rc.getCurrentPlayer();
                    }
                    out.writeObject(c.getGame());
                    //azioni di gioco
                    rc.nextRound()//fine turno
                }



            }
            catch (IOException e){
                //gestione ecc
            }*/
            catch(InterruptedException e){
                //gestione ecc
            }


        }
    }
}
