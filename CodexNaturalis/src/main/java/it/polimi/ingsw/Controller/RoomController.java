package it.polimi.ingsw.Controller;

import it.polimi.ingsw.CS.Room;



import java.util.ArrayList;

public class RoomController {
    ArrayList<Room> rooms = null;
    public RoomController() {
        this.rooms = new ArrayList<Room>();
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
    public void addRoom(Room room) {
        this.rooms.add(room);
    }
    public void removeRoom(Room room) {
        this.rooms.remove(room);
    }
    public Room getRoom(String roomName) {
        for (Room room : rooms) {
            if(room.getName().equals(roomName)){
                return room;
            }
        }
        return null;
    }
}
