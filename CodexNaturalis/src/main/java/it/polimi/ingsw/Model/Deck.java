package it.polimi.ingsw.Model;
import java.util.Collections;

public class Deck{
    private int numberOfCards;
    private final String kindOfDeck;
    private ArrayList<Card> cards;

    public Deck(int numberOfCards, String kindOfDeck, ArrayList<Card> cards) {
        this.numberOfCards = numberOfCards;
        this.kindOfDeck = kindOfDeck;
        this.cards = cards;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard(int position) {
        if (position >= 0 && position < cards.size()) {
            return cards.remove(position);
        } else {
            throw new IndexOutOfBoundsException("Invalid position: " + position);
        }
    }

    // TO DO.

}