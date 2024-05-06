package it.polimi.ingsw.CS;

import it.polimi.ingsw.Model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRMIInterface extends Remote {
    void setPlayer(Player player) throws RemoteException;
    void runClient() throws RemoteException;
}
