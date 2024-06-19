package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.ParsingController;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import javax.swing.text.ParagraphView;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

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
    void isFirst() {
        Game game = new Game(playerss, decks, comObj);
        assertTrue(game.isFirst());
        game.setExpPlayers(2);
        game.addPlayer(p1);
        game.addPlayer(p2);
        assertFalse(game.isFirst());
    }

    @Test
    void addPlayer(){
        Player p5 = new Player("Andrea");
        Game game = new Game(playerss, decks, comObj);
        game.setExpPlayers(4);
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.addPlayer(p4);
        game.addPlayer(p5);
        assertEquals(playerss.getLast().getNickname(), p4.getNickname());
    }

    @Test
    void pickPlayerObj() throws IOException, ParseException {
        Game game = new Game();
        ParsingController pc = new ParsingController();
        game.setOtherObjs(pc.createObjectiveCardsArray());
        ObjectiveCard[] objs = game.pickPlayerObj();
        System.out.println(objs[0].getRule().getName());
        System.out.println(objs[1].getRule().getName());
    }

    @Test
    void RemovePlayer(){
        Game game = new Game();
        game.setExpPlayers(3);
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.addPlayer(p3);
        game.removePlayer(p3.getNickname());
        assertEquals(game.getNumPlayer(), game.getExpPlayers()-1);
        assertEquals(game.getPlayers().getLast(), p2);
    }

    @Test
    void finish() throws IOException, ParseException {
        p1.setPlayerGround(pg1);
        p2.setPlayerGround(pg2);
        p3.setPlayerGround(pg3);
        p4.setPlayerGround(pg4);
        p1.setReachedObjNo(3);
        p2.setReachedObjNo(1);
        p3.setReachedObjNo(2);    //Note: to try every possible case, change scores and reached objectives
        p4.setReachedObjNo(3);
        p1.getPlayerGround().setPlayerScore(22);
        p2.getPlayerGround().setPlayerScore(22);
        p3.getPlayerGround().setPlayerScore(13);
        p4.getPlayerGround().setPlayerScore(22);
        ParsingController pc = new ParsingController();
        comObj[0] = pc.createObjectiveCardsArray().get(0);
        comObj[1] = pc.createObjectiveCardsArray().get(1);
        Hand hand = new Hand();
        hand.setSecretObj(pc.createObjectiveCardsArray().get(1));
        p1.setHand(hand);
        p2.setHand(hand);
        p3.setHand(hand);
        p4.setHand(hand);
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
}