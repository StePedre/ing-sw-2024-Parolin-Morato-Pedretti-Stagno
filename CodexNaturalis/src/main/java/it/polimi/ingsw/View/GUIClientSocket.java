package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StarterCard;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * The class GUIClientSocket contains all the methods needed in order to send and receive Objects to and from the server.
 * It has an ObjectOutputStream used to send Objects, and an ObjectInputStream used to receive Objects.
 */

public class GUIClientSocket {
    ObjectInputStream in;
    ObjectOutputStream  out;

    /**
     * Class constructor.
     * It sets the two objectStreams.
     *
     * @param in is the ObjectInputStream to set.
     * @param out is the ObjectOutputStream to set.
     * @throws IOException if there has been problems regarding input or output.
     */
    public GUIClientSocket(InputStream in, OutputStream out) throws IOException {
        this.in = new ObjectInputStream(in);
        this.out = new ObjectOutputStream(out);
    }

    /**
     * This method receives a boolean from the server.
     *
     * @return such boolean.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public boolean receiveBooleanFromServer() throws IOException, ClassNotFoundException {
        return (Boolean) in.readObject();
    }

    /**
     * This method receives the colors from the server.
     *
     * @return such array of colors.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public Set<String> receiveColorsFromServer() throws IOException, ClassNotFoundException {
        return (Set<String>) in.readObject();
    }

    /**
     * This method receives an updated instance of the game from the server.
     *
     * @return such game.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public Game receiveGameFromServer() throws IOException, ClassNotFoundException {
        return (Game) in.readObject();
    }

    /**
     * This method receives an updated instance of the Player from the server.
     *
     * @return such Player.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public Player receivePlayerFromServer() throws IOException, ClassNotFoundException {
        return (Player) in.readObject();
    }

    /**
     * This method receives the rooms from the server.
     *
     * @return such list of Room.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public ArrayList<Room> receiveRoomsFromServer() throws IOException, ClassNotFoundException {
        return (ArrayList<Room>) in.readObject();
    }

    /**
     * This method receives the two common objective cards from the server.
     *
     * @return an array of ObjectiveCards that contains the two common objective.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public ObjectiveCard[] receiveSecretObjsFromServer() throws IOException, ClassNotFoundException {
        return (ObjectiveCard[]) in.readObject();
    }

    /**
     * This method receives the starter card of a player from the server.
     *
     * @return such StarterCard.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public StarterCard receiveStarterCardFromServer() throws IOException, ClassNotFoundException {
        return (StarterCard) in.readObject();
    }

    /**
     * This method receives a string from the server.
     *
     * @return such String.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public String receiveString() throws IOException, ClassNotFoundException {
        return (String) in.readObject();
    }

    /**
     * This method receives the winner (or the winners) of the game from the server.
     *
     * @return such list of players.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public ArrayList<Player> receiveWinnersFromServer() throws IOException, ClassNotFoundException {
        return (ArrayList<Player>) in.readObject();
    }

    /**
     * This method resets the input stream.
     *
     * @throws IOException if there has been problems regarding input or output.
     */
    public void reset() throws IOException {
        in.reset();
    }

    /**
     * This method uses the output stream to send an Object to the server.
     *
     * @param obj is the object to send.
     * @throws IOException if there has been problems regarding input or output.
     */
    public void sendToServer(Object obj) throws IOException {
        out.writeObject(obj);
    }

}

