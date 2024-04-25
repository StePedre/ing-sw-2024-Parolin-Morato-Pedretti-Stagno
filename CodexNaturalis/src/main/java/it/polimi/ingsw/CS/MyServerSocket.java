package it.polimi.ingsw.CS;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MyServerSocket {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(59090); //trovare porta su cui lavorare
        Socket connection = serverSocket.accept();


        connection.close();
        serverSocket.close();
    }
}
