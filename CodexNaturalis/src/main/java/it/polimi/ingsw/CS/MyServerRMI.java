package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.PlaceCardController;
import it.polimi.ingsw.Controller.PlayerController;
import it.polimi.ingsw.Controller.RoomController;
import it.polimi.ingsw.Controller.RoundController;
import it.polimi.ingsw.Model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;

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


    public Player addNewPlayer(String nickname, String roomName) throws RemoteException{
        Player newPlayer = new Player(nickname);
        Game game = rooms.getRoom(roomName).getGame();
        if(game.getPlayer(nickname) == null) {
            game.addPlayer(newPlayer);
            return newPlayer;
        }
        return null;
    }

    public StarterCard getFirstCard( String roomName) throws RemoteException{
        Room room = rooms.getRoom(roomName);
        return room.getPlayerController().pickCard(room.getGame());
    }

    public void setFirstCard(StarterCard st, String nickname, String roomName) throws RemoteException, InvalidPositionException {
        Room room = rooms.getRoom(roomName);
        PlayerController pc = room.getPlayerController();
        PlayerGround pg = room.getGame().getPlayer(nickname).getPlayerGround();
        pc.setFirstCard(st, pg);
    }

    public void setObjSecret(ObjectiveCard objSecret, String nickname, String roomName) throws RemoteException{
        Room room = rooms.getRoom(roomName);
        PlayerController pc = room.getPlayerController();
        Player player = room.getGame().getPlayer(nickname);
        pc.setObjSecret(objSecret, player.getHand());
        pc.populateHand(room.getGame(),player);
        room.getRoundController().addPlayer(player);
        room.getRoundController().setFirstPlayer();
    }

    public boolean isGameOver(String roomName) throws RemoteException{
        return rooms.getRoom(roomName).getGame().isOver();
    }

    public ObjectiveCard[] getObjCards(String roomName) throws RemoteException{
        Room room = rooms.getRoom(roomName);
        return room.getPlayerController().pickObjCard(room.getGame());
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

    public boolean isCurrentPlayer(String roomName, String nickname) throws RemoteException{
        Room room = rooms.getRoom(roomName);
        return room.getRoundController().getCurrentPlayer().getNickname().equals( nickname);
    }

    public void placeCard(PlayableCard card, Position position, String roomName, String nickname) throws RemoteException, MissingResourcesException, InvalidPositionException {
        Player player = rooms.getRoom(roomName).getGame().getPlayer(nickname);
        PlaceCardController.place(card, player, position);
        PlaceCardController.removeFromHand(card,player);
    }
    public ArrayList<Player> getMultiWinners(String roomJoined) throws RemoteException{
        return rooms.getRoom(roomJoined).getGame().getMultiWinners();
    }

    public void nextRound(String roomName) throws RemoteException{
        rooms.getRoom(roomName).getRoundController().nextRound();
    }

    public void drawCard(int int1, int int2, String roomName, String nickname) throws RemoteException{
        Game game = rooms.getRoom(roomName).getGame();
        game.getPlayer(nickname).getHand().chooseCard(game.getDecks()[int1].drawCard(int2));
    }

    public boolean addRoom(String roomName) throws RemoteException{
        Room room = new Room(roomName);
        for(Room r : rooms.getRooms()) {
            if(r.getName().equals(roomName)){
                return false;
            }
        }
        rooms.addRoom(room);
        return true;
    }


}
