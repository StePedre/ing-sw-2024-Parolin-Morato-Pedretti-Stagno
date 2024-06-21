package it.polimi.ingsw.Controller;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomControllerTest {
    RoomController rc = new RoomController();
    Room r1 = new Room("room1");
    Room r2 = new Room("room2");

    @Test
    void addAndGetAndRemove(){
        rc.addRoom(r1);
        rc.addRoom(r2);
        for(Room r: rc.getRooms()){
            System.out.println(r.getName());
        }
        assertEquals(rc.getRoom(r2.getName()), r2.getRoom());
        rc.removeRoom(r2);
        assertNull(rc.getRoom(r2.getName()));
        assertNotNull(rc.getRoom(r1.getName()));
    }
    @Test
    void alreadyExists(){
        rc.addRoom(r1);
        assertTrue(rc.alredyExist(r1.getName()));
        assertFalse(rc.alredyExist(r2.getName()));
    }
    @Test
    void alreadyInGame(){
        Game game = new Game();
        r1.parse(game);
        r1.getGame().setExpPlayers(1);
        r1.getGame().addPlayer(new Player("silvia"));
        rc.addRoom(r1);
        assertTrue(rc.alreadyInGame(r1.getName(), "silvia"));
        assertFalse(rc.alreadyInGame(r1.getName(), "teo"));
    }

}