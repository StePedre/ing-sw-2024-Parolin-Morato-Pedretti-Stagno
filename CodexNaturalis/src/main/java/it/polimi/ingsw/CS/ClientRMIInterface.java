package it.polimi.ingsw.CS;

import it.polimi.ingsw.Model.InvalidPositionException;
import it.polimi.ingsw.Model.MissingResourcesException;
import it.polimi.ingsw.Model.Player;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRMIInterface extends Remote {
    void setPlayer(Player player) throws RemoteException;
    void runClient() throws IOException, InvalidPositionException, MissingResourcesException;


}
