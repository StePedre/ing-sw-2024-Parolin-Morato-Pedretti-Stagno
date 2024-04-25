package it.polimi.ingsw.CS;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MyClientSocket {
    private Socket socket= null;
    private  InputStream in = null;
    private  OutputStream out = null;
    public MyClientSocket(int port, String host) throws IOException {
        socket = new Socket(host, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }
    public void runClient(){
        // inserire funzione client
    }
    public void close() throws IOException {
        socket.close();
    }
}
