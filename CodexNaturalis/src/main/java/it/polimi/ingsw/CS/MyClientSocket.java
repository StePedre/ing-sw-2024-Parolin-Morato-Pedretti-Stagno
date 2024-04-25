package it.polimi.ingsw.CS;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MyClientSocket {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",59090);
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();


        socket.close();
    }
}
