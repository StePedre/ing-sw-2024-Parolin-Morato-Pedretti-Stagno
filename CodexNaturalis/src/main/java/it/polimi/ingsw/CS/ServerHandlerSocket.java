package it.polimi.ingsw.CS;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandlerSocket implements Runnable {
    private Socket client= null;
    private String nickname = null;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    int expPlayer = -1;
    int numPlayer = -1;
    public ServerHandlerSocket(Socket connection,String nickname,int expPlayer,int numPlayer) throws IOException {
        client = connection;
        this.nickname = nickname;
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
        this.expPlayer = expPlayer;
        this.numPlayer = numPlayer;
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
