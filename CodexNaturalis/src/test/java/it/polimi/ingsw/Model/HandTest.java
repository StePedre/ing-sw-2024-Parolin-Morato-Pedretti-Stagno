package it.polimi.ingsw.Model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {
    @Test
    void drawCard() {
        Hand hand = new Hand();
        Deck deck = DeckTest.deckCreation();
        for(int i = 0; i<deck.getNumberOfCards(); i++){
            deck.drawCard(0);
        }
        assertFalse(hand.drawCard(deck));
    }

    @Test
    void chooseCard() {
    }

    @Test
    void selectObj() {
    }
}