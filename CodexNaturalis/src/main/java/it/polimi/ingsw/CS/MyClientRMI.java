package it.polimi.ingsw.CS;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.TUI;

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
    public void runClient() throws RemoteException {
        System.out.println("Client connected");

        if(inter) {//decisione se usare TUI o GUI
            useTUI();
        }
        else{
            useGUI();
        }
    }

    private void useTUI() throws RemoteException {
        tui = new TUI();
        String nickname = tui.insertNickname();
        tui.showRoom(server.showRooms());
        if(tui.chooseRoom()){
            roomJoined = tui.getRoomName(true);
            server.addRoom(roomJoined);
            server.setPlayerNumber(tui.askPlayersNo(), roomJoined);
        }else{
            roomJoined = tui.getRoomName(false);
        }
            player = server.addNewPlayer(nickname, roomJoined);
            tui.Welcome(player);
            waitingForPlayers = true;
            listenToPlayers();
    }

    private void listenToPlayers() {
        Thread t = new Thread(() -> {
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

                    Thread.sleep(1000);
                }
            } catch (RemoteException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
    }

    private ArrayList<Player> getPlayers() throws RemoteException {
        return server.getRooms().getRoom(roomJoined).getGame().getPlayers();
    }
    private void useGUI(){

    }


}
