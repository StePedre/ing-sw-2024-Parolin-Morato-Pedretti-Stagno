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
    void start() throws RemoteException, NotExistingChatException {   // tests methods createglobalchat and createprivatechat as they are called inside start()
        playerss.add(p1);
        playerss.add(p2);
        playerss.add(p3);
        playerss.add(p4);
        Game game =  new Game(playerss, decks, comObj);
        game.start();
        System.out.println(game.getChat().getName());    // prints global chat name ("Global Chat") and number 4
        System.out.println(game.getChat().getPlayersNo());
        System.out.println(game.getPrivateChat(p1, p3).getName());   // prints chat between p1 and p3
        System.out.println(game.getPrivateChat(p2, p4).getName());

    }

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
        p1.getPlayerGround().setPlayerScore(23);
        p2.getPlayerGround().setPlayerScore(23);
        p3.getPlayerGround().setPlayerScore(23);
        p4.getPlayerGround().setPlayerScore(23);
        playerss.add(p1);
        playerss.add(p2);
        playerss.add(p3);
        playerss.add(p4);

        Game game =  new Game(playerss, decks, comObj);
        ArrayList<Player> winners = game.finish();
        for(Player p: winners){
            System.out.println(p.getNickname());
        }
    }

    @Test
    void isFirst() {
    }
}