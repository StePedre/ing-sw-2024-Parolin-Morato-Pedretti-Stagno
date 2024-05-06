package it.polimi.ingsw.CS;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.View.*;

import java.util.ArrayList;


public class MyClientSocket {
    @SuppressWarnings("FieldCanBeLocal")
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
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
        //chiede nickname
        out.writeObject(tui.insertNickname());
        // mostra stanze
        ArrayList<Room> room = (ArrayList<Room>) in.readObject();
        tui.showRoom(room);
        /*out.writeObject(/*risulotato tui); // se vero craere stanza, falso joinare
        out.writeObject(/*nome stranza);*/
        //testStanze();
        if(tui.chooseRoom()){
            out.writeObject(true);
            out.writeObject(tui.getRoomName(true));
        }else{
            out.writeObject(false);
            out.writeObject(tui.getRoomName(false));
        }
        //ciclo nome stanza esistente
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
                while(!tui.yourTurnPlay(game,player)){
                    doNothing();//quando vero ha deciso cosa giocare
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
        close();
    }
    public void useGUI(){}

    public void close() throws IOException {
        socket.close();
    }
    public void updateData() throws IOException, ClassNotFoundException {
        game = (Game) in.readObject();
        player = (Player) in.readObject();
    }
    public void doNothing(){

    }
}
