package it.polimi.ingsw.View;

import it.polimi.ingsw.Controller.ParsingController;
import it.polimi.ingsw.Model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TUITest {
    Player p1 = new Player("Silvia");
    Player p2 = new Player("Gabbo");
    Player p3 = new Player("Stefano");
    Player p4 = new Player("Matteo");
    PlayerGround pg1 = new PlayerGround();
    PlayerGround pg2 = new PlayerGround();
    PlayerGround pg3 = new PlayerGround();
    PlayerGround pg4 = new PlayerGround();
    ArrayList<Player> playerss = new ArrayList<>();
    Deck[] decks = new Deck[2];
    ObjectiveCard[] comObj= new ObjectiveCard[2];

    @Test
    void winners(){
        p1.setPlayerGround(pg1);
        p2.setPlayerGround(pg2);
        p3.setPlayerGround(pg3);
        p4.setPlayerGround(pg4);
        p1.setReachedObjNo(3);
        p2.setReachedObjNo(1);
        p3.setReachedObjNo(2);    //Note: to try every possible case, change scores and reached objectives (they all work)
        p4.setReachedObjNo(3);
        p1.getPlayerGround().setPlayerScore(22);
        p2.getPlayerGround().setPlayerScore(22);
        p3.getPlayerGround().setPlayerScore(13);
        p4.getPlayerGround().setPlayerScore(22);
        playerss.add(p1);
        playerss.add(p2);
        playerss.add(p3);
        playerss.add(p4);

        Game game =  new Game(playerss, decks, comObj);
        TUI tui = new TUI();
        game.finish();
        ArrayList<Player> winners = game.getMultiWinners();
        tui.winnersPrint(winners);
    }


}