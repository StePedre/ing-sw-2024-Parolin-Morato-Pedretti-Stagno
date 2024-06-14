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
        this.rooms = new ArrayList<Room>();
    }

    /**
     * return the list of the active rooms
     *
     * @return the available rooms
     */
    public synchronized ArrayList<Room> getRooms() {
        ArrayList<Room> availableRoom= new ArrayList<Room>();
        for(Room room : rooms) {
            if(room.getPlayerInRoom() != room.getGame().getExpPlayers()){
                availableRoom.add(room);
            }
        }
        return availableRoom;
    }

    /**
     * add a new room on the list of the available rooms
     *
     * @param room the new room
     */
    public synchronized void addRoom(Room room) {
        this.rooms.add(room);
    }

    /**
     * remove a room from the list of room
     *
     * @param room the room to remove
     */
    public synchronized void removeRoom(Room room) {
        this.rooms.remove(room);
    }

    /**
     * return a room based on his name
     *
     * @param roomName the name of the room
     * @return the room
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
     * Check if already exist a room with the same name
     *
     * @param s the name of the room
     * @return true if the name already exist, false otherwise
     */
    public synchronized boolean alredyExist(String s){// return false if not alredy exist
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
     * Check if in a room already exist a player with a specified nickname
     *
     * @param game the game to check
     * @param s the nickname of the player
     * @return true if the nickname already exist, false otherwise
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
