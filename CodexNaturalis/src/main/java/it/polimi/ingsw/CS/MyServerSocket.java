package it.polimi.ingsw.CS;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MyServerSocket {
    private ServerSocket serverSocket = null;
    private Socket connection = null;
    public MyServerSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port); //trovare porta su cui lavorare e aggiungere try-catch
    }
    public void runServer() throws IOException {
        while(true) { //forse modificabile comn flag
            connection = serverSocket.accept();
            //inserire funzione creazione thread
        }
    }

    public void close() throws IOException {
        connection.close();
        serverSocket.close();
    }
}
