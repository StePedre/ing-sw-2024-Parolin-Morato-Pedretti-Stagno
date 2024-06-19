package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.PlaceCardController;
import it.polimi.ingsw.Controller.PlayerController;
import it.polimi.ingsw.Controller.RoomController;
import it.polimi.ingsw.Model.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Set;

/**
 * The class MyServerRMI extends UnicastRemoteObject and implements ServerRMIInterface.
 * All the methods are detailed in the Interface.
 * This class has a RoomController that manages the roooms.
 */

public class MyServerRMI extends UnicastRemoteObject implements ServerRMIInterface {

    RoomController rooms;

    /**
     * Class constructor.
     * It sets up the RMI server with the port number and the RoomController.
     *
     * @param port is the port number of the server.
     * @param room is the room to set.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public MyServerRMI(int port, RoomController room) throws RemoteException {
        this.rooms = room;
        java.rmi.registry.LocateRegistry.createRegistry(port);
        java.rmi.registry.Registry registry = java.rmi.registry.LocateRegistry.getRegistry();
        registry.rebind("ServerRMI", this);
        System.out.println("Server RMI started.");
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param nickname is the name of the player to add.
     * @return such player.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public Player addNewPlayer(String nickname, String roomName) throws RemoteException{
        Player newPlayer = new Player(nickname);
        Game game = rooms.getRoom(roomName).getGame();
        if(game.getPlayer(nickname) == null) {
            rooms.getRoom(roomName).addPlayerInRoom();
            game.addPlayer(newPlayer);
            return newPlayer;
        }
        return null;
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param roomName is the name of the room to add.
     * @return the updated list of rooms.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
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

    /**
     * See ServerRMIInterface for more details.
     *
     * @param int1 is the type of deck from which the card is drawn.
     * @param int2 is the position of the card drawn (0 and 1 are the two cards faced up, 2 is the first of the deck
     *             faced down)
     * @param roomName is the name of the room in which to find the game.
     * @param nickname is the name fo player that draws the card.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public void drawCard(int int1, int int2, String roomName, String nickname) throws RemoteException{
        Game game = rooms.getRoom(roomName).getGame();
        game.getPlayer(nickname).getHand().chooseCard(game.getDecks()[int1].drawCard(int2));
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param roomName is the name of the room in which to find the game.
     * @return such card.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public StarterCard getFirstCard( String roomName) throws RemoteException{
        Room room = rooms.getRoom(roomName);
        return room.getPlayerController().pickCard(room.getGame());
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param roomJoined is the name of the room in which to find the game.
     * @return such list of players.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public ArrayList<Player> getMultiWinners(String roomJoined) throws RemoteException{
        return rooms.getRoom(roomJoined).getGame().getMultiWinners();
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param roomName is the name of the room in which to find the game.
     * @return such array of ObjectiveCards.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public ObjectiveCard[] getObjCards(String roomName) throws RemoteException{
        Room room = rooms.getRoom(roomName);
        return room.getPlayerController().pickObjCard(room.getGame());
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param roomName is the name of the room in which to find the game.
     * @return such list of colors.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public Set<String> getRemainingColors(String roomName) throws RemoteException{
        return rooms.getRoom(roomName).getGame().getColors();
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @return such room controller.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public RoomController getRooms() throws RemoteException{
        return rooms;
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param nickname is the name of the player.
     * @param roomName is the name of the room in which to find the game.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public boolean isCurrentPlayer(String roomName, String nickname) throws RemoteException{
        Room room = rooms.getRoom(roomName);
        return room.getRoundController().getCurrentPlayer().getNickname().equals( nickname);
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param room is the name of the room in which to find the game.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public boolean isFirstPlayer(String room) throws RemoteException{
        return rooms.getRoom(room).getGame().isFirst();
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param roomName is the name of the room in which to find the game.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public boolean isGameOver(String roomName) throws RemoteException{
        return rooms.getRoom(roomName).getGame().isOver();
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param roomName is the name of the room in which to find the game.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public void nextRound(String roomName) throws RemoteException{
        rooms.getRoom(roomName).getRoundController().nextRound();
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param card is the card to play.
     * @param position is the position in which the card is played.
     * @param roomName is the name of the room in which to find the game.
     * @param nickname is the name of the player.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     * @throws MissingResourcesException if there are not the available resources to play that card.
     * @throws InvalidPositionException if the position of the card does not belong to available positions set.
     */
    public void placeCard(PlayableCard card, Position position, String roomName, String nickname) throws RemoteException, MissingResourcesException, InvalidPositionException {
        Player player = rooms.getRoom(roomName).getGame().getPlayer(nickname);
        PlaceCardController.place(card, player, position);
        PlaceCardController.removeFromHand(card,player);
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public void runServer() throws RemoteException{

    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param st is the starter card to set.
     * @param nickname is the nickname of the player to whom the starter card is to be set.
     * @param roomName is the name of the room in which to find the game.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     * @throws InvalidPositionException if the position of the card does not belong to available positions set.
     */
    public void setFirstCard(StarterCard st, String nickname, String roomName) throws RemoteException, InvalidPositionException {
        Room room = rooms.getRoom(roomName);
        PlayerController pc = room.getPlayerController();
        PlayerGround pg = room.getGame().getPlayer(nickname).getPlayerGround();
        pc.setFirstCard(st, pg);
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param objSecret is the secret objective card to set.
     * @param nickname is the nickname of the player to whom the secret objective card is to be set.
     * @param roomName is the name of the room in which to find the game.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public void setObjSecret(ObjectiveCard objSecret, String nickname, String roomName) throws RemoteException{
        Room room = rooms.getRoom(roomName);
        PlayerController pc = room.getPlayerController();
        Player player = room.getGame().getPlayer(nickname);
        pc.setObjSecret(objSecret, player.getHand());
        pc.populateHand(room.getGame(),player);
        room.getRoundController().addPlayer(player);
        room.getRoundController().setFirstPlayer();
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param color is the color to set.
     * @param nickname is the name of the player to whom the color is to be set.
     * @param roomName is the name of the room in which to find the game.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public boolean setPlayerColor(String color, String nickname, String roomName) throws RemoteException{
        Set<String> remainingColors = getRemainingColors(roomName);
        if(remainingColors.contains(color)) {
            rooms.getRoom(roomName).getGame().getPlayer(nickname).setColor(color);
            return false;
        }
        return true;
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param number is the number of expected players.
     * @param room is the name of the room.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public void setPlayerNumber(int number, String room) throws RemoteException{
        rooms.getRoom(room).getGame().setExpPlayers(number);
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @return such list of rooms.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public ArrayList<Room> showRooms() throws RemoteException{
        return rooms.getRooms();
    }

    public boolean isLastTurn(String roomName) throws RemoteException{
        return rooms.getRoom(roomName).getRoundController().isLastTurn();
    }

    public boolean isDeckEmpty(String roomName) throws RemoteException{
        return rooms.getRoom(roomName).getGame().getDecks()[0].getCards().isEmpty() || rooms.getRoom(roomName).getGame().getDecks()[1].getCards().isEmpty();
    }

    public boolean checkCardRequirements(PlayableCard card, String nickname, String roomName) throws RemoteException{
        return rooms.getRoom(roomName).getGame().getPlayer(nickname).getPlayerGround().checkRequirements(card);
    }

}
