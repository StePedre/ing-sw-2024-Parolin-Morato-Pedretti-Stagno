package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.Player;

import java.io.*;

public class GUIClientSocket {
    ObjectInputStream in;
    ObjectOutputStream  out;

    public GUIClientSocket(InputStream in, OutputStream out) throws IOException {
        this.in = new ObjectInputStream(in);
        this.out = new ObjectOutputStream(out);
    }
    public Player receivePlayerFromServer() throws IOException, ClassNotFoundException {
        return (Player) in.readObject();
    }
    public Room receiveRoomFromServer() throws IOException, ClassNotFoundException {
        return (Room) in.readObject();
    }
    public void sendToServer(Object obj) throws IOException {
        out.writeObject(obj);
    }
    public boolean receiveBooleanFromServer() throws IOException, ClassNotFoundException {
        return (Boolean) in.readObject();
    }
}

