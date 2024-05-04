package it.polimi.ingsw.CS;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.View.*;
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
        out.writeObject(tui.insertNickname());
        if(in.readBoolean()){
            out.writeObject(tui.askPlayersNo());
        }
        player = (Player) in.readObject();
        tui.Welcome(player);
        updateData();
        ObjectiveCard[] objs =(ObjectiveCard[]) in.readObject();
        out.writeObject(tui.chooseObjective(objs[0],objs[1]));
        out.writeBoolean(tui.showStarterCard((StarterCard) in.readObject()));
        updateData();
        Thread t = new Thread(()->{while(true) {
                                        tui.notYourTurn(game, player);
                                    }
                                });
        //aspettare turno
        while(true){
            if(in.readBoolean()){
                if(t.isAlive()){
                    t.interrupt();
                }
                updateData();
                while(!tui.yourTurnPlay(game,player)){//quando vero ha deciso cosa giocare
                }
                out.writeObject(tui.inputCardToPlace(player));
                out.writeObject(tui.inputCoordinates());
                out.writeObject(tui.yourTurnDraw(game,player));
                updateData();
            }
            else{
                t.start();// modificabile mettendo fuori dal ciclo e a fine turno
            }
        }

    }
    public void useGUI(){}

    public void close() throws IOException {
        socket.close();
    }
    public void updateData() throws IOException, ClassNotFoundException {
        game = (Game) in.readObject();
        player = (Player) in.readObject();
    }
}
