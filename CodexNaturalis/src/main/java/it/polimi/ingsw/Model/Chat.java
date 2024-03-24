package it.polimi.ingsw.Model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
public class Chat extends UnicastRemoteObject implements ChatInterface{
    private String Name;                   //cosa va private?
    private int PlayersNo;
    public ChatInterface[] playersList;
    private int i = 0;

    public Chat(String Name, int PlayersNo) throws RemoteException{
        this.Name = Name;
        this.PlayersNo = PlayersNo;
        this.playersList = new ChatInterface[PlayersNo];
    }
    public String getName() throws RemoteException {
        return this.Name;
    }
    public void setPlayer(ChatInterface c) throws RemoteException, Exception {
        if (i < PlayersNo) {
            playersList[i] = c;
            i++;
        } else throw new Exception("Chat is full");
    }
    public ChatInterface getPlayer(int i) throws RemoteException{
        return playersList[i];
    }
    public ChatInterface[] getPlayersList() throws RemoteException{
        return playersList;
    }
    public void send(String msg) throws RemoteException{
        System.out.println(msg);
    }
    public void showPrevious() throws RemoteException{
        // mostra i messaggi -> hashset?
    }
}
