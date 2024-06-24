package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.RoomController;
import it.polimi.ingsw.Model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

/**
 * The class ServerRMIInterface extends Remote class and contains all the methods implemented in the MyServerRMI class
 */

public interface ServerRMIInterface extends Remote {

    /**
     * This method adds a new player to the room.
     *
     * @param nickname is the name of the player to add.
     * @param room is the name of the room.
     * @return such player.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    Player addNewPlayer(String nickname, String room) throws RemoteException;

    /**
     * This method adds a room to the list of rooms.
     *
     * @param roomName is the name of the room to add.
     * @return the updated list of rooms.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    boolean addRoom(String roomName) throws RemoteException;

    /**
     * This method gets the boolean that tells if a player has the available requirements to place a card or not.
     *
     * @param card is the card to place.
     * @param nickname is the name of the player.
     * @param roomName is the name of the room.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    boolean checkCardRequirements(PlayableCard card, String nickname, String roomName) throws RemoteException;

    /**
     * This method draws a card for a player.
     *
     * @param int1 is the type of deck from which the card is drawn. (0 is the resource deck, 1 is the gold one)
     * @param int2 is the position of the card drawn (0 and 1 are the two cards faced up, 2 is the first of the deck
     *             faced down)
     * @param roomName is the name of the room in which to find the game.
     * @param nickname is the name fo player that draws the card.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    void drawCard(int int1, int int2, String roomName, String nickname) throws RemoteException;

    /**
     * This method gets the first card of the game.
     *
     * @param roomName is the name of the room in which to find the game.
     * @return such card.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    StarterCard getFirstCard(String roomName) throws RemoteException;

    /**
     * This method gets the list of players that have won the game.
     *
     * @param roomJoined is the name of the room in which to find the game.
     * @return such list of players.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    ArrayList<Player> getMultiWinners(String roomJoined) throws RemoteException;

    /**
     * This method gets the common objective cards from the game.
     *
     * @param roomName is the name of the room in which to find the game.
     * @return such array of ObjectiveCards.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    ObjectiveCard[] getObjCards(String roomName) throws RemoteException;

    /**
     * This method gets the list of remaining colors to choose in a game.
     *
     * @param roomName is the name of the room in which to find the game.
     * @return such list of colors.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    Set<String> getRemainingColors(String roomName) throws RemoteException;

    /**
     * This method gets the room controller associated to the rooms.
     *
     * @return such room controller.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    RoomController getRoomController() throws RemoteException;

    /**
     * This method tells if a player is the current player.
     *
     * @param nickname is the name of the player.
     * @param roomName is the name of the room in which to find the game.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    boolean isCurrentPlayer(String nickname, String roomName) throws RemoteException;

    void deregisterClient(ClientRMIInterface client) throws RemoteException;

    boolean isValidColor(String color, String roomName) throws RemoteException;

    boolean isAlreadyInRoom(String roomName, String nickname) throws RemoteException;

    /**
     * This method gets the boolean that tells if a deck is empty or not.
     *
     * @param roomName is the name of the room.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    boolean isDeckEmpty(String roomName) throws RemoteException;

    /**
     * This method tells if a player is the first one or not.
     *
     * @param room is the name of the room in which to find the game.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    boolean isFirstPlayer(String room) throws RemoteException;

    /**
     * This method tells if the game is over or not.
     *
     * @param roomName is the name of the room in which to find the game.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    boolean isGameOver(String roomName) throws RemoteException;

    /**
     * This method gets the boolean that tells if it is the last turn or not.
     *
     * @param roomName is the name of the room.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    boolean isLastTurn(String roomName) throws RemoteException;

    /**
     * This method advances to the next round of the round controller.
     *
     * @param roomName is the name of the room in which to find the game.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    void nextRound(String roomName) throws RemoteException;

    /**
     * This method place a card in a specific position from the hand of a player.
     *
     * @param card is the card to play.
     * @param position is the position in which the card is played.
     * @param roomName is the name of the room in which to find the game.
     * @param nickname is the name of the player.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     * @throws MissingResourcesException if there are not the available resources to playe that card.
     * @throws InvalidPositionException if the position of the card does not belong to available positions set.
     */
    void placeCard(PlayableCard card, Position position, String roomName, String nickname) throws RemoteException, MissingResourcesException, InvalidPositionException;


    /**
     * This method set the starter card to a player in a game.
     *
     * @param st is the starter card to set.
     * @param nickname is the nickname of the player to whom the starter card is to be set.
     * @param roomJoined is the name of the room in which to find the game.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     * @throws InvalidPositionException if the position of the card does not belong to available positions set.
     */
    void setFirstCard(StarterCard st, String nickname, String roomJoined) throws RemoteException, InvalidPositionException;

    /**
     * This method sets the secret objective card to a player in a game.
     *
     * @param objSecret is the secret objective card to set.
     * @param nickname is the nickname of the player to whom the secret objective card is to be set.
     * @param roomName is the name of the room in which to find the game.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    void setObjSecret(ObjectiveCard objSecret, String nickname, String roomName) throws RemoteException;

    /**
     * This method sets the color to a player.
     *
     * @param color is the color to set.
     * @param nickname is the name of the player to whom the color is to be set.
     * @param roomName is the name of the room in which to find the game.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    boolean setPlayerColor(String color, String nickname, String roomName) throws RemoteException;

    /**
     * This method sets the number of expected players in the room.
     *
     * @param number is the number of expected players.
     * @param room is the name of the room.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    void setPlayerNumber(int number, String room) throws RemoteException;

    /**
     * This method shows the rooms available.
     *
     * @return such list of rooms.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    ArrayList<Room> showRooms() throws RemoteException;

    void registerClient(ClientRMIInterface client) throws RemoteException;

    void checkClients() throws RemoteException;

    boolean isTerminating(String roomName) throws RemoteException;

}
