package it.polimi.ingsw.Controller;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;


import java.util.ArrayList;

public class RoomController {
    ArrayList<Room> rooms;
    public RoomController() {
        this.rooms = new ArrayList<Room>();
    }

    public synchronized ArrayList<Room> getRooms() {
        return rooms;
    }
    public synchronized void addRoom(Room room) {
        this.rooms.add(room);
    }
    public synchronized void removeRoom(Room room) {
        this.rooms.remove(room);
    }
    public synchronized Room getRoom(String roomName) {
        for (Room room : rooms) {
            if(room.getName().equals(roomName)){
                return room;
            }
        }
        return null;
    }
    public synchronized boolean alredyExist(String s){// return false, not alredy exist
        boolean flag = false;
        for(Room r : rooms){
            if(r.getName().equals(s)){
                flag=true;
                break;
            }
        }
        return flag;
    }
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
