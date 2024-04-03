package it.polimi.ingsw.Model;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatInterface extends Remote {
    public String getName() throws RemoteException;
    public void send(String msg) throws RemoteException;
    public void setPlayer(Player player) throws RemoteException,FullChatException; //controllare eccezioni
    public Player getPlayer(int i) throws RemoteException;
    public Player[] getPlayersList() throws RemoteException;
    public int getPlayersNo() throws RemoteException;

    // show previous method
}
