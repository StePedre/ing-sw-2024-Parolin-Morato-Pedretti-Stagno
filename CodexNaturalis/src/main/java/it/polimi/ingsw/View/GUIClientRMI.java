package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.ClientRMIInterface;
import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.CS.ServerRMIInterface;
import it.polimi.ingsw.Model.*;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class GUIClientRMI extends UnicastRemoteObject implements Serializable {
    ServerRMIInterface server;
    public GUIClientRMI(String address) throws RemoteException {
        super();
        try {
            server = (ServerRMIInterface) Naming.lookup(address);
            System.out.println("Connected to RMI server.");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<Room> getRooms() throws RemoteException {
        return server.showRooms();
    }

    public boolean addRoom(String roomName) throws RemoteException {
        return server.addRoom(roomName);
    }

    public Room getRoom(String name) throws RemoteException {
        return server.getRooms().getRoom(name);
    }

    public Player addPlayer(String playerNick, String roomName) throws RemoteException {
        return server.addNewPlayer(playerNick, roomName);
    }

    public void draw(int i1, int i2, String roomName, String nickname) throws RemoteException {
        server.drawCard(i1, i2, roomName, nickname);
    }

    public void place(PlayableCard card, Position position, String roomName, String nickname) throws MissingResourcesException, RemoteException, InvalidPositionException {
        server.placeCard(card, position, roomName, nickname);
    }

    public void setFirstCard(StarterCard st, String nickname, String roomName) throws RemoteException, InvalidPositionException {
        server.setFirstCard(st, nickname, roomName);
    }

    public void setObj(ObjectiveCard secret, String nickname, String roomName) throws RemoteException {
        server.setObjSecret(secret, nickname, roomName);
    }

    public ArrayList<Player> getWinners(String roomName) throws RemoteException {
        return server.getMultiWinners(roomName);
    }

    public StarterCard showFirstCard(String roomName) throws RemoteException {
        return server.getFirstCard(roomName);
    }

    public ObjectiveCard[] showSecretObjs(String roomName) throws RemoteException {
        return server.getObjCards(roomName);
    }

    public boolean isCurrent(String nickname, String roomName) throws RemoteException {
        return server.isCurrentPlayer(nickname, roomName);
    }

    public boolean isFirst(String roomName) throws RemoteException {
        return server.isFirstPlayer(roomName);
    }

    public boolean isOver(String roomName) throws RemoteException {
        return server.isGameOver(roomName);
    }

    public void nextRound(String roomName) throws RemoteException {
        server.nextRound(roomName);
    }

    public void setPlayerNo(int n, String roomName) throws RemoteException {
        server.setPlayerNumber(n, roomName);
    }






    /*public void runClient() throws IOException, InvalidPositionException, MissingResourcesException {

    }*/

    /*@Override
    public void writeMessage(String message) throws RemoteException {

    }*/
}
