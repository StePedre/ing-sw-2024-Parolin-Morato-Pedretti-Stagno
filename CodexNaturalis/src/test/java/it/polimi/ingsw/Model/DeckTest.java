package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.ScoreRules.FlatRule;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    static Deck deckCreation(){
        PlayableCard card1 = new PlayableCard(1, new FlatRule(0), new Corner[4], new Corner[4], Resource.FOX, new HashMap<>());
        PlayableCard card2 = new PlayableCard(2, new FlatRule(0), new Corner[4], new Corner[4], Resource.FOX, new HashMap<>());
        PlayableCard card3 = new PlayableCard(3, new FlatRule(0), new Corner[4], new Corner[4], Resource.FOX, new HashMap<>());
        PlayableCard card4 = new PlayableCard(4, new FlatRule(0), new Corner[4], new Corner[4], Resource.FOX, new HashMap<>());
        PlayableCard card5 = new PlayableCard(5, new FlatRule(0), new Corner[4], new Corner[4], Resource.FOX, new HashMap<>());
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);

        return new Deck(5, "ResourceDeck", cards);
    }


    @Test
    void drawCard() {
        Deck deck = deckCreation();
        int expectedID = deck.getCards().get(1).getId();  // necessary to save because drawcard also removes card
        Card drawn = deck.drawCard(1);
        assertEquals(drawn.getId(), expectedID);
    }
}