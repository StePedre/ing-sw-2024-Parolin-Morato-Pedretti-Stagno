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

public class MyClientRMI extends UnicastRemoteObject implements ClientRMIInterface, Serializable {

    private final Scanner scan = new Scanner(System.in);
    TUI tui =  null;

    String nickname;

    String roomJoined;
    ServerRMIInterface server;

    boolean waitingForPlayers = false;


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

    public void runClient() throws IOException, InvalidPositionException, MissingResourcesException {
        System.out.println("Client connected");
        useTUI();
    }

    public void writeMessage(String message) throws RemoteException{
        System.out.println(message);
    }

    private void useTUI() throws IOException, InvalidPositionException, MissingResourcesException {
        tui = new TUI();
        tui.showRoom(server.showRooms());
        if(tui.chooseRoom()){
            roomJoined = controlRoom(true, server.getRooms().getRooms());
            server.addRoom(roomJoined);
            server.setPlayerNumber(tui.askPlayersNo(), roomJoined);
        }else{
            roomJoined = controlRoom2( false, server.getRooms().getRooms());
        }
            nickname = controlNickname(true);
            server.setPlayerColor(tui.chooseColor(server.getRemainingColors(roomJoined)), nickname, roomJoined);
            Player player = server.addNewPlayer(nickname, roomJoined);
            tui.Welcome(player);
            waitingForPlayers = true;
            listenToPlayers();
            System.out.println("All players have joined, lets start the game!");
            startEarlyGame();
            startNormalGame();
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
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
    }

    private void startEarlyGame() throws IOException, InvalidPositionException {
        StarterCard st = server.getFirstCard( roomJoined);
        if(tui.showStarterCard(st)){
            st.flipCard();
        }
        server.setFirstCard(st, nickname, roomJoined);
        ObjectiveCard[] obj = server.getObjCards(roomJoined);
        server.setObjSecret(obj[tui.chooseObjective(obj[0],obj[1])-1], nickname, roomJoined);
    }

    private void startNormalGame() throws RemoteException, MissingResourcesException, InvalidPositionException {

        while(!server.isGameOver(roomJoined)) {//fino a fine gioco, gestire primo turno
            while (!server.isCurrentPlayer(roomJoined, nickname)) {
                if (server.isGameOver(roomJoined)) {
                    break;
                }else{
                    Room room = server.getRooms().getRoom(roomJoined);
                    //checkYourTurn();
                    tui.notYourTurn(room.getGame(), room.getGame().getPlayer(nickname));
                }
            }
            while(server.isCurrentPlayer(roomJoined, nickname)) {
                if (server.isGameOver(roomJoined)) {
                    break;
                }else{
                    Game game = server.getRooms().getRoom(roomJoined).getGame();
                    Player player = game.getPlayer(nickname);
                    if(tui.yourTurnPlay(game, player)){
                        PlayableCard card = tui.inputCardToPlace(player);
                        server.placeCard(card, tui.inputCoordinates(player), roomJoined, nickname);
                        game = server.getRooms().getRoom(roomJoined).getGame();
                        player = game.getPlayer(nickname);
                        drawCardFromDeck(tui.yourTurnDraw(game, player));
                        server.nextRound(roomJoined);
                    }
                }
            }

        }
        tui.winnersPrint(server.getMultiWinners(roomJoined));
    }

    private String controlNickname(boolean choice) throws RemoteException {
        String nickname = tui.insertNickname(choice);
        if(server.getRooms().alredyInGame(server.getRooms().getRoom(roomJoined).getGame(),nickname)){
            return controlNickname(false);
        }else{
            return nickname;
        }
    }

    private String controlRoom(boolean choice, ArrayList<Room> rooms) throws RemoteException{
        String roomName = tui.getRoomName(choice, rooms);
        if(server.getRooms().alredyExist(roomName)){
            return controlRoom(choice, rooms);
        } else {
            return roomName;
        }
    }
    private String controlRoom2(boolean choice, ArrayList<Room> rooms) throws RemoteException{
        String roomName = tui.getRoomName(choice, rooms);
        if(server.getRooms().alredyExist(roomName)){
            return roomName;
        } else {
            return controlRoom(choice, rooms);

        }
    }

    private void checkYourTurn(){
        Runnable myThread = () ->
        {
            while (true) {
                try {
                    if (server.isCurrentPlayer(roomJoined, nickname)) break;
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                tui.yourTurnPlay(server.getRooms().getRoom(roomJoined).getGame(), server.getRooms().getRoom(roomJoined).getGame().getPlayer(nickname));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
        Thread run = new Thread(myThread);
        run.start();
    }

    private void drawCardFromDeck(int position){
        // 0: scoperta resource 1: scoperta resource 2: top deck resource 4...
            try {
                switch (position) {
                    case 0 -> {
                        server.drawCard(0,0,roomJoined, nickname);
                    }
                    case 1 -> {
                        server.drawCard(0,1,roomJoined, nickname);
                    }
                    case 2 -> {
                        server.drawCard(0,2,roomJoined, nickname);
                    }
                    case 3 -> {
                        server.drawCard(1,0,roomJoined, nickname);
                    }
                    case 4 -> {
                        server.drawCard(1,1,roomJoined, nickname);
                    }
                    case 5 -> {
                        server.drawCard(1,2,roomJoined, nickname);
                    }
                    default -> throw new Exception();
                };
            } catch (IOException | ClassNotFoundException e) {
                //gestire ecc
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    private ArrayList<Player> getPlayers() throws RemoteException {
        return server.getRooms().getRoom(roomJoined).getGame().getPlayers();
    }

}
