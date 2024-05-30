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
            Thread t = new Thread(() ->{
                while(!game.isOver()){
                    if(game.getNumPlayer()!=game.getExpPlayers()){
                        try {
                            socket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }
            });
            start();
            colorChoose();
            out.writeObject(player);
            out.writeObject(false);
            while(game.getNumPlayer() != game.getExpPlayers()) {  // in gui schermata waiting
            }
            out.writeObject(true);
            playerinit();
            sendData();
            if(!(player.getNickname().equals(rc.getCurrentPlayer().getNickname()))){
                out.writeObject(false);  // non è il suo turno
            }
            while(true){// start game flow
                while(!(player.getNickname().equals(rc.getCurrentPlayer().getNickname()))){  // finchè non è il suo turno
                }
                out.writeObject(true);//è il tuo turno
                sendData();
                if(rc.isLastTurn()){
                    out.writeObject(true);
                }
                else{
                    out.writeObject(false);
                }
                playCard();
                if(rc.isLastTurn()){
                    rc.nextRound();
                    out.writeObject(true);
                    break;
                }
                else{
                    out.writeObject(false);
                }
                if(player.getPlayerGround().getPlayerScore()>=20 && !rc.isEnding()){
                    rc.setEnding();
                }
                if(!(rc.isLastTurn() || (game.getDecks()[0].getNumberOfCards()==0 && game.getDecks()[1].getNumberOfCards()==0))) {
                    out.writeObject(true);
                    drawCard();
                }
                else{
                    out.writeObject(false);
                }
                if(game.getDecks()[0].getNumberOfCards()==0 && game.getDecks()[1].getNumberOfCards()==0){//controllo numeri carte deck
                    rc.setEnding();
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
        catch (IOException | ClassNotFoundException | InvalidPositionException e){
            //creare eccezione per chiudere socket sul MyServerSocket
            game.removePlayer(player.getNickname());
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
        } catch (IOException | ClassNotFoundException e) {
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
        out.reset();
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
        } else{
            out.writeObject(false);// da problemi al secondo player nellaa GUIsocket
        }
    }
    public Room roomChoice() throws IOException, ClassNotFoundException {
        out.reset();
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
                if(room.getGame().getNumPlayer() == room.getGame().getExpPlayers()){
                    b=true;
                    out.writeObject(true);
                }else{
                    b = false;
                    out.writeObject(false);
                }

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
        rc.setPlayers(game.getPlayers());
        rc.setFirstPlayer();
        PlayerController pc = new PlayerController(player.getPlayerGround(),player.getHand());
        pc.populateHand(game,player);
        StarterCard st = pc.pickCard(game);
        out.writeObject(st);
        if((boolean)in.readObject()){
            st.flipCard();
        }
        pc.setFirstCard(st);
        ObjectiveCard[] obj = pc.pickObjCard(game);
        out.writeObject(obj);
        pc.setObjSecret(obj[((int)in.readObject())-1]);
    }
    public void colorChoose() throws IOException, ClassNotFoundException {

        boolean colorOK = false;
        String color;
        while(!colorOK){
            out.writeObject(game);
            color = (String) in.readObject();
            if(game.getColors().contains(color)){
                colorOK = game.markColor(color);
                out.writeObject(colorOK);
            }
            else{
                out.writeObject(false);
            }
            if(colorOK){
                player.setColor(color);
            }
        }
    }

}
