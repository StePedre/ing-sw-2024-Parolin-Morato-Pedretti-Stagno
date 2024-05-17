package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StarterCard;

import java.io.*;
import java.util.ArrayList;

public class GUIClientSocket {
    ObjectInputStream in;
    ObjectOutputStream  out;

    public GUIClientSocket(InputStream in, OutputStream out) throws IOException {
        this.in = new ObjectInputStream(in);
        this.out = new ObjectOutputStream(out);
    }
    public void reset() throws IOException {
        in.reset();
    }
    public Player receivePlayerFromServer() throws IOException, ClassNotFoundException {
        return (Player) in.readObject();
    }
    public Room receiveRoomFromServer() throws IOException, ClassNotFoundException {
        return (Room) in.readObject();
    }
    public ObjectiveCard[] receiveSecretObjsFromServer() throws IOException, ClassNotFoundException {
        return (ObjectiveCard[]) in.readObject();
    }
    public ArrayList<Room> receiveRoomsFromServer() throws IOException, ClassNotFoundException {
        return (ArrayList<Room>) in.readObject();
    }
    public void sendToServer(Object obj) throws IOException {
        out.writeObject(obj);
    }
    public boolean receiveBooleanFromServer() throws IOException, ClassNotFoundException {
        return (Boolean) in.readObject();
    }
    public StarterCard receiveStarterCardFromServer() throws IOException, ClassNotFoundException {
        return (StarterCard) in.readObject();
    }
    public Game receiveGameFromServer() throws IOException, ClassNotFoundException {
        return (Game) in.readObject();
    }
    public ArrayList<Player> receiveWinnersFromServer() throws IOException, ClassNotFoundException {
        return (ArrayList<Player>) in.readObject();
    }

}

