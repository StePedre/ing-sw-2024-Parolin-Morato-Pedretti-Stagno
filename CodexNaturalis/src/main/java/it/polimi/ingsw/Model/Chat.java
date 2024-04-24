package it.polimi.ingsw.Model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

/**
 * Chat class implements a remote chat service between two or more players.
 * Extending class 'UnicastRemoteObject' allows Chat to enable RMI.
 * The Chat class implements ChatInterface, where are described all methods
 * invokable remotely.
 * Each chat has a name, a number of players (between 2 and 4), the list of players
 * and a variable (initialized to 0) that represents the number of players already in.
 * In addition to get methods, class Chat presents methods for sending a message and
 * showing all previous messages.
 */

public class Chat extends UnicastRemoteObject implements ChatInterface{
    private String Name;
    private int PlayersNo;
    private Player[] playersList;
    private int i = 0;

    /**
     * Class constructor.
     *
     * @param Name is the chat name. May be general (between all players)
     *            or private (between two specific players).
     * @param PlayersNo is the number of players involved in the chat.
     * @exception RemoteException arises if the invocation of a remote constructor
     * is not successful, e.g. connection problems.
     */
    public Chat(String Name, int PlayersNo) throws RemoteException{
        this.Name = Name;
        this.PlayersNo = PlayersNo;
        this.playersList = new Player[PlayersNo];
    }

    /**
     * The method gets the chat name.
     *
     * @return a string containing the chat name.
     * @exception RemoteException arises if the invocation of a remote object
     * or method is not successful, e.g. connection problems.
     */
    public String getName() throws RemoteException {
        return this.Name;
    }

    /**
     * The method gets the number of players using the chat.
     *
     * @return players number.
     * @exception RemoteException arises if the invocation of a remote object
     * or method is not successful, e.g. connection problems.
     */
    public int getPlayersNo() throws RemoteException {
        return PlayersNo;
    }

    /**
     * The method gets a specific player (of class Player) from the players list.
     *
     * @param i is the index associated with the player.
     * @return Player object.
     * @exception RemoteException arises if the invocation of a remote object
     * or method is not successful, e.g. connection problems.
     */
    public Player getPlayer(int i) throws RemoteException{
        return playersList[i];
    }

    /**
     * The method gets the full list of players.
     *
     * @return array of Players.
     * @exception RemoteException arises if the invocation of a remote object
     * or method is not successful, e.g. connection problems.
     */
    public Player[] getPlayersList() throws RemoteException{
        return playersList;
    }

    /**
     * The method adds a player to the chat, if there is enough space. This can only
     * happen if the current number of players (i) is less than maximum number of players.
     * When a new player is added, the variable i is incremented.
     *
     * @param p is the Player that needs to be added to che chat.
     * @exception RemoteException arises if the invocation of a remote object
     * or method is not successful, e.g. connection problems.
     * @exception FullChatException is thrown when the number of chat participants (i)
     * has reached its maximum (PlayersNo).
     */
    public void setPlayer(Player p) throws RemoteException, FullChatException {
        if (i < PlayersNo) {
            playersList[i] = p;
            i++;
        } else throw new FullChatException("Chat is full");
    }

    /**
     * The method prints a message in the chat: there's no need of specifying the
     * addressee because of how the chat is build.
     *
     * @param msg is the string to be sent.
     * @exception RemoteException arises if the invocation of a remote object
     * or method is not successful, e.g. connection problems.
     */
    public void send(String msg) throws RemoteException{
        System.out.println(msg);
    }

    /**
     */
    public void showPrevious() throws RemoteException{
        // mostra i messaggi -> hashset?
    }
}
