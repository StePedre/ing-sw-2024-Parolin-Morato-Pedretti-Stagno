package it.polimi.ingsw.CS;

import it.polimi.ingsw.Model.InvalidPositionException;
import it.polimi.ingsw.Model.MissingResourcesException;
import it.polimi.ingsw.Model.Player;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRMIInterface extends Remote {
    void runClient() throws IOException, InvalidPositionException, MissingResourcesException;

    void writeMessage(String message) throws RemoteException;

}
