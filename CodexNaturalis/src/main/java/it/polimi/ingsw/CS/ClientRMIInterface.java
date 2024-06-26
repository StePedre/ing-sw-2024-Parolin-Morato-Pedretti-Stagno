package it.polimi.ingsw.CS;

import it.polimi.ingsw.Model.InvalidPositionException;
import it.polimi.ingsw.Model.MissingResourcesException;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.TUI;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The interface ServerRMIInterface extends remote, and it is implemented by class MyServerRMI.
 */

public interface ClientRMIInterface extends Remote {

    /**
     * This method gets the name of the room joined by a player.
     *
     * @return such name.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    String getRoomJoined() throws RemoteException;

    /**
     * This method is used to catch an exception.
     *
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    void ping() throws RemoteException;

    /**
     * This method starts the client and calls the method that start the TUI.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws InvalidPositionException if the position of the card does not belong to available positions set.
     * @throws MissingResourcesException if there are not the available resources to play that card.
     */
    void runClient(TUI tui) throws IOException, InvalidPositionException, MissingResourcesException;

    /**
     * This method terminates the client.
     * It is called at the end of the game, or in case any of the players disconnects.
     *
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    void terminateClient() throws RemoteException;

    String getNickname() throws RemoteException;

}
