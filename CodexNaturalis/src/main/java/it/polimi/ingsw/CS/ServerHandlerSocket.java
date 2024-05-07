package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.*;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.View.TUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ServerHandlerSocket implements ServerHandlerInterface {
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private Game game;
    private final RoomController rooms;
    private Player player;
    private RoundController rc;
    private final Socket socket;
    public ServerHandlerSocket(ObjectOutputStream oos, ObjectInputStream ois, RoomController rooms, Socket socket) {
        out = oos;
        in = ois;
        this.rooms=rooms;
        this.socket = socket;
    }
    @Override
    public void run() {
        //funzioni sul client
        try {
            start();
            out.writeObject(player);
            while (game.getNumPlayer() != game.getExpPlayers()) {

            }
            rc.setplayers(game.getPlayers());
            //sendData(); //inviare istanza game
            rc.setFirstPlayer();
            PlayerController pc = new PlayerController(player.getPlayerGround(),player.getHand());// momentaneo
            //popola la mano
            pc.populateHand(game,player);
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
            pc.setFirstCard(st);


            TUI tui = new TUI();
            tui.showHand(player);
            out.writeObject(player);
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
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }


    }
    public void playCard() throws IOException, ClassNotFoundException {
        try {
            PlayableCard card = (PlayableCard) in.readObject();
            Position pos = (Position) in.readObject();
            player.getPlayerGround().placeCard(card, pos); // modificare con apposito controller
        }
        catch (MissingResourcesException e){
            e.printStackTrace();
        }
        catch (InvalidPositionException e){
            e.printStackTrace();
        }
    }
    public void sendData() throws IOException, ClassNotFoundException {
        out.flush();
        out.writeObject(game);//invio game aggiornato
        out.flush();
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
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void over() throws IOException {
        out.writeObject(game.getMultiWinners());
    }
    public void doNothing(){

    }
    public void start() throws IOException, ClassNotFoundException {
        out.writeObject(true);
        //invio stanze
        out.writeObject(rooms.getRooms());
        Room room;
        boolean b;
        do { // controllo non esistano stanze con lo stesso nome
            if ((boolean) in.readObject()) {
                room = new Room((String) in.readObject());
                //controllo nome stanza
                if (rooms.alredyExist(room.getName())) {
                    out.writeObject(true);
                    b= true;
                } else {
                    out.writeObject(false);
                    b= false;
                    rooms.addRoom(room);
                }
            } else {
                room = rooms.getRoom((String) in.readObject());
                b = false;
                out.writeObject(false);
            }
        }while (b);
        //chiedere nickname
        String nickname;
        do { // cicla finchè il nickname non è unico per la stanza
            nickname = (String) in.readObject();
            if(rooms.alredyInGame(room.getGame(),nickname)){// true trovato nome uguale
                out.writeObject(true);
                b=true;
            }
            else{
                out.writeObject(false);
                b=false;
            }
        }while(b);
        //verificare primo player
        firstPlayer(room);
        //istanziare primo player
        this.player = new Player(nickname);
        game = room.getGame();
        rc = room.getRoundController();
        game.addPlayer(player);
    }
    private void firstPlayer(Room room) throws IOException, ClassNotFoundException {// implementare nella stanza
        if(room.getGame().isFirst()) {
            //chiedere num exp player
            out.writeObject(true);
            int n =(int) in.readObject();
            room.getGame().setExpPlayers(n);
        }
        else{
            out.writeObject(false);
        }
    }
}
