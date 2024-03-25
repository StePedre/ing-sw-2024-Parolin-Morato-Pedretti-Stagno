package it.polimi.ingsw.Model;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatInterface extends Remote {
    public String getName() throws RemoteException;
    public void send(String msg) throws RemoteException;
    public void setPlayer(ChatInterface c)throws RemoteException, Exception;
    public ChatInterface getPlayer(int i) throws RemoteException;
    public ChatInterface[] getPlayersList() throws RemoteException;

    // show previous method
}
