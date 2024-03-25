package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private Deck[] decks;
    decks = new Deck[3];
    private int numPlayer;
    private Chat chat;
    private Chat[] chatPrivate;
    private ObjCard commonObj[];
    private Player firstPlayer;
    // constructor
    public Game(ArrayList<Player> players, Deck[] decks, int numPlayer, Chat chat, Chat[] chatPrivate, ObjCard[] commonObj, Player firstPlayer) {
        this.players = players;
        this.decks = decks;
        this.numPlayer = numPlayer;
        this.chat = chat;
        this.chatPrivate = chatPrivate;
        this.commonObj = commonObj;
        
    }
    public void start(){

    }
    public void finish(){

    }
    public void setPhase(){

    }
    // creo sempre la chat generale
    public void createGlobalChat(){
        String name = "Global Chat";
        int PlayersNo = this.numPlayer;
        chat = new Chat(name, PlayersNo);
    }
    // quelle singole forse meglio crearle solo quando c'è richiesta di mex
    // qui per ogni player nell'array creo una chat per ogni player successivo nell'array (così non ho doppioni)
    public void createPrivateChats{
        for (int i = 0; i < (numPlayer-1); i++) {
            String name1 = players.get(i).getNickname;
            for(int j = i+1; j<numPlayer; j++){
                String name2 = players.get(j).getNickname;
                String name = name1 + " and " + name2 + " Private Chat";
                chatPrivate[i] = new Chat(name, 2);
            }
        }
    }
}
