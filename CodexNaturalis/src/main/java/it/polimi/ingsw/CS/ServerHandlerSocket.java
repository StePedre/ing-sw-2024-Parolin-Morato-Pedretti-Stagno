package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.*;
import it.polimi.ingsw.Model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ServerHandlerSocket implements ServerHandlerInterface {
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Game game;
    private final Player player;
    private final RoundController rc;
    private final Socket socket;
    public ServerHandlerSocket(ObjectOutputStream oos, ObjectInputStream ois, String nickname, Game game, RoundController rc, Socket socket) {
        out = oos;
        in = ois;
        this.game=game;
        this.player = new Player(nickname);
        game.addPlayer(player);
        this.rc = rc;
        this.socket = socket;
    }
    @Override
    public void run() {
        //funzioni sul client
        try {
            out.writeObject(player);
            while (game.getNumPlayer() != game.getExpPlayers()) {
                doNothing();
            }
            rc.setplayers(game.getPlayers());
            sendData(); //inviare istanza game
            rc.setFirstPlayer();
            PlayerController pc = new PlayerController(player.getPlayerGround(),player.getHand());// momentaneo
            //select secret obj
            ObjectiveCard[] obj = pc.pickObjCard(game);
            out.writeObject(obj);
            pc.setObjSecret(obj[((int)in.readObject())-1]);
            //started card
            StarterCard st = pc.pickCard(game);
            out.writeObject(st);
            if((boolean)in.readObject()){
                st.flipCard();
            }
            pc.setFirtCard(st);
            //popola la mano
            pc.populateHand(game,player);
            sendData();
            if(!(player.getNickname().equals(rc.getCurrentPlayer().getNickname()))){//primo turno
                out.writeObject(false);
            }
            while(!game.isOver()){//fino a fine gioco, gestire primo turno
                while(!(player.getNickname().equals(rc.getCurrentPlayer().getNickname()))){
                    if(game.isOver()){
                        break;
                    }
                }
                out.writeObject(true);//è il tuo turno
                if(game.isOver()){
                    out.writeObject(true);
                    over();
                    break;
                }
                else{
                    out.writeObject(false);
                }
                //invio componenti gioco aggiornate
                sendData();
                //attendo di ricevere carta da giocare
                playCard();
                //controllo se vittoria
                if(player.getPlayerGround().getPlayerScore()>=20){
                    //fine gioco
                    out.writeObject(true);
                    game.finish();
                    over();
                }
                else{
                    out.writeObject(false);
                    //non vinto
                }
                //pescaggio carta
                drawCard();
                if(game.getDecks()[0].getNumberOfCards()==0 && game.getDecks()[1].getNumberOfCards()==0){//controllo numeri carte deck
                    //fine gioco
                    out.writeObject(true);
                    game.finish();
                    over();
                }
                else{
                    out.writeObject(false);
                    //non vinto
                }
                rc.nextRound();//fine turno
                sendData();
                out.writeObject(false);//non è più il suo turno
            }
            socket.close();
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
    public void drawCard(){// 0: scoperta resource 1: scoperta resource 2: top deck resource 4...
        try {
            //Deck deck = (Deck) in.readObject();
            int i =(int) in.readObject();
            Deck deck;
            int card = switch (i) {
                case 0 -> {
                    deck = game.getDecks()[0];
                    yield 0;
                }
                case 1 -> {
                    deck = game.getDecks()[0];
                    yield 1;
                }
                case 2 -> {
                    deck = game.getDecks()[0];
                    yield 2;
                }
                case 3 -> {
                    deck = game.getDecks()[1];
                    yield 0;
                }
                case 4 -> {
                    deck = game.getDecks()[1];
                    yield 1;
                }
                case 5 -> {
                    deck = game.getDecks()[1];
                    yield 2;
                }
                default -> throw new Exception();
            };
            if(player.getHand().drawCard(deck)){//sostituire con controller
                player.getHand().chooseCard(deck.drawCard(card));
            }
        } catch (IOException e) {
            //gestire ecc
        } catch (ClassNotFoundException e) {
            //gestire ecc
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void over() throws IOException {
        out.writeObject(game.getMultiWinners());
    }
    public void doNothing(){

    }
}
