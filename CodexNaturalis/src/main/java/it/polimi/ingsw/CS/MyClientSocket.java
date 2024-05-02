package it.polimi.ingsw.CS;
import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Scanner;

public class MyClientSocket {
    private Socket socket= null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private Scanner scan = new Scanner(System.in);
    public MyClientSocket(int port, String host) throws IOException {
        socket = new Socket(host, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }
    public void runClient() throws IOException, ClassNotFoundException {// per test println
        System.out.println("Client connected");
        String s = (String )in.readObject();
        System.out.println(s);
        out.writeObject(scan.nextLine());
        if(in.readBoolean()){//se primo player
            s = (String) in.readObject();
            System.out.println(s);
            int n = scan.nextInt();
            out.writeObject(n);
        }
        s= (String) in.readObject();
        System.out.println(s);
        while(true){

        }
    }
    public void close() throws IOException {
        socket.close();
    }
}
