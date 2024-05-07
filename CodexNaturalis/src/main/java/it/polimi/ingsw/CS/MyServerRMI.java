package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.RoomController;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MyServerRMI extends UnicastRemoteObject implements ServerRMIInterface {


    RoomController rooms;

    public MyServerRMI(int port, RoomController room) throws RemoteException {
        this.rooms = room;
        java.rmi.registry.LocateRegistry.createRegistry(port);
        java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.getRegistry();
        registry.rebind("ServerRMI", this);
        System.out.println("Server RMI started.");

    }

    public void runServer() throws RemoteException{

    }

    public RoomController getRooms() throws RemoteException{
        return rooms;
    }

    public Player addNewPlayer(String nickname, String room) throws RemoteException{
        Player newPlayer = new Player(nickname);
        rooms.getRoom(room).getGame().addPlayer(newPlayer);
        return newPlayer;
    }

    public ArrayList<Room> showRooms() throws RemoteException{
        return rooms.getRooms();
    }

    public void setPlayerNumber(int number, String room) throws RemoteException{
        rooms.getRoom(room).getGame().setExpPlayers(number);
    }


    public boolean isFirstPlayer(String room) throws RemoteException{
        return rooms.getRoom(room).getGame().isFirst();
    }

    public void addRoom(String roomName) throws RemoteException{
        Room room = new Room(roomName);
        rooms.addRoom(room);
    }


}
