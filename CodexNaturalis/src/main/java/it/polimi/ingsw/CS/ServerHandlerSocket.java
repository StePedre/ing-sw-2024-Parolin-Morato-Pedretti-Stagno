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
        try {
            start();
            out.writeObject(player);
            out.writeObject(false);
            while (game.getNumPlayer() != game.getExpPlayers()) {  // in gui schermata waiting
            }
            out.writeObject(true);
            playerinit();
            sendData();
            boolean flag;
            if(!(player.getNickname().equals(rc.getCurrentPlayer().getNickname()))){
                out.writeObject(false);  // non è il suo turno
            }
            while(true){// start game flow
                while(!(player.getNickname().equals(rc.getCurrentPlayer().getNickname()))){  // finchè non è il suo turno
                }
                out.writeObject(true);//è il tuo turno
                if(rc.isLastTurn()){
                    out.writeObject(true);
                }
                else{
                    out.writeObject(false);
                }
                /*if(game.isOver()){
                    out.writeObject(true);
                    over();
                    break;
                }
                else{
                    out.writeObject(false);
                }*/
                sendData();
                playCard();
                if(player.getPlayerGround().getPlayerScore()>=20 && !rc.isLastTurn()){
                    //fine gioco
                    out.writeObject(true);
                    //game.finish();
                    //over();
                    rc.setLastTurn();
                    break;
                }
                else{
                    out.writeObject(false);
                    //non vinto
                }
                if(!rc.isLastTurn()) {
                    out.writeObject(true);
                    drawCard();
                }
                else{
                    out.writeObject(false);
                    break;
                }
                if(game.getDecks()[0].getNumberOfCards()==0 && game.getDecks()[1].getNumberOfCards()==0){//controllo numeri carte deck
                    //fine gioco
                    out.writeObject(true);
                    //game.finish();
                    //over();
                    rc.setLastTurn();
                    break;
                }
                else{
                    out.writeObject(false);
                    //non fine gioco
                }
                rc.nextRound();
                sendData();
                out.writeObject(false);//non è più il suo turno
                out.reset();
            }
            if(rc.getLastPlayer().getNickname().equals(player.getNickname())){
                game.finish();
            }
            while(!game.isOver()){
            }
            over();
            socket.close();
        }
        catch (IOException e){
            //creare eccezione per chiudere socket sul MyServerSocket
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            //creare eccezione per chiudere socket sul MyServerSocket
            e.printStackTrace();
        } catch (InvalidPositionException e) {
            //creare eccezione per chiudere socket sul MyServerSocket
            e.printStackTrace();
        }


    }
    public void playCard() throws IOException, ClassNotFoundException {
        try {
            PlayableCard card = (PlayableCard) in.readObject();
            Position pos = (Position) in.readObject();
            PlaceCardController.place(card,player,pos);
            PlaceCardController.removeFromHand(card,player);
            out.writeObject(player);
            out.reset();
        }
        catch (MissingResourcesException e){
            e.printStackTrace();
        }
        catch (InvalidPositionException e){
            e.printStackTrace();
        }
    }
    public void sendData() throws IOException, ClassNotFoundException {
        out.reset();
        out.writeObject(game);
        out.writeObject(player);
    }
    public void drawCard(){// 0: scoperta resource 1: scoperta resource 2: top deck resource 4...
        try {
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
            if(player.getHand().drawCard(deck)){
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
    public void start() throws IOException, ClassNotFoundException {
        Room room = roomChoice();
        String nickname = nicknameChoice(room);
        firstPlayer(room);
        this.player = new Player(nickname);
        game = room.getGame();
        rc = room.getRoundController();
        game.addPlayer(player);
    }
    public void firstPlayer(Room room) throws IOException, ClassNotFoundException {// implementare nella stanza
        if(room.getGame().isFirst()) {
            out.writeObject(true);
            int n =(int) in.readObject();
            room.getGame().setExpPlayers(n);
        }
        else{
            out.writeObject(false);
        }
    }
    public Room roomChoice() throws IOException, ClassNotFoundException {
        out.writeObject(rooms.getRooms());
        Room room;
        boolean b;
        do {
            if ((boolean) in.readObject()) {
                room = new Room((String) in.readObject());
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
        return room;
    }
    public String nicknameChoice(Room room) throws IOException, ClassNotFoundException {
        String nickname;
        Boolean b;
        do {
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
        return nickname;
    }
    public void playerinit() throws IOException, ClassNotFoundException, InvalidPositionException {
        rc.setplayers(game.getPlayers());
        rc.setFirstPlayer();
        PlayerController pc = new PlayerController(player.getPlayerGround(),player.getHand());
        pc.populateHand(game,player);
        ObjectiveCard[] obj = pc.pickObjCard(game);
        out.writeObject(obj);
        pc.setObjSecret(obj[((int)in.readObject())-1]);
        StarterCard st = pc.pickCard(game);
        out.writeObject(st);
        if((boolean)in.readObject()){
            st.flipCard();
        }
        pc.setFirstCard(st);
    }

}
