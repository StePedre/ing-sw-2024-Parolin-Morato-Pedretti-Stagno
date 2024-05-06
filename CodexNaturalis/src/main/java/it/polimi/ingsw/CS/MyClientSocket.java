package it.polimi.ingsw.CS;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.View.*;

import java.util.ArrayList;
import java.util.Scanner;

public class MyClientSocket {
    private Socket socket= null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private final Scanner scan = new Scanner(System.in);
    Player player = null;
    Game game = null;
    TUI tui =  null;
    boolean inter;
    public MyClientSocket(int port, String host,boolean inter) throws IOException {
        socket = new Socket(host, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        this.inter = inter;
    }
    public void runClient() throws IOException, ClassNotFoundException {// per test println
        if(!in.readBoolean()){
            throw new ConnectException();
        }
        System.out.println("Client connected");

        if(inter) {//decisione se usare TUI o GUI
            useTUI();
        }
        else{
            useGUI();
        }
    }
    public void useTUI() throws IOException, ClassNotFoundException {
        tui= new TUI();
        // mostra stanze
        ArrayList<Room> roomSockets = (ArrayList<Room>) in.readObject();
        for(Room room : roomSockets){
            System.out.println(room.getName());
        }
        /*out.writeObject(/*risulotato tui); // se vero craere stanza, falso joinare
        out.writeObject(/*nome stranza);*/
        testStanze();
        //ciclo nome stanza esistente
        out.writeObject(tui.insertNickname());
        if((boolean)in.readObject()){
            int i = tui.askPlayersNo();
            out.writeObject(i);
        }
        player = (Player) in.readObject();
        tui.Welcome(player);
        updateData();
        ObjectiveCard[] objs =(ObjectiveCard[]) in.readObject();
        out.writeObject(tui.chooseObjective(objs[0],objs[1]));
        out.writeObject(tui.showStarterCard((StarterCard) in.readObject()));
        updateData();
        Thread t = new Thread(()->{while(true) {
                                        tui.notYourTurn(game, player);
                                    }
                                });
        //aspettare turno
        while(true){
            if((boolean)in.readObject()){
                if(t.isAlive()){
                    t.interrupt();
                }
                if((boolean)in.readObject()){//finito gioco per vittoria altrui
                    break;
                }
                updateData();
                while(!tui.yourTurnPlay(game,player)){//quando vero ha deciso cosa giocare
                }
                out.writeObject(tui.inputCardToPlace(player));
                out.writeObject(tui.inputCoordinates());
                //aspetta riscontro vittoria
                if((boolean)in.readObject()){
                    break;
                }
                out.writeObject(tui.yourTurnDraw(game,player));
                //aspetta riscontro vittoria
                if((boolean) in.readObject()){
                    break;
                }
                updateData();
            }
            else{
                t.start();// modificabile mettendo fuori dal ciclo e a fine turno
                //aspetta riscontro vittoria
            }
        }
        tui.winnersPrint((ArrayList<Player>) in.readObject());
    }
    public void useGUI(){}

    public void close() throws IOException {
        socket.close();
    }
    public void updateData() throws IOException, ClassNotFoundException {
        game = (Game) in.readObject();
        player = (Player) in.readObject();
    }
    public void testStanze() throws IOException, ClassNotFoundException {
        Scanner s = new Scanner(System.in);
        //System.out.println("Vuoi creare una stanza?");
        System.out.println((String) in.readObject());
        if(s.nextLine().equalsIgnoreCase("si")){
            out.writeObject(true);
            System.out.println((String) in.readObject());
            String st = s.nextLine();
            out.writeObject(st);
        }
        else{
            out.writeObject(false);// se vero craere stanza, falso joinare
            System.out.println((String)in.readObject());
            String st = s.nextLine();
            out.writeObject(st);
        }

    }
}
