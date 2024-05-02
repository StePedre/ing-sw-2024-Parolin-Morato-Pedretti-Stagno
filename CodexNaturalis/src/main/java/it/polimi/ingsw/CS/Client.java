package it.polimi.ingsw.CS;

import java.io.IOException;

public class Client {
    public static void main(String[] args) {
        try{
            MyClientSocket myClientSocket = new MyClientSocket(59090,"127.0.0.1");
            myClientSocket.runClient();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            //aggiugni ecc
        }
    }
}
