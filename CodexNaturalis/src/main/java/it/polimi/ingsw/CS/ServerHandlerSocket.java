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
    private InitGameController controller = null;
    public ServerHandlerSocket(ObjectOutputStream oos,ObjectInputStream ois,String nickname,Game game,InitGameController controller) throws IOException {
        out = oos;
        in = ois;
        this.game=game;
        this.player = new Player(nickname);
        game.addPlayer(player);
        this.controller=controller;
    }
    @Override
    public void run() {
        //funzioni sul client
        try {
            out.writeObject(player);
            while (game.getNumPlayer() != game.getExpPlayers()) {
                //game.wait();
            }
            sendData(); //inviare istanza game
            rc = new RoundController(game.getPlayers());//creare round controller
            rc.setFirstPlayer();
            PlayerController pc = new PlayerController(player.getPlayerGround(),player.getHand());// momentaneo
            //select secret obj
            ObjectiveCard[] obj = pc.pickObjCard(game);
            out.writeObject(obj);
            pc.setObjSecret(obj[((int)in.readObject())-1]);
            //started card
            StarterCard st = pc.pickCard(game);
            out.writeObject(st);
            if(in.readBoolean()){
                st.flipCard();
            }
            pc.setFirtCard(st);
            sendData();
            while(true){//fino a fine gioco
                while(!(player.getNickname().equals(rc.getCurrentPlayer().getNickname()))){
                   //wait();
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
                sendData();
            }
        }
        catch (IOException e){
            //gestione ecc
        }
        catch (ClassNotFoundException e) {
            //gestione ecc
        } catch (InvalidPositionException e) {
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
        out.writeObject(player);//invio Playerground e mano aggiornati
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
