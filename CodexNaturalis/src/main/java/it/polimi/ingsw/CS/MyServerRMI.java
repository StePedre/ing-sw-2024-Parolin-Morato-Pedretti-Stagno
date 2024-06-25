package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.PlaceCardController;
import it.polimi.ingsw.Controller.PlayerController;
import it.polimi.ingsw.Controller.RoomController;
import it.polimi.ingsw.Controller.RoundController;
import it.polimi.ingsw.Model.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class MyServerRMI extends UnicastRemoteObject and implements ServerRMIInterface.
 * All the methods are detailed in the Interface.
 * This class has a RoomController that manages the rooms and Map clients that contains all the clients connected to a
 * game.
 */

public class MyServerRMI extends UnicastRemoteObject implements ServerRMIInterface {

    RoomController rooms;
    private Map<ClientRMIInterface, String> clients;


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
        clients = new ConcurrentHashMap<>();
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
            game.addPlayer(newPlayer);
            rooms.getRoom(roomName).getRoundController().addPlayer(newPlayer);
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
     * @param card is the card to place.
     * @param nickname is the name of the player.
     * @param roomName is the name of the room.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public boolean checkCardRequirements(PlayableCard card, String nickname, String roomName) throws RemoteException{
        return rooms.getRoom(roomName).getGame().getPlayer(nickname).getPlayerGround().checkRequirements(card);
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public void checkClients() throws RemoteException {
        System.out.println("Checking clients:");
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    pingClients();
                } catch (InterruptedException | RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param client is the client to remove.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public void deregisterClient(ClientRMIInterface client) throws RemoteException {
        clients.remove(client);
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
        if(isDeckEmpty(roomName)){
            rooms.getRoom(roomName).getRoundController().setEnding();
        }
        rooms.getRoom(roomName).getRoundController().nextRound();

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
    public RoomController getRoomController() throws RemoteException{
        return rooms;
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param roomName is the name of the room in which to do the check.
     * @param nickname is the name of the player to check.
     * @return a boolean that tells if a player is already in a game or not.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public boolean isAlreadyInRoom(String roomName, String nickname) throws RemoteException{
        Game game = rooms.getRoom(roomName).getGame();
        return game.getPlayer(nickname) != null;
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
        return room.getRoundController().getCurrentPlayer().getNickname().equals(nickname);
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param roomName is the name of the room.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public boolean isDeckEmpty(String roomName) throws RemoteException{
        return rooms.getRoom(roomName).getGame().getDecks()[0].getCards().isEmpty() && rooms.getRoom(roomName).getGame().getDecks()[1].getCards().isEmpty();
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param roomName is the name of the room in which to find the game.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public boolean isFirstPlayer(String roomName) throws RemoteException{
        return rooms.getRoom(roomName).getGame().isFirst();
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
     * @param roomName is the name of the room.
     * @return such boolean.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public boolean isLastTurn(String roomName) throws RemoteException{
        return rooms.getRoom(roomName).getRoundController().isLastTurn();
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param roomName the name of the Room in which the game needs to be checked.
     * @return a boolean that tells if the game is ending or not.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public boolean isTerminating(String roomName) throws RemoteException{
        return rooms.getRoom(roomName).getGame().isTerminating();
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param color is the color to check if it's still available.
     * @param roomName is the name of the room in which to do the check.
     * @return a boolean that tells if the color is available or not.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public boolean isValidColor(String color, String roomName) throws RemoteException{
        return rooms.getRoom(roomName).getGame().getColors().contains(color);
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
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    @Override
    public void ping() throws RemoteException{

    }

    /**
     * This method catches every exception from every client in a game.
     * Everytime this happens, the player is added to the disconnected list.
     * If their game was already started (number of players in the room is the same as the expected ones) that game is set
     * to end. Otherwise, the player is only removed from the room.
     *
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    private void pingClients() throws RemoteException {
        List<ClientRMIInterface> disconnectedClients = new ArrayList<>();
        for (Map.Entry<ClientRMIInterface, String> entry : clients.entrySet()) {
            ClientRMIInterface client = entry.getKey();
            try {
                client.ping();
            } catch (RemoteException e) {
                disconnectedClients.add(client);
            }
        }
        for (ClientRMIInterface client : disconnectedClients) {
            Game game = rooms.getRoom(clients.get(client)).getGame();
            if(game.getPlayers().size() == game.getExpPlayers()){
                game.setTermination();
            }else {
                rooms.getRoom(clients.get(client)).removePlayerInRoom();
            }
            clients.remove(client);
        }
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
        RoundController rc = rooms.getRoom(roomName).getRoundController();
        if(player.getPlayerGround().getPlayerScore() >= 20 && !rc.isEnding()){
            rc.setEnding();
        }
    }

    /**
     * See ServerRMIInterface for more details.
     *
     * @param client is the client to add to the map and to the game.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    @Override
    public void registerClient(ClientRMIInterface client) throws RemoteException {
        String roomJoined = client.getRoomJoined();
        rooms.getRoom(roomJoined).addPlayerInRoom();
        clients.put(client, roomJoined);
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
            remainingColors.remove(color);
            rooms.getRoom(roomName).getGame().setColors(remainingColors);
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
    public ArrayList<Room> showRooms() throws RemoteException {
        List<Room> availableRooms = rooms.getRooms().stream()
                .filter(room -> (!room.isFull() && !room.isOccupied()))
                .toList();
        return new ArrayList<>(availableRooms);
    }

}
