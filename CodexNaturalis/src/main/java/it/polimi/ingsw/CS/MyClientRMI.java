package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.PlaceCardController;
import it.polimi.ingsw.Controller.PlayerController;
import it.polimi.ingsw.Controller.RoundController;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.View.TUI;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class MyClientRMI extends UnicastRemoteObject implements ClientRMIInterface, Serializable {

    private final Scanner scan = new Scanner(System.in);
    Player player;
    Game game = null;
    TUI tui =  null;

    String nickname;

    PlayerController playerController;

    String roomJoined;
    ServerRMIInterface server;
    boolean inter;

    boolean waitingForPlayers = false;


    public MyClientRMI( String address ,boolean inter) throws RemoteException {
        super();
        this.inter = inter;
        try {
            server = (ServerRMIInterface) Naming.lookup(address);
            System.out.println("Connected to RMI server.");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void setPlayer(Player player) throws RemoteException{
        this.player = player;
    }
    public void runClient() throws IOException, InvalidPositionException, MissingResourcesException {
        System.out.println("Client connected");

        if(inter) {//decisione se usare TUI o GUIsocket
            useTUI();
        }
        else{
            useGUI();
        }
    }

    private void useTUI() throws IOException, InvalidPositionException, MissingResourcesException {
        tui = new TUI();
        tui.showRoom(server.showRooms());
        if(tui.chooseRoom()){
            roomJoined = controlRoom(true, true);
            server.addRoom(roomJoined);
            server.setPlayerNumber(tui.askPlayersNo(), roomJoined);
        }else{
            roomJoined = controlRoom2( false, true);
        }
            nickname = controlNickname(true);
            player = server.addNewPlayer(nickname, roomJoined);
            tui.Welcome(player);
            waitingForPlayers = true;
            listenToPlayers();
            System.out.println("All players have joined, lets start the game!");
            startEarlyGame();
            startNormalGame();
    }

    private void listenToEndTurn() {
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
                Thread.sleep(1000);
            }
        } catch (RemoteException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

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
                    Thread.sleep(1000);
                }
            } catch (RemoteException | InterruptedException e) {
                throw new RuntimeException(e);
            }
    }

    private void startEarlyGame() throws IOException, InvalidPositionException {
        playerController = new PlayerController(player.getPlayerGround(),player.getHand());
        //select secret obj
        game = server.getRooms().getRoom(roomJoined).getGame();
        ObjectiveCard[] obj = playerController.pickObjCard(game);
        playerController.setObjSecret(obj[tui.chooseObjective(obj[0],obj[1])-1]);
        StarterCard st = playerController.pickCard(game);
        if(tui.showStarterCard(st)){
            st.flipCard();
        }
        playerController.setFirstCard(st);
        System.out.println(player.getNickname());
        playerController.populateHand(game,player);
    }

    private void startNormalGame() throws RemoteException, MissingResourcesException, InvalidPositionException {

        RoundController roundController = server.getRooms().getRoom(roomJoined).getRoundController();
        roundController.setPlayers(game.getPlayers());
        roundController.setFirstPlayer();
        while(!game.isOver()) {//fino a fine gioco, gestire primo turno
            while (!(player.getNickname().equals(roundController.getCurrentPlayer().getNickname()))) {
                if (game.isOver()) {
                    break;
                }else{
                    tui.notYourTurn(game, player);
                }
            }
            while((player.getNickname().equals(roundController.getCurrentPlayer().getNickname()))) {
                if (game.isOver()) {
                    break;
                }else{
                    if(tui.yourTurnPlay(game, player)){
                        PlayableCard card = tui.inputCardToPlace(player);
                        player.getPlayerGround().placeCard(card, tui.inputCoordinates(player));
                        PlaceCardController.removeFromHand(card,player);
                        drawCardFromDeck(tui.yourTurnDraw(game, player));
                        roundController.nextRound();
                    }
                }
            }

        }
        tui.winnersPrint(game.getMultiWinners());
    }

    private String controlNickname(boolean choice) throws RemoteException {
        String nickname = tui.insertNickname(choice);
        if(server.getRooms().alredyInGame(server.getRooms().getRoom(roomJoined).getGame(),nickname)){
            return controlNickname(false);
        }else{
            return nickname;
        }
    }

    private String controlRoom(boolean choice, boolean choice2) throws RemoteException{
        String roomName = tui.getRoomName(choice, choice2);
        if(server.getRooms().alredyExist(roomName)){
            return controlRoom(choice, false);
        } else {
            return roomName;
        }
    }
    private String controlRoom2(boolean choice, boolean choice2) throws RemoteException{
        String roomName = tui.getRoomName(choice, choice2);
        if(server.getRooms().alredyExist(roomName)){
            return roomName;
        } else {
            return controlRoom(choice, false);

        }
    }

    private void drawCardFromDeck(int position){
        // 0: scoperta resource 1: scoperta resource 2: top deck resource 4...
            try {
                //Deck deck = (Deck) in.readObject();
                Deck deck;
                int card = switch (position) {
                    case 0 -> {
                        deck = game.getDecks()[0];
                        yield 0;
                    }
                    case 1 -> {
                        deck = game.getDecks()[0];
                        yield 1;
                    }
                    case 2 -> {
                        deck = game.getDecks()[0];
                        yield 2;
                    }
                    case 3 -> {
                        deck = game.getDecks()[1];
                        yield 0;
                    }
                    case 4 -> {
                        deck = game.getDecks()[1];
                        yield 1;
                    }
                    case 5 -> {
                        deck = game.getDecks()[1];
                        yield 2;
                    }
                    default -> throw new Exception();
                };
                if(player.getHand().drawCard(deck)){//sostituire con controller
                    player.getHand().chooseCard(deck.drawCard(card));
                }
            } catch (IOException | ClassNotFoundException e) {
                //gestire ecc
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    private ArrayList<Player> getPlayers() throws RemoteException {
        return server.getRooms().getRoom(roomJoined).getGame().getPlayers();
    }
    private void useGUI(){

    }


}
