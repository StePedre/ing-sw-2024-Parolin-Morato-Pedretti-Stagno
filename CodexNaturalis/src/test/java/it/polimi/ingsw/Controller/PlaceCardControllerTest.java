package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PlaceCardControllerTest {

    @Test
    void draw() throws IOException, ParseException {
        Player p = new Player("silvia");
        ParsingController pc = new ParsingController();
        Deck resdeck = pc.createResDeck();
        Deck goldeck = pc.createGoldDeck();
        PlayableCard[] hand = {resdeck.getCards().get(1), goldeck.getCards().get(1), null};
        p.setHand(new Hand(hand));
        Game g = new Game();
        g.setDecks(new Deck[] {resdeck, goldeck});
        Random r = new Random();
        int i = r.nextInt(5)+1;
        PlaceCardController.draw(i, g, p);
        if(resdeck.getCards().size()==39 || goldeck.getCards().size()==39){
        }
        else{
            System.out.println(false);
        }
    }
}