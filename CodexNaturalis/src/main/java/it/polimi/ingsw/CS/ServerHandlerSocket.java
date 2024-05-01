package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.*;
import it.polimi.ingsw.Model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandlerSocket implements ServerHandlerInterface {
    private Socket client= null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    private Game game = null;
    private Player player;
    private RoundController rc = null;
    public ServerHandlerSocket(Socket connection,String nickname,Game game) throws IOException {
        client = connection;
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
        this.game=game;
        this.player = new Player(nickname);
        game.addPlayer(player);
    }
    @Override
    public void run() {
        //funzioni sul client
        try {
            while (game.getNumPlayer() != game.getExpPlayers()) {
                wait();
            }
            InitGameController c = new InitGameController(game);
            out.writeObject(game); //inviare istanza game
            rc = new RoundController(game.getPlayers());//creare round controller
            rc.setFirstPlayer();
            while(true){//fino a fine gioco
                while(!(player.getNickname().equals(rc.getCurrentPlayer().getNickname()))){
                   wait();
                }
                //invio componenti gioco aggiornate
                sendData();
                //attendo di ricevere carta da giocare
                playCard();
                //controllo se vittoria
                if(player.getPlayerGround().getPlayerScore()>=20){
                    //gestire vittroia, magari eccezione??
                }
                //pescaggio carta
                drawCard();
                rc.nextRound();//fine turno
                notifyAll();
            }
        }
        catch (IOException e){
            //gestione ecc
        }
        catch(InterruptedException e){
            //gestione ecc
        }
        catch (ClassNotFoundException e) {
            //gestione ecc
        }


    }
    public void playCard() throws IOException, ClassNotFoundException {
        try {
            PlayableCard card = (PlayableCard) in.readObject();
            Position pos = (Position) in.readObject();
            player.getPlayerGround().placeCard(card, pos); // modificare con apposito controller
        }
        catch (MissingResourcesException e){
            //gestione ecc
        }
        catch (InvalidPositionException e){
            //gestione ecc
        }
    }
    public void sendData() throws IOException, ClassNotFoundException {
        out.writeObject(game);//invio game aggiornato
        out.writeObject(player.getHand());//invio mano aggiornata, forse non serve
        out.writeObject(player.getPlayerGround());//invio Playerground aggiornato
    }
    public void drawCard(){
        try {
            Deck deck = (Deck) in.readObject();
            int i = in.readInt();
            if(player.getHand().drawCard(deck)){//sostituire con controller
                player.getHand().chooseCard(deck.drawCard(i));
            }
        } catch (IOException e) {
            //gestire ecc
        } catch (ClassNotFoundException e) {
            //gestire ecc
        }
    }
}
