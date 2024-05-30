package it.polimi.ingsw.CS;
import java.io.*;
import java.net.Socket;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.View.*;

import java.util.ArrayList;
import java.util.Scanner;


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
                System.out.println("Write the IP of the server");
                Scanner s = new Scanner(System.in);
                useTUI(59090, s.nextLine());
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
        do {
            out.writeObject(tui.insertNickname(true));
        }while((boolean) in.readObject());
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
        do{
            game = (Game) in.readObject();
            out.writeObject(tui.chooseColor(game));
        }while((boolean)in.readObject());
        player = (Player) in.readObject();
        in.readObject();//legge false per non numero di player
        tui.Welcome(player);
        in.readObject();//legge true per raggiungimento numero player
        out.writeObject(tui.showStarterCard((StarterCard) in.readObject()));
        ObjectiveCard[] objs =(ObjectiveCard[]) in.readObject();
        out.writeObject(tui.chooseObjective(objs[0],objs[1]));
        updateData();
        tui.Welcome(player);
        tui.showGround(game,player);
        out.reset();
        boolean b =  (boolean) in.readObject();
        while(true){
            if(b){
                updateData();
                tui.yourTurnPlay(game,player);
                if((boolean) in.readObject()){
                    System.out.println("è l'ultimo turno");// Aggiungere a tui
                }
                out.writeObject(tui.inputCardToPlace(player));
                out.writeObject(tui.inputCoordinates(player));
                out.reset();
                player = (Player) in.readObject();
                if((boolean)in.readObject()){
                    break;
                }
                if((boolean)in.readObject()) {
                    out.writeObject(tui.yourTurnDraw(game, player));
                }
                updateData();
                b=(boolean) in.readObject();
            }
            else{
                out.writeObject(tui.notYourTurn(game,player));//aggiungere possibilità di aspettare e basta
                b=(boolean)in.readObject();
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
        GUIsocket.startGUI();
    }
}
