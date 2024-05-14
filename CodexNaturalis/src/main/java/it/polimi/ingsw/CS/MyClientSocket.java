package it.polimi.ingsw.CS;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.View.*;
import javafx.application.Application;

import java.util.ArrayList;


public class MyClientSocket {
    private  Socket socket;
    private  ObjectInputStream in;
    private  ObjectOutputStream out;
    Player player = null;
    Game game = null;
    TUI tui =  null;
    boolean inter;
    public MyClientSocket(boolean inter) {
        this.inter = inter;
    }
    public void runClient() throws IOException {// per test println
        try {
            System.out.println("Client connected");
            if(inter) {
                useTUI(59090, "127.0.0.1");
            }else{
                useGUI();
            }
        }catch (IOException e) {
            close();
            throw e;
        } catch (ClassNotFoundException e) {
            close();
            e.printStackTrace();
        } catch (InterruptedException e){
            close();
            e.printStackTrace();
        }
    }
    public void useTUI(int port, String host) throws IOException, ClassNotFoundException, InterruptedException {
        tui= new TUI();
        socket = new Socket(host, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        // mostra stanze
        ArrayList<Room> room = (ArrayList<Room>) in.readObject();
        tui.showRoom(room);
        do {
            if (tui.chooseRoom()) { //create
                out.writeObject(true);
                out.writeObject(tui.getRoomName(true, true));
            } else { //join
                out.writeObject(false);
                out.writeObject(tui.getRoomName(false, true));
            }
        }while((boolean)in.readObject());
        //chiede nickname
        do {
            out.writeObject(tui.insertNickname(true));
        }while((boolean) in.readObject());
        //ciclo nome stanza esistente
        boolean NoOk = false;
        if((boolean)in.readObject()){
            do {
                int i = tui.askPlayersNo();
                if(i>=2 && i<=4) {
                    out.writeObject(i);
                    NoOk = true;
                }
            }while(!NoOk);
        }
        player = (Player) in.readObject();
        tui.Welcome(player);
        ObjectiveCard[] objs =(ObjectiveCard[]) in.readObject();
        out.writeObject(tui.chooseObjective(objs[0],objs[1]));
        out.writeObject(tui.showStarterCard((StarterCard) in.readObject()));
        //readPlayer();
        //tui.showHand(player);
        updateData();
        tui.Welcome(player);
        tui.showGround(game,player);
        //aspettare turno
        out.reset();
        boolean b =  (boolean) in.readObject();
        while(true){
            if(b){
                if((boolean)in.readObject()){//finito gioco per vittoria altrui
                    break;
                }
                updateData();
                while(!tui.yourTurnPlay(game,player)){
                }
                out.writeObject(tui.inputCardToPlace(player));
                out.writeObject(tui.inputCoordinates());
                out.reset();
                player = (Player) in.readObject();
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
                b=false;
            }
            else{
                while(!b) {
                    tui.notYourTurn(game,player);//aggiungere possibilità di aspettare e basta
                    out.writeObject(true);
                    b=(boolean)in.readObject();
                }
            }
        }
        tui.winnersPrint((ArrayList<Player>) in.readObject());
        close();
    }

    public void close() throws IOException {
        socket.close();
    }
    public void updateData() throws IOException, ClassNotFoundException {
        game = (Game) in.readObject();
        player = (Player) in.readObject();
    }
    public void useGUI(){
        GUI.startGUI();
    }
}
