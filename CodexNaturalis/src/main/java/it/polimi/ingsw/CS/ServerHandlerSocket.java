package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.*;
import it.polimi.ingsw.Model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ServerHandlerSocket implements Runnable{
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private Game game;
    private final RoomController rooms;
    private Player player;
    private RoundController rc;
    private final Socket socket;
    private PlayerController pc;
    private Room room;
    public ServerHandlerSocket(ObjectOutputStream oos, ObjectInputStream ois, RoomController rooms, Socket socket) {
        out = oos;
        in = ois;
        this.rooms=rooms;
        this.socket = socket;
    }

    /**
     * This method manages the color choice of the player.
     * It sets the chosen color and removes it from the available colors.
     * Every Object (boolean, colors) are sent and received to and from the server using the ObjectOutputStream
     * and ObjectInputStream.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public void colorChoice() throws IOException, ClassNotFoundException {
        boolean colorOK = false;
        String color;
        out.writeObject(game.getColors());
        while(!colorOK){
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
            else{
                out.writeObject(game.getColors());
            }
        }
    }

    /**
     * This method calls the method that draws a card from the specified deck.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public void drawCard() throws IOException, ClassNotFoundException {// 0: scoperta resource 1: scoperta resource 2: top deck resource 4...
        int i =(int) in.readObject();
        PlaceCardController.draw(i,game,player);
    }

    /**
     * This method tells the client if a player is the first in the room or not.
     *
     * @param room is the name of the room.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public void firstPlayer(Room room) throws IOException, ClassNotFoundException {// implementare nella stanza
        if(room.getGame().isFirst()) {
            out.writeObject(true);
            int n =(int) in.readObject();
            room.getGame().setExpPlayers(n);
        } else{
            out.writeObject(false);
        }
    }

    /**
     * This method manages the game flow, server side.
     * It adds the player to the room once the number of players matches the number of expected players, tells each player
     * if it is their turn or not. If it is, checks if it is the last one and called the method that places the card onto
     * the player ground. After that, if any player has reached 20 points the game end is set, otherwise the player draws
     * a card from the two decks.
     * Also, the end of the game is set if both decks have no cards left.
     * After each turn, it increases the round in the RoundController.
     * At the end of the game, the winner (or the winners) are announced.
     * The method also removes a player (and prints a message) from the game if there has been some problems.
     * Every Object (boolean, Game, Player, Cards) are sent and received to and from the server using the ObjectOutputStream
     * and ObjectInputStream.
     */
    public void gameFlow(){
        try {
            Thread t = new Thread(() ->{
                while(true){
                    if(game.isTerminating()){
                        try {
                            socket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            game.addPlayer(player);
            rc.addPlayer(player);
            while(game.getNumPlayer() != game.getExpPlayers()) {  // in gui schermata waiting
            }
            t.start();
            out.writeObject(true);
            playerinit();
            sendData();
            if(!(player.getNickname().equals(rc.getCurrentPlayer().getNickname()))){
                out.writeObject(false);  // non è il suo turno
            }
            while(true){// start game flow
                while(!(player.getNickname().equals(rc.getCurrentPlayer().getNickname()))){// finchè non è il suo turno
                    if(socket.isClosed()){
                        throw new IOException();
                    }
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
                if(game.getDecks()[0].getNumberOfCards()==0 && game.getDecks()[1].getNumberOfCards()==0 && !rc.isEnding()){//controllo numeri carte deck
                    rc.setEnding();
                }
                sendData();
                out.writeObject(false);//non è più il suo turno
                out.reset();
                rc.nextRound();
            }
            t.interrupt();
            if(rc.getLastPlayer().getNickname().equals(player.getNickname())){
                game.finish();
            }
            while(!game.isOver()){
                Thread.sleep(1000);
            }
            out.writeObject(game.getMultiWinners());
            socket.close();
        }
        catch (IOException | ClassNotFoundException | InvalidPositionException e) {
            //game.removePlayer(player.getNickname());
            game.setTermination();
            System.out.println("il player " + player.getNickname() + " si è disconnesso");
            //e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method checks if the nickname is already taken or not.
     * Every Object (boolean, nickname) are sent and received to and from the server using the ObjectOutputStream
     * and ObjectInputStream.
     *
     * @param room is the name of the room in which to do the check.
     * @return the nickname.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public String nicknameChoice(Room room) throws IOException, ClassNotFoundException {
        String nickname;
        Boolean b;
        do {
            nickname = (String) in.readObject();
            if(rooms.alreadyInGame(room.getName(),nickname)){// true trovato nome uguale
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

    /**
     * This method places the card on the player ground.
     * The card played is removed from the hand of the player.
     * Every Object (Position, Player, Card) are sent and received to and from the server using the ObjectOutputStream
     * and ObjectInputStream.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public void playCard() throws IOException, ClassNotFoundException {
        try {
            PlayableCard card = (PlayableCard) in.readObject();
            Position pos = (Position) in.readObject();
            PlaceCardController.place(card,player,pos);
            PlaceCardController.removeFromHand(card,player);
            out.reset();
            out.writeObject(player);
            out.reset();
        }
        catch (MissingResourcesException | InvalidPositionException e){
            e.printStackTrace();
        }
    }

    /**
     * This method initializes the player: populates their hand, the chosen starter card and the chosen secret objective
     * card.
     * Every Object (boolean, Cards) are sent and received to and from the server using the ObjectOutputStream
     * and ObjectInputStream.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     * @throws InvalidPositionException if the position of the card does not belong to available positions set.
     */
    public void playerinit() throws IOException, ClassNotFoundException, InvalidPositionException {
        rc.setFirstPlayer();
        //PlayerController pc = new PlayerController(player.getPlayerGround(),player.getHand());
        pc.populateHand(game,player);
        StarterCard st = pc.pickCard(game);
        out.writeObject(st);
        if((boolean)in.readObject()){
            st.flipCard();
        }
        pc.setFirstCard(st,player.getPlayerGround());
        ObjectiveCard[] obj = pc.pickObjCard(game);
        out.writeObject(obj);
        pc.setObjSecret(obj[((int)in.readObject())-1],player.getHand());
    }

    /**
     * This method creates or joins a room, depending on what the client has choosen.
     * In any case, it adds the player to the room.
     * Every Object (boolean, Room) are sent and received to and from the server using the ObjectOutputStream
     * and ObjectInputStream.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public void roomChoice() throws IOException, ClassNotFoundException { //controllo nome room
        out.reset();
        out.writeObject(rooms.getRooms());
        boolean b;
        do {
            if ((boolean) in.readObject()) {
                room = new Room((String) in.readObject());
                if (rooms.alreadyExist(room.getName())) {
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
        room.addPlayerInRoom();
    }

    @Override
    public void run() {
        try {
            start();
            colorChoice();
            out.writeObject(player);
            out.writeObject(false);
            gameFlow();
        } catch (IOException | ClassNotFoundException e) {
            room.removePlayerInRoom();
        }
    }

    /**
     * This method send updated data (Game and Player) to the client using the ObjectOutputStream.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public void sendData() throws IOException, ClassNotFoundException {
        out.reset();
        out.writeObject(game);
        out.writeObject(player);
    }

    /**
     * This method sets up the start of the game: room choice, entering nickname, checking if it is the first player.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public void start() throws IOException, ClassNotFoundException {
        roomChoice();
        pc = room.getPlayerController();
        String nickname = nicknameChoice(room);
        out.reset();
        firstPlayer(room);
        player = new Player(nickname);
        game = room.getGame();
        rc = room.getRoundController();
    }

}
