package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.RoomController;
import it.polimi.ingsw.Model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerRMIInterface extends Remote {

    void runServer() throws RemoteException;

    StarterCard getFirstCard(String roomName) throws RemoteException;

    ObjectiveCard[] getObjCards(String roomName) throws RemoteException;

    void setFirstCard(StarterCard st, String nickname, String roomJoined) throws RemoteException, InvalidPositionException;

    void setObjSecret(ObjectiveCard objSecret, String nickname, String roomName) throws RemoteException;

    boolean isGameOver(String roomName) throws RemoteException;

    boolean isCurrentPlayer(String nickname, String roomName) throws RemoteException;
    void placeCard(PlayableCard card, Position position, String roomName, String nickname) throws RemoteException, MissingResourcesException, InvalidPositionException;
    Player addNewPlayer(String nickname, String room) throws RemoteException;

    ArrayList<Player> getMultiWinners(String roomJoined) throws RemoteException;

    void drawCard(int int1, int int2, String roomName, String nickname) throws RemoteException;

    void nextRound(String roomName) throws RemoteException;

    void setPlayerNumber(int number, String room) throws RemoteException;

    ArrayList<Room> showRooms() throws RemoteException;
    boolean isFirstPlayer(String room) throws RemoteException;

    boolean addRoom(String roomName) throws RemoteException;

    RoomController getRooms() throws RemoteException;



}
