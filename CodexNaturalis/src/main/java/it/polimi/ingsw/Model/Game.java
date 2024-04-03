package it.polimi.ingsw.Model;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private Deck[] decks;
    private int numPlayers;
    private Chat chat;
    private Chat[] privChatList;
    private ObjectiveCard[] commonObj;
    private Player firstPlayer;
    private int i;
    private ArrayList<String> multiWinners;
    // constructor
    public Game(ArrayList<Player> players, int numPlayer, Deck[] decks, ObjectiveCard[] commonObj, Player firstPlayer){
        this.players = players;
        this.numPlayers = numPlayer;
        this.decks = decks;
        this.commonObj = commonObj;
        this.firstPlayer = firstPlayer;
    }
    public void start() throws RemoteException {
        settingPhase();
        createGlobalChat();
        createPrivateChats();
    }
    public void settingPhase(){
        for(int i = 0; i<3; i++){
            // decks[i] = from file
            // decks[i].setCards() = from file
        }
        numPlayers = players.size();
        firstPlayer = players.getFirst();
        // create decks (da file), cards (da file), first player, objcard, etc ....
    }
    // il motivo (fine deck, raggiunto 20 punti) lo gestisce il controller
    public void finish(){
        int max = players.getFirst().getPlayerGround().getPlayerScore();
        String winnerName = players.getFirst().getNickname();
        int curr;
        int k = 0;
        for(int j = 1; j<numPlayers; j++){
            curr = players.get(j).getPlayerGround().getPlayerScore();
            if (curr > max) {
                max = curr;
                winnerName = players.get(j).getNickname();
            }
            else if (curr == max) {
                multiWinners.add(players.get(j).getNickname());
            }
        }
        if (!multiWinners.isEmpty()) {
            System.out.println(winnerName + " wins the game!");
        }
        else {
            System.out.println("It's a draw: ");
            while(!multiWinners.isEmpty()){
                System.out.println(multiWinners.getLast() + " ");
                multiWinners.removeLast();
            }
            System.out.println("win the game!");
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public Deck[] getDecks() {
        return decks;
    }
    public int getNumPlayer() {
        return numPlayers;
    }
    public Chat getChat() {
        return chat;
    }
    public Chat[] getPrivChatList() {
        return privChatList;
    }
    public Chat getPrivateChat(Player player1, Player player2) throws RemoteException, NotExistingChatException {
        for(int j = 0; j<privChatList.length; j++){
            if(privChatList[j].getPlayer(0) == player1 && privChatList[j].getPlayer(1) == player2) {
                return privChatList[j];
            }
        }
            throw new NotExistingChatException("Error: chat do not exist");
    }
    public ObjectiveCard[] getCommonObj() {
        return commonObj;
    }
    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void createGlobalChat() throws RemoteException {
        String name = "Global Chat";
        int PlayersNo = this.numPlayers;
        chat = new Chat(name, PlayersNo);
    }
    // quelle singole forse meglio crearle solo quando c'è richiesta di mex
    // qui per ogni player nell'array creo una chat per ogni player successivo nell'array (così non ho doppioni)
    public void createPrivateChats() throws RemoteException{
        for (int i = 0; i < (numPlayers-1); i++) {
            String name1 = players.get(i).getNickname();
            for(int j = i+1; j<numPlayers; j++){
                String name2 = players.get(j).getNickname();
                String name = name1 + " and " + name2 + " Private Chat";
                privChatList[i] = new Chat(name, 2);
            }
        }
    }
}
