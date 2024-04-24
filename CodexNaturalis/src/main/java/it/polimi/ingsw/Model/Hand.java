package it.polimi.ingsw.Model;

import java.util.Arrays;

/**
 * The Hand class implements a player's hand during a game session. Each Player is associated with one Hand.
 * One hand is made of an array of Card objects, and their maximum number is three. Moreover, each hand requires
 * a secret objective of type ObjectiveCard that won't change for the whole game session.
 * Apart from get and set methods, other operations that may be called on the class Hand are choosing one card from the
 * PlayerGround and checking if it's possible to draw a card from a deck (the deck is not empty).
 */

public class Hand {
    private Card[] cards;
    private ObjectiveCard secretObj;
    private static final int maxNumberOfCards = 3;

    /** Class constructor with no parameters.
     * The array of cards is initialized with the maximum number (3) and secretObj with the default values.
     */
    public Hand() {
        this.cards = new Card[maxNumberOfCards];
        this.secretObj = new ObjectiveCard(0, null);
    }

    /** Class constructor with parameters.
     * It creates a new array of Card object (of length maxNumberOfCard) and it copies the values of the array passed
     * as a parameter. SecretObj is initialized with the default values
     *
     * @param cards is an array of cards that must be in the hand of the player.
     */
    public Hand(Card[] cards) {
        this.cards = new Card[maxNumberOfCards];
        this.cards= Arrays.copyOf(cards,maxNumberOfCards);
        this.secretObj = new ObjectiveCard(0, null);
    }

    /**
     * The method gets from the hand the card at the position passed as a parameter.
     *
     * @param position is the position of the requested card.
     * @return the Card at the specified position.
     */
    public Card getCard(int position) {
        return this.cards[position];
    }

    /**
     * The method gets the secret objective card, which is unique for each hand.
     *
     * @return the secret ObjectiveCard
     */
    public ObjectiveCard getObjCard() {
        return this.secretObj;
    }

    /**
     * The method sets the secret objective card of the player's hand.
     *
     * @param secretObj is the secret objective card to set.
     */
    public void setSecretObj(ObjectiveCard secretObj) {
        this.secretObj = secretObj;
    }

    /**
     * The method check if a given deck is empty or not through its number of cards.
     *
     * @param deck gets checked if it's empty or not.
     * @return true if it's possible to draw from the deck passed as a parameter (so it's not empty), false otherwise.
     */
    public boolean drawCard(Deck deck) {
        return deck.getNumberOfCards() > 0;
    }

    /**
     * The method, given it is possible to draw from a deck, adds the chosen card (passed as a parameter) to the hand.
     * The method firstly finds the position i of the card previously played (cards[i] is null),
     * that should be the third position of the array. Then the new card is added to the position i, and this should
     * guarantee a Hand with the correct number of cards.
     *
     * @param card needs to be added to the hand.
     */
    public void chooseCard(Card card) {
        int nullIndex = 0;
        for (int i = 0; i < maxNumberOfCards; i++) {
            if (this.cards[i] == null)
                nullIndex = i;
        }
        cards[nullIndex] = card;
    }

    /**
     * The method choose the secret objective card to keep in the hand. The choice should be made between
     * two different cards and should be done nly once for each player at the beginning of the game.
     *
     * @param objective is the chosen ObjectiveCard.
     */
    public void selectObj(ObjectiveCard objective) {
        this.secretObj = objective;
    }
}
