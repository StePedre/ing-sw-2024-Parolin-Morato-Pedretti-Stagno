package it.polimi.ingsw.Model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
public class Chat extends UnicastRemoteObject implements ChatInterface{
    private String Name;
    private int PlayersNo;
    private Player[] playersList;
    private int i = 0;

    public Chat(String Name, int PlayersNo) throws RemoteException{
        this.Name = Name;
        this.PlayersNo = PlayersNo;
        this.playersList = new Player[PlayersNo];
    }
    public String getName() throws RemoteException {
        return this.Name;
    }

    public int getPlayersNo() throws RemoteException {
        return PlayersNo;
    }
    public void setPlayer(Player p) throws RemoteException, FullChatException {
        if (i < PlayersNo) {
            playersList[i] = p;
            i++;
        } else throw new FullChatException("Chat is full");
    }
    public Player getPlayer(int i) throws RemoteException{
        return playersList[i];
    }
    public Player[] getPlayersList() throws RemoteException{
        return playersList;
    }

    public void send(String msg) throws RemoteException{
        System.out.println(msg);
    }

    public void showPrevious() throws RemoteException{
        // mostra i messaggi -> hashset?
    }
}
/**
 *
 */