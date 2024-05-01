package it.polimi.ingsw.Model;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Each instance of Game class is a game session. Objects of class Game must have: a list of players and their number,
 * identity of the first player, two Decks (lists of Cards), one array of ObjectiveCard, one global Chat object and
 * one array of Chat objects (all the private chats).
 * A list of strings is initialized as empty: it will contain the nicknames of multiple winners, if there is more than
 * one winner.
 */

public class Game {
    private ArrayList<Player> players;
    private Deck[] decks;
    private Chat chat;
    private Chat[] privChatList;
    private ObjectiveCard[] commonObj;
    private ArrayList<String> multiWinners = new ArrayList<>();
    private int expPlayers = -1;
    private int numPlayers;

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
        return players;
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
        return numPlayers;
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
        for (Chat value : privChatList) {
            if (value.getPlayer(0) == player1 && value.getPlayer(1) == player2) {
                return value;
            }
        }
        throw new NotExistingChatException("Error: chat do not exist");
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
    public void finish() {
        int max = players.getFirst().getPlayerGround().getPlayerScore();
        String winnerName = players.getFirst().getNickname();
        int curr;
        int flag = 0;
        for (int j = 1; j < numPlayers; j++) {
            curr = players.get(j).getPlayerGround().getPlayerScore();
            if (curr > max) {
                max = curr;
                winnerName = players.get(j).getNickname();
                flag = 1;
            } else if (curr == max && !(players.get(j).getNickname().equals(winnerName))){
                multiWinners.add(winnerName);
                multiWinners.add(players.get(j).getNickname());
                flag = 2;
            }
        }
        if (flag==1) {
            System.out.println(winnerName + " wins the game!");
        } else if (flag==2) {  // pari punti vince chi ha realizzato più carte obiettivo



            // to do


            // se ancora pareggio:
            System.out.println("It's a draw: ");
            for(String name : multiWinners) {
                System.out.println(name + " ");
            }
            System.out.println("win the game!");
        }
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
        for (int i = 0; i < (numPlayers - 1); i++) {
            String name1 = players.get(i).getNickname();
            for (int j = i + 1; j < numPlayers; j++) {
                String name2 = players.get(j).getNickname();
                String name = name1 + " and " + name2 + " Private Chat";
                privChatList[i] = new Chat(name, 2);
            }
        }
    }

    public void addPlayer(Player player) {
        if(numPlayers<4){
            players.add(player);
            numPlayers++;
        }
    }

    public void setDecks(Deck[] decks) {
        this.decks = decks;
    }

    public void setCommonObj(ObjectiveCard[] commonObj) {
        this.commonObj = commonObj;
    }

    public int getExpPlayers() {
        return expPlayers;
    }

    public void setExpPlayers(int n) {
        expPlayers = n;
    }

    public boolean isFirst() {
        synchronized (players) {
            return (numPlayers == 0);
        }

    }
}
