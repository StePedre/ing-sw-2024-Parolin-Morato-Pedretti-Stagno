package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.*;
import it.polimi.ingsw.Model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandlerSocket implements ServerHandlerInterface {
    private Socket client= null;
    private String nickname = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    private Game game = null;
    private RoundController rc = null;
    public ServerHandlerSocket(Socket connection,String nickname,Game game) throws IOException {
        client = connection;
        this.nickname = nickname;
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
    }
    @Override
    public void run() {
        //funzioni sul client
        try {
            while (game.getNumPlayer() != game.getExpPlayers()) {
                wait();
            }
        //recuperare game
            InitGameController c = new InitGameController(game);
            out.writeObject(game); //inviare istanza game
            rc = new RoundController(game.getPlayers());//creare round controller
            rc.setFirstPlayer();
            String currTurn = rc.getCurrentPlayer().getNickname();
           /* while(end){
                while(currTurn!=this.nickname){
                    currTurn=rc.getCurrentPlayer();
                }
                out.writeObject(c.getGame());
                //azioni di gioco
                rc.nextRound()//fine turno
            }*/



        }
        catch (IOException e){
            //gestione ecc
        }
        catch(InterruptedException e){
            //gestione ecc
        }


    }
}
