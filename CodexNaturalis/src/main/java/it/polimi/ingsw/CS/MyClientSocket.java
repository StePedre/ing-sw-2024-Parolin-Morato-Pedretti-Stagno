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
    public MyClientSocket(int port, String host) throws IOException {
        socket = new Socket(host, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }
    public void runClient() throws IOException, ClassNotFoundException {// per test println
        if(!in.readBoolean()){
            throw new ConnectException();
        }
        System.out.println("Client connected");

        if(true) {//decisione se usare TUI o GUI
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
        game = (Game) in.readObject();
        player = (Player) in.readObject();
        ObjectiveCard[] objs =(ObjectiveCard[]) in.readObject();
        out.writeObject(tui.chooseObjective(objs[0],objs[1]));
        out.writeBoolean(tui.showStarterCard((StarterCard) in.readObject()));
    }
    public void useGUI(){}

    public void close() throws IOException {
        socket.close();
    }
}
