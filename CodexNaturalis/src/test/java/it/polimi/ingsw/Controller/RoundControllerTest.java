package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundControllerTest {
    RoundController rc = new RoundController();
    Player p1 = new Player("silvia");
    Player p2 = new Player("teo");
    Player p3 = new Player("ste");
    Player p4 = new Player("andre");

    @Test
    void setFirstAndNextRound(){
        rc.addPlayer(p1);
        rc.setFirstPlayer();
        assertEquals(rc.getLastPlayer(), rc.getCurrentPlayer());
        rc.addPlayer(p2);
        assertNotEquals(rc.getLastPlayer(), p2);
        assertNotEquals(rc.getCurrentPlayer(), p2);
        assertEquals(rc.getCurrentPlayer(), p1);
        rc.nextRound();
        assertEquals(rc.getCurrentPlayer(), p2);
        assertEquals(rc.getLastPlayer(), p1);
    }
    @Test
    void lastTurn(){
        rc.addPlayer(p1);
        rc.setFirstPlayer();
        rc.nextRound();
        assertFalse(rc.isLastTurn());
        rc.setEnding();
        rc.nextRound();
        assertTrue(rc.isLastTurn());
    }
}