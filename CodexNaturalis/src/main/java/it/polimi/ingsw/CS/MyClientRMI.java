package it.polimi.ingsw.CS;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.View.TUI;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The class MyClientRMI manages all the action of a player in an RMI connection.
 * It has a TUI, its nickname, the name of the room joined and an instance of the ServerRMIInterface.
 * It also has a boolean that tells if the game is waiting for players.
 */

public class MyClientRMI extends UnicastRemoteObject implements ClientRMIInterface, Serializable {

    TUI tui =  null;
    String nickname;
    String roomJoined;
    ServerRMIInterface server;
    boolean waitingForPlayers = false;

    /**
     * Class constructor.
     * It sets up the connection with the server.
     *
     * @param address is the IP address of the server.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    public MyClientRMI(String address) throws RemoteException {
        super();
        try {
            server = (ServerRMIInterface) Naming.lookup(address);
            System.out.println("Connected to RMI server.");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }


    /**
     * This method checks if a player's nickname is already in the game or not.
     * If it is not, it recalls this method with a false boolean passed as a parameter. This allows to keep asking for
     * a correct nickname to be written.
     *
     * @param choice is the boolean the tells if the nickname is valid or not.
     * @return the nickname.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    private String controlNickname(boolean choice) throws RemoteException {
        String nickname = tui.insertNickname(choice);
        if(server.getRooms().alreadyInGame(roomJoined,nickname)){
            return controlNickname(false);
        }else{
            return nickname;
        }
    }

    /**
     * This method checks if the name of a room is already in the list of available rooms.
     * If it is, it recalls the method. This allows to keep asking for a valid name.
     * Otherwise, it returns the name of the room.
     *
     * @param choice is the boolean that tells if the player wants to create or join a room.
     * @param rooms the list of rooms already created.
     * @return the name of the room.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    private String controlRoom(boolean choice, ArrayList<Room> rooms) throws RemoteException{
        String roomName = tui.getRoomName(choice, rooms);
        if(server.getRooms().alredyExist(roomName)){
            return controlRoom(choice, rooms);
        } else {
            return roomName;
        }
    }

    /**
     * This method checks if the name of a room is already in the list of available rooms.
     * If it is not, it recalls the method. This allows to keep asking for a valid name.
     * Otherwise, it returns the name of the room.
     *
     * @param rooms the list of rooms already created.
     * @return the name of the room.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    private String controlRoom2(ArrayList<Room> rooms) throws RemoteException{
        String roomName = tui.getRoomName(false, rooms);
        if(server.getRooms().alredyExist(roomName) || server.getRooms().getRoom(roomName).isFull()){
            return roomName;
        } else {
            return controlRoom(false, rooms);

        }
    }

    /**
     * This method draws a card from a deck.
     *
     * @param position is the position of the card to draw.
     *                 1 and 2 are the two resource cards faced up, 3 is the first card of the resource deck faced down.
     *                 4 and 5 are the two gold cards faced up, 6 is the first card of the gold deck faced down.
     */
    private void drawCardFromDeck(int position){
        try {
            switch (position) {
                case 1 -> {
                    server.drawCard(0,0,roomJoined, nickname);
                }
                case 2 -> {
                    server.drawCard(0,1,roomJoined, nickname);
                }
                case 3 -> {
                    server.drawCard(0,2,roomJoined, nickname);
                }
                case 4 -> {
                    server.drawCard(1,0,roomJoined, nickname);
                }
                case 5 -> {
                    server.drawCard(1,1,roomJoined, nickname);
                }
                case 6 -> {
                    server.drawCard(1,2,roomJoined, nickname);
                }
                default -> throw new Exception();
            };
        } catch (IOException | ClassNotFoundException e) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets the list of players in the room.
     *
     * @return such list of players.
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     */
    private ArrayList<Player> getPlayers() throws RemoteException {
        return server.getRooms().getRoom(roomJoined).getGame().getPlayers();
    }

    /**
     * This method manages the players that join a room.
     * For each player that joins, it checks if it is a new player.
     * Once all have joined, the method ends.
     */
    private void listenToPlayers() {
        try {
            ArrayList<Player> oldPlayers = getPlayers();
            while (waitingForPlayers) {

                ArrayList<Player> currentPlayers = getPlayers();

                for (Player currentPlayer : currentPlayers) {
                    boolean isNewPlayer = true;
                    for (Player oldPlayer : oldPlayers) {
                        if (currentPlayer.getNickname().equals(oldPlayer.getNickname())) {
                            isNewPlayer = false;
                            break;
                        }
                    }
                    if (isNewPlayer) {
                        tui.playerJoined(currentPlayer);
                    }
                }

                oldPlayers = new ArrayList<>(currentPlayers);

                if(currentPlayers.size() == server.getRooms().getRoom(roomJoined).getGame().getExpPlayers())
                    waitingForPlayers = false;
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method calls the TUI starting method.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws InvalidPositionException if the position of the card does not belong to available positions set.
     * @throws MissingResourcesException if there are not the available resources to play that card.
     */
    public void runClient() throws IOException, InvalidPositionException, MissingResourcesException {
        System.out.println("Client connected");
        useTUI();
    }

    /**
     * This method sets the starter card of a player (flipped or not depending on what the player has choosen) and the
     * secret objective card.
     * It receives every object from the server, and then it sends them back (updated).
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws InvalidPositionException if the position of the card does not belong to available positions set.
     */
    private void startEarlyGame() throws IOException, InvalidPositionException {
        StarterCard st = server.getFirstCard(roomJoined);
        if(tui.showStarterCard(st)){
            st.flipCard();
        }
        server.setFirstCard(st, nickname, roomJoined);
        ObjectiveCard[] obj = server.getObjCards(roomJoined);
        server.setObjSecret(obj[tui.chooseObjective(obj[0],obj[1])-1], nickname, roomJoined);
    }

    /**
     * This method manages the flow of the game.
     * It checks if the player is the current one: if it is then receives the card to play and its position, then draws
     * a card from the decks. Otherwise, it calls the notYourTurn method.
     * After all the turns have been played, it prints the winner players.
     * Each choice or object is received from the server.
     *
     * @throws RemoteException if there has been problems during the execution of a remote method call.
     * @throws MissingResourcesException if there are not the available resources to play that card.
     * @throws InvalidPositionException if the position of the card does not belong to available positions set.
     */
    private void startNormalGame() throws RemoteException, MissingResourcesException, InvalidPositionException {

        while(!server.isGameOver(roomJoined)) {
            while (!server.isCurrentPlayer(roomJoined, nickname)) {
                if (server.isGameOver(roomJoined)) {
                    break;
                }else{
                    Room room = server.getRooms().getRoom(roomJoined);
                    tui.notYourTurn(room.getGame(), room.getGame().getPlayer(nickname));
                    int i = 0;
                    while(!server.isCurrentPlayer(roomJoined, nickname)){
                        i++;
                    }
                }
            }
            while(server.isCurrentPlayer(roomJoined, nickname)) {
                if (server.isGameOver(roomJoined)) {
                    break;
                }else{
                    Game game = server.getRooms().getRoom(roomJoined).getGame();
                    Player player = game.getPlayer(nickname);
                    if(tui.yourTurnPlay(game, player)){
                        boolean status = true;
                        while(status) {
                            PlayableCard card = null;
                            while(card == null) {
                                card = tui.inputCardToPlace(game, player);
                            }
                                server.placeCard(card, tui.inputNumberPosition(player), roomJoined, nickname);
                                game = server.getRooms().getRoom(roomJoined).getGame();
                                player = game.getPlayer(nickname);
                                drawCardFromDeck(tui.yourTurnDraw(game, player));
                                server.nextRound(roomJoined);
                                status = false;
                        }
                    }
                }
            }

        }
        tui.winnersPrint(server.getMultiWinners(roomJoined));
    }

    /**
     * This method manages the pre-game of a player when using the text user interface.
     * It creates or joins a room, adds the player with their nickname, sets their color.
     * Once all the players have joined the room, it calls the methods that manages the game flow.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws InvalidPositionException if the position of the card does not belong to available positions set.
     * @throws MissingResourcesException if there are not the available resources to play that card.
     */
    private void useTUI() throws IOException, InvalidPositionException, MissingResourcesException {
        tui = new TUI();
        tui.showRoom(server.showRooms());
        if(tui.chooseRoom()){
            roomJoined = controlRoom(true, server.getRooms().getRooms());
            server.addRoom(roomJoined);
            server.setPlayerNumber(tui.askPlayersNo(), roomJoined);
        }else{
            roomJoined = controlRoom2(server.getRooms().getRooms());
        }
            nickname = controlNickname(true);
            Player player = server.addNewPlayer(nickname, roomJoined);
            while(server.setPlayerColor(tui.chooseColor(server.getRemainingColors(roomJoined)), nickname, roomJoined)){
                System.out.println("Wrong color");
            };
            tui.Welcome(player);
            server.addPlayerToRoundController(nickname,roomJoined);
            waitingForPlayers = true;
            listenToPlayers();
            System.out.println("All players have joined, lets start the game!");
            startEarlyGame();
            startNormalGame();
    }

}
