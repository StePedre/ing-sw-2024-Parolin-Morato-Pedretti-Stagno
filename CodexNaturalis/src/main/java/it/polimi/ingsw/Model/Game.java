package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Each instance of Game class is a game session. Objects of class Game must have: a list of players and their number,
 * identity of the first player, two Decks (lists of Cards), one array of ObjectiveCard, one global Chat object and
 * one array of Chat objects (all the private chats).
 * A list of strings is initialized as empty: it will contain the nicknames of multiple winners, if there is more than
 * one winner.
 */

public class Game implements Serializable {
    private ArrayList<Player> players;
    private Deck[] decks;
    private Chat chat;
    private Chat[] privChatList = new Chat[6];
    private ObjectiveCard[] commonObj;
    private ArrayList<Player> multiWinners = new ArrayList<>();
    private int expPlayers = -1;
    private int numPlayers;
    private ArrayList<ObjectiveCard> otherObjs = null;

    private ArrayList<StarterCard> starterCards;



    /**
     * Class constructor.
     *
     * @param players   is the list of Players attending the game.
     * @param decks     decks used in the game (must be 2).
     * @param commonObj is the set of common objective cards.
     */
    public Game(ArrayList<Player> players, Deck[] decks, ObjectiveCard[] commonObj) {
        this.players = players;
        this.numPlayers = players.size();
        this.decks = decks;
        this.commonObj = commonObj;
    }

    public Game() {
        players = new ArrayList<>();
        numPlayers = 0;
    }

    /**
     * The method gets the list of players attending the game.
     *
     * @return the list of players.
     */
    public ArrayList<Player> getPlayers() {
        synchronized (players) {
            return players;
        }
    }

    /**
     * The method gets the decks used in the game session.
     *
     * @return the decks array.
     */
    public Deck[] getDecks() {
        return decks;
    }

    /**
     * The method gets the number of players attending the game.
     *
     * @return the number of participant in the game.
     */
    public int getNumPlayer() {
        synchronized (players) {
            return numPlayers;
        }
    }

    /**
     * The method gets the global chat, where all the players are participating.
     *
     * @return the global Chat.
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * The method gets the full list of private chats, where each one of them is between two players.
     *
     * @return the private Chats list.
     */
    public Chat[] getPrivChatList() {
        return privChatList;
    }

    /**
     * The method gets a specific private chat from the list of all chat between two players, checking each time
     * the two participants' nickname.
     *
     * @return the requested private Chat.
     * @throws RemoteException          because Chat class extends UnicastRemoteObject.
     * @throws NotExistingChatException because requested chat may not exist.
     */
    public Chat getPrivateChat(Player player1, Player player2) throws RemoteException, NotExistingChatException {
        int i = 0;
        Chat toReturn = null;
        for (Chat value : privChatList) {
            if(value==null){
                break;
            } else if ((value.getPlayer(0) == player1 && value.getPlayer(1) == player2) || (value.getPlayer(1) == player1 && value.getPlayer(0) == player2)) {
                toReturn = value;
                break;
            }
        }
        if(toReturn!=null){
            return toReturn;
        }
        else{
            throw new NotExistingChatException("Error: chat do not exist");
        }
    }

    /**
     * The method gets the array of common objective cards.
     *
     * @return ObjectiveCards array.
     */
    public ObjectiveCard[] getCommonObj() {
        return commonObj;
    }


    /**
     * The method initializes the game by calling other methods to create the global chat and the private ones.
     *
     * @throws RemoteException because Chat class extends UnicastRemoteObject.
     */
    public void start() throws RemoteException {
        createGlobalChat();
        createPrivateChats();
    }

    /**
     * The method is invoked when the game is finished. The final routine consists of checking each player's score:
     * whenever one's score is higher than the previous one, it is set as the maximum. The player with the maximum
     * score is the winner and a message is printed to show it. In case of more than one player with the same maximum
     * score, there is a draw, and they are all considered winners.
     */
    /*public ArrayList<Player> finish() {
        int max = players.getFirst().getPlayerGround().getPlayerScore();
        Player winner = players.getFirst();
        int curr;
        int flag = 1;
        for (int j = 1; j < numPlayers; j++) {
            curr = players.get(j).getPlayerGround().getPlayerScore();
            if (curr > max) {
                max = curr;
                winner = players.get(j);
                if(!(multiWinners.isEmpty())){
                    multiWinners.clear();
                }
                multiWinners.add(winner);
                flag = 1;
            } else if (curr == max && !(players.get(j).getNickname().equals(winner.getNickname()))){
                multiWinners.add(players.get(j));
                flag = 2;
            }
        }
        if (flag==1) {
            return multiWinners;

        } else {  // pari punti vince chi ha realizzato più carte obiettivo
            int maxObjNo = multiWinners.getFirst().getReachedObjNo();
            ArrayList<Player> multiWinners2 = new ArrayList<>();
            for(Player obj: multiWinners){
                if(obj.getReachedObjNo()>maxObjNo){
                    maxObjNo = obj.getReachedObjNo();
                    winner = obj;
                    if(!(multiWinners2.isEmpty())){
                        multiWinners2.clear();
                    }
                    multiWinners2.add(winner);
                } else if (obj.getReachedObjNo() == maxObjNo) {
                    multiWinners2.add(obj);
                }
            }
            return multiWinners2;
        }
    }*/

    public ArrayList<Player>  finish(){
        int[] scores = new int[numPlayers];
        ArrayList<Player> multi2 = new ArrayList<>();
        int i = 0;
        for(Player p : players){
            scores[i] = p.getPlayerGround().getPlayerScore();
            i++;
        }
        i--;
        Arrays.sort(scores);
        for(Player p  : players){
            if(p.getPlayerGround().getPlayerScore()==scores[i]){
                multiWinners.add(p);
            }
        }
        if(multiWinners.size()>1){
            int[] ObjNo = new int[multiWinners.size()];
            i=0;
            for(Player p : multiWinners){
                ObjNo[i] = p.getReachedObjNo();
                i++;
            }
            i--;
            Arrays.sort(ObjNo);
            for(Player p: multiWinners){
                if(p.getReachedObjNo()==ObjNo[i]){
                    multi2.add(p);
                }
            }
        }
        return multi2;
    }

    /**
     * The method creates a new global chat through the Chat constructor: the number of players in the chat is the same
     * as the game one.
     *
     * @throws RemoteException because Chat class extends UnicastRemoteObject.
     */
    public void createGlobalChat() throws RemoteException {
        String name = "Global Chat";
        int PlayersNo = this.numPlayers;
        chat = new Chat(name, PlayersNo);
    }

    /**
     * The method creates an array of new private chats through the Chat constructor: each chat has two participants.
     * For each player, a private chat is created with all the others player, one at a time, skipping the creation
     * when the two players are already paired.
     *
     * @throws RemoteException because Chat class extends UnicastRemoteObject.
     */
    public void createPrivateChats() throws RemoteException {
        int k = 0;
        for (int i = 0; i < (numPlayers - 1); i++) {
            String name1 = players.get(i).getNickname();
            for (int j = i + 1; j < numPlayers; j++) {
                String name2 = players.get(j).getNickname();
                String name = name1 + " and " + name2 + " Private Chat";
                privChatList[k] = new Chat(name, 2);
                privChatList[k].getPlayersList()[0] = players.get(i);
                privChatList[k].getPlayersList()[1] = players.get(j);
                k++;
            }
        }
    }

    /**
     * The method adds a player to the game and increments the total number of players. This can only happen
     * until the total number of players is 4.
     *
     * @param player is the player to add.
     */
    public void addPlayer(Player player) {
        synchronized (players) {
            if (numPlayers < 4) {
                players.add(player);
                numPlayers++;
            }
        }
    }

    /**
     * The method sets the decks of the game.
     *
     * @param decks is the array of decks that will be used in the game.
     */
    public void setDecks(Deck[] decks) {
        this.decks = decks;
    }

    /**
     * The method sets the common objectives shared by all players. There are two of them, gathered in an array of
     * Objective Card.
     *
     * @param commonObj is the array of two common objectives.
     */
    public void setCommonObj(ObjectiveCard[] commonObj) {
        this.commonObj = commonObj;
    }

    /**
     * The method gets the expected number of players.
     *
     * @return the number of expected players.
     */
    public int getExpPlayers() {
        return expPlayers;
    }

    /**
     * The method sets the number of expected players in the game.
     *
     * @param n is the number of expected players.
     */
    public void setExpPlayers(int n) {
        expPlayers = n;
    }

    /**
     * The method checks if the number of player is zero (in other words, if a player is the first to enter the game).
     *
     * @return true if there are no other players, false otherwise.
     */
    public boolean isFirst() {
        synchronized (players) {
            return (numPlayers == 0);
        }
    }
    public StarterCard getOneStarterCard() {
        synchronized (starterCards) {
            Random rand = new Random();
            return starterCards.remove(rand.nextInt());
        }
    }

    public void setStarterCards(ArrayList<StarterCard> starterCards) {
        this.starterCards = starterCards;
    }
    public void setOtherObjs(ArrayList<ObjectiveCard> objs){
        this.otherObjs = objs;
    }
    public ObjectiveCard[] pickPlayerObj(){
        synchronized (otherObjs) {
            Random rand = new Random();
            ObjectiveCard[] objs = {otherObjs.remove(rand.nextInt(otherObjs.size())), otherObjs.remove(rand.nextInt(otherObjs.size()))};
            return objs;
        }
    }
}
