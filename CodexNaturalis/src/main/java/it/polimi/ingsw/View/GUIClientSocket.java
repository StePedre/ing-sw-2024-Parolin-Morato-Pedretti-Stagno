package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GUIClientSocket {
    ObjectInputStream in;
    ObjectOutputStream  out;

    public GUIClientSocket(ObjectInputStream in, ObjectOutputStream out){
        this.in = in;
        this.out = out;
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
}

