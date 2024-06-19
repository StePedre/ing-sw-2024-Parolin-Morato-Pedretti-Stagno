package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.ParsingController;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {
    ParsingController pc = new ParsingController();
    ArrayList<PlayableCard> cards = pc.parsingPlayableCards();
    PlayableCard[] cardsHand = {cards.get(5), cards.get(23), cards.get(47)};

    HandTest() throws IOException, ParseException {
    }

    @Test
    void getAndRemoveCard() {
        Hand hand = new Hand(cardsHand);
        assertEquals(hand.getCard(1), cards.get(23));
        hand.removeCard(hand.getCard(1));
        assertNull(hand.getCard(1));
    }

    @Test
    void chooseCard() throws IOException, ParseException {
        Deck deck = pc.createResDeck();
        Hand hand = new Hand(cardsHand);
        hand.removeCard(hand.getCard(1));
        PlayableCard card = deck.drawCard(5);
        if(hand.drawCard(deck)){
            hand.chooseCard(card);
        }
        assertEquals(hand.getCard(1), card);
    }
}