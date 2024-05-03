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
    private Scanner scan = new Scanner(System.in);
    Player player = null;
    Game game = null;
    CLI cli =  null;
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
        String s = (String )in.readObject();
        System.out.println(s);
        out.writeObject(scan.nextLine());
        if(in.readBoolean()){//se primo player, aggiungere sta roba alla classe CLI
            s = (String) in.readObject();
            System.out.println(s);
            int n = scan.nextInt();
            out.writeObject(n);
        }
        s= (String) in.readObject();
        System.out.println(s);
        if(true) {//decisione se usare TUI o GUI
            useTUI();
        }
        else{
            useGUI();
        }
    }
    public void useTUI() throws IOException, ClassNotFoundException {
        cli= new CLI();
        game = (Game) in.readObject();
        player = (Player) in.readObject();
        cli.Welcome(player);
    }
    public void useGUI(){}

    public void close() throws IOException {
        socket.close();
    }
}
