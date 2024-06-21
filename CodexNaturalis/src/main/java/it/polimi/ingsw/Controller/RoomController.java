package it.polimi.ingsw.Controller;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class RoomController implements the controller in charge of managing the selection and creation of rooms in order
 * to play a game. Every method is synchronized, which means each thread calling the method needs to wait
 *  * for another invocation (if existing) to finish.
 * There is a list of Room that contains all the rooms created.
 * The controller needs to add or remove a room to the list, check if a room with the same name already exists and check
 * if in a room already exist a player with a specified nickname.
 */

public class RoomController implements Serializable {
    ArrayList<Room> rooms;

    /**
     * Class constructor.
     * It initializes the rooms.
     */
    public RoomController() {
        this.rooms = new ArrayList<>();
    }

    /**
     * The method returns the list of the active rooms.
     *
     * @return the available rooms.
     */
    public synchronized ArrayList<Room> getRooms() {
        ArrayList<Room> availableRoom= new ArrayList<>();
        for(Room room : rooms) {
            if(room.getPlayerInRoom() != room.getGame().getExpPlayers()){
                availableRoom.add(room);
            }
        }
        return availableRoom;
    }

    /**
     * The method adds a new room to the list of the available rooms.
     *
     * @param room is the new room.
     */
    public synchronized void addRoom(Room room) {
        this.rooms.add(room);
    }

    /**
     * The method removes a room from the list of rooms.
     *
     * @param room is the room to remove.
     */
    public synchronized void removeRoom(Room room) {
        this.rooms.remove(room);
    }

    /**
     * The method returns a room based on its name.
     *
     * @param roomName is the name of the room.
     * @return the room object.
     */
    public synchronized Room getRoom(String roomName) {
        for (Room room : rooms) {
            if(room.getName().equals(roomName)){
                return room;
            }
        }
        return null;
    }

    /**
     * The method checks if a room with the same name already exists.
     *
     * @param s is the name of the room.
     * @return true if the name already exists, false otherwise.
     */
    public synchronized boolean alredyExist(String s){
        boolean flag = false;
        for(Room r : rooms){
            if(r.getName().equals(s)){
                flag=true;
                break;
            }
        }
        return flag;
    }

    /**
     * The method checks if a player with a specified nickname already exist in a room.
     *
     * @param game is the game to check.
     * @param s is the nickname of the player.
     * @return true if the nickname already exists, false otherwise.
     */
    public synchronized boolean alredyInGame(Game game, String s){
        boolean flag = false;
        for(Player p: game.getPlayers()){
            if(p.getNickname().equals(s)){
                flag=true;
                break;
            }
        }
        return flag;
    }
}
