package it.polimi.ingsw.Model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class Deck consists mainly in a list of objects from class Card.
 * It also has the number of cards and the type of deck.
 * Apart from get methods, it's also provided of a method to shuffle the cards
 * and one to draw a card.
 */

public class Deck implements Serializable {
    private static final long serialVersionUID = 10L;
    private int numberOfCards;
    private final String kindOfDeck;
    private ArrayList<PlayableCard> cards;

    /**
     * Class constructor.
     *
     * @param numberOfCards is the number of cards in the deck.
     * @param kindOfDeck is the type of deck.
     * @param cards is the full list of cards the deck is made of.
     */
    public Deck(int numberOfCards, String kindOfDeck, ArrayList<PlayableCard> cards) {
        this.numberOfCards = numberOfCards;
        this.kindOfDeck = kindOfDeck;
        this.cards = cards;
    }

    /**
     * The method is used to draw a card from the deck, if the choice is made
     * within the boundaries. When a card is drawn, it is also removed from the deck.
     *
     * @param position is used to identify the card by its position in the deck:
     *                 0 is the left card face up, 1 is the right one face up and
     *                 2 is the one on top of the rest of the list, face down.
     * @return chosen card.
     * @exception IndexOutOfBoundsException if the chosen card is identified with
     * a negative number or a number bigger than current deck's size.
     */
    public PlayableCard drawCard(int position) {
        if (position >= 0 && position < cards.size()) {
            return cards.remove(position);
        } else {
            throw new IndexOutOfBoundsException("Invalid position: " + position);
        }
    }

    /**
     * The method gets the list of cards. First and second element of the list
     * should be flipped with the front visible. From the third one, cards
     * must show the back.
     *
     * @return list of object of class Card.
     */
    public ArrayList<PlayableCard> getCards() {
        return cards;
    }

    /**
     * The method gets the type of deck as a string.
     *
     * @return deck type.
     */
    public String getKindOfDeck() {
        return kindOfDeck;
    }

    /**
     * The method gets the total number of cards currently in the deck.
     *
     * @return number of cards.
     */
    public int getNumberOfCards() {
        return this.cards.size();
    }

    /**
     * The method shuffles the deck: cards are reordered randomly.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

}