package it.polimi.ingsw.CS;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.View.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

/**
 * The class MyClientSocket manages all the action of a player in a Socket connection.
 * It has a Socket object for the connection, an ObjectOutputStream and an ObjectInputStream to send and receive to and
 * from server. It also has an instance of the Player, Game and TUI and a boolean inter that tells which interface the
 * user has choosen.
 */

public class MyClientSocket {
    private  Socket socket;
    private  ObjectInputStream in;
    private  ObjectOutputStream out;
    Player player = null;
    Game game = null;
    TUI tui =  null;
    boolean inter;

    /**
     * Class constructor.
     * It sets the interface boolean.
     *
     * @param inter is the boolean to set.
     */
    public MyClientSocket(boolean inter) {
        this.inter = inter;
    }

    /**
     * This method closes the socket connection.
     *
     * @throws IOException if there has been problems regarding input or output.
     */
    public void close() throws IOException {
        socket.close();
    }

    /**
     * This method calls the starting methods of the interface chosen. If the TUI was selected, it asks to insert the IP
     * address of the server in order to establish the connection.
     *
     * @throws IOException if there has been problems regarding input or output.
     */
    public void runClient() throws IOException {// per test println
        try {
            System.out.println("Client connected");
            if(inter) {
                useTUI(59090);
            }else{
                useGUI();
            }
        }catch (IOException e) {
            close();
            System.out.println("Client disconnection, close the app and restart");
            //throw e;
        } catch (ClassNotFoundException | InterruptedException e) {
            close();
            e.printStackTrace();
        }
    }

    /**
     * This method updates instances of Game and Player after receiving the updated objects from the server.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding casting an object.
     */
    public void updateData() throws IOException, ClassNotFoundException {
        game = (Game) in.readObject();
        player = (Player) in.readObject();
    }

    /**
     * This method starts the GUI.
     */
    public void useGUI(){
        GUIsocket.startGUI();
    }

    /**
     * This method manages the game flow when using the TUI.
     * It sets up the connection, creates or joins a room, gets the player's nickname, asks the number of players expected
     * in the room if the player that has joined was the first one, chooses the color, sets the starter card and the
     * secret objective card, shows the player ground and manages the turn of a player by placing the selected card and
     * drawing another one from the decks selected, till there is the final turn.
     * After that, it prints the players that have won the game.
     * Every Object (boolean, Game, Player, Cards) are sent and received to and from the server using the ObjectOutputStream
     * and ObjectInputStream.
     *
     * @param port is the port number of the server.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     * @throws InterruptedException if there has been problems during the activity of a thread.
     */
    public void useTUI(int port) throws IOException, ClassNotFoundException, InterruptedException {
        tui= new TUI();
        String host = tui.inputIP();
        socket = new Socket(InetAddress.getByName(host), port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        ArrayList<Room> room = (ArrayList<Room>) in.readObject();
        tui.showRoom(room);
        do {
            if (tui.chooseRoom()) { //create
                out.writeObject(true);
                out.writeObject(tui.getRoomName(true, room));
            } else { //join
                out.writeObject(false);
                out.writeObject(tui.getRoomName(false, room));
            }
        }while((boolean)in.readObject());
        do {
            out.writeObject(tui.insertNickname(true));
        }while((boolean) in.readObject());
        boolean NoOk = false;
        if((boolean)in.readObject()){
            do {
                int i = tui.askPlayersNo();
                if(i>=2 && i<=4) {
                    out.writeObject(i);
                    NoOk = true;
                }
            }while(!NoOk);
        }
        do{
            out.writeObject(tui.chooseColor((Set<String>) in.readObject()));
        }while(!(boolean)in.readObject());
        player = (Player) in.readObject();
        in.readObject();//legge false per non numero di player
        tui.Welcome(player);
        in.readObject();//legge true per raggiungimento numero player
        out.writeObject(!tui.showStarterCard((StarterCard) in.readObject()));
        ObjectiveCard[] objs =(ObjectiveCard[]) in.readObject();
        out.writeObject(tui.chooseObjective(objs[0],objs[1]));
        updateData();
        tui.Welcome(player);
        out.reset();
        boolean b =  (boolean) in.readObject();
        while(true){
            if(b){
                updateData();
                tui.yourTurnPlay(game,player);
                if((boolean) in.readObject()){
                    tui.lastTurn();
                }
                PlayableCard card =null;
                while(card == null){
                    card = tui.inputCardToPlace(game,player);
                }
                out.writeObject(card);
                out.writeObject(tui.inputNumberPosition(player));
                out.reset();
                player = (Player) in.readObject();
                if((boolean)in.readObject()){
                    break;
                }
                if((boolean)in.readObject()) {
                    out.writeObject((tui.yourTurnDraw(game, player)));
                }
                updateData();
                b=(boolean) in.readObject();
            }
            else{
                tui.notYourTurn(game,player);
                b=(boolean)in.readObject();
            }
        }
        tui.winnersPrint((ArrayList<Player>) in.readObject());
        close();
    }

}
