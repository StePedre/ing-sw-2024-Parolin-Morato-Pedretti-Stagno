package it.polimi.ingsw.CS;

import java.io.IOException;
import java.net.ConnectException;

public class Client {
    public static void main(String[] args) {
        try{//chiedere Gui/Tui e tipo di connesione
            MyClientSocket myClientSocket = new MyClientSocket(59090,"127.0.0.1");
            myClientSocket.runClient();
        }
        catch (ConnectException e){
            System.out.println("Numero di giocatori massimo raggiunto o nessun server attivo");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            //aggiugni ecc
        }
    }
}
