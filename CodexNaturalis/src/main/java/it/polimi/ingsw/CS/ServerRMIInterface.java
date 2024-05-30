package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.RoomController;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerRMIInterface extends Remote {

    public void runServer() throws RemoteException;

    public Player addNewPlayer(String nickname, String room) throws RemoteException;

    public void setPlayerNumber(int number, String room) throws RemoteException;

    public ArrayList<Room> showRooms() throws RemoteException;
    public boolean isFirstPlayer(String room) throws RemoteException;

    public boolean addRoom(String roomName) throws RemoteException;

    RoomController getRooms() throws RemoteException;



}
