package it.polimi.ingsw.Model;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private Deck[] decks;
    private int numPlayer;
    private Chat chat;
    private Chat[] chatPrivate;
    private ObjCard commonObj[];
    private Player firstPlayer;
    // constructor
    public Game(ArrayList<Player> players, int numPlayer) {
        this.players = players;
        this.numPlayer = numPlayer;
    }
    public void start(){
        // createGlobalChat();
        // createPrivateChats();
        // settingPhase();
    }
    public void finish(){
        // count points, select winner
    }
    public void settingPhase(){
       // create decks (da file), cards (da file), first player, objcard, etc ....
    }
    // creo sempre la chat generale
    public void createGlobalChat() throws RemoteException {
        String name = "Global Chat";
        int PlayersNo = this.numPlayer;
        chat = new Chat(name, PlayersNo);
    }
    // quelle singole forse meglio crearle solo quando c'è richiesta di mex
    // qui per ogni player nell'array creo una chat per ogni player successivo nell'array (così non ho doppioni)
    public void createPrivateChats() throws RemoteException{
        for (int i = 0; i < (numPlayer-1); i++) {
            String name1 = players.get(i).getNickname();
            for(int j = i+1; j<numPlayer; j++){
                String name2 = players.get(j).getNickname();
                String name = name1 + " and " + name2 + " Private Chat";
                chatPrivate[i] = new Chat(name, 2);
            }
        }
    }
}
