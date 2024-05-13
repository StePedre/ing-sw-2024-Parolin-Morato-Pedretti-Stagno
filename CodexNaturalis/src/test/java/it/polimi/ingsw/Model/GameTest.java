package it.polimi.ingsw.Model;

import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

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
    void finish() { // tests if finish() method works based on winners name (they must match the given scores)
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
        game.finish();
        if(game.isOver()){
            ArrayList<Player> winners = game.getMultiWinners();
            for(Player p: winners){
                System.out.println(p.getNickname());
            }
        }
    }

    @Test
    void isFirstFalse() {   //2 players added -> there is already a first player -> false
        Game game = new Game(playerss, decks, comObj);
        game.addPlayer(p1);
        game.addPlayer(p2);
        boolean flag = game.isFirst();
        assertFalse(flag);
    }

    @Test
    void isFirstTrue() {   //no players added -> there is no first player -> true
        Game game = new Game(playerss, decks, comObj);
        boolean flag = game.isFirst();
        assertTrue(flag);
    }

    @Test
    void addPlayer(){  //should stop at 4 players, therefore should print p4.nickname
        Player p5 = new Player("Andrea");
        Game game = new Game(playerss, decks, comObj);
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.addPlayer(p4);
        game.addPlayer(p5);
        System.out.println(playerss.getLast().getNickname());
    }
}