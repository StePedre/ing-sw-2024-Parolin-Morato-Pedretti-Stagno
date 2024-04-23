package it.polimi.ingsw.Model;

import java.util.Arrays;

public class Hand {
    private Card[] cards;
    private ObjectiveCard secretObj;
    private static final int maxNumberOfCards = 3;
    // used as max size of array of cards in hand

    // constructor with no parameters
    // secretObj is initialized with the default values
    public Hand() {
        this.cards = new Card[maxNumberOfCards];
        this.secretObj = new ObjectiveCard(0, null);
    }

    // constructor with cards
    // cycle for is used in order to prevent a mutable hand from the outside
    // (it creates a new array of Card, and it copies the values of the array passed as a parameter)
    // secretObj is initialized with the default values
    public Hand(Card[] cards) {
        this.cards = new Card[maxNumberOfCards];
        this.cards= Arrays.copyOf(cards,maxNumberOfCards);
        this.secretObj = new ObjectiveCard(0, null);
    }

    // setter
    public void setSecretObj(ObjectiveCard secretObj) {
        this.secretObj = secretObj;
    }

    // getter
    public Card getCard(int position) {
        return this.cards[position];
    }

    public ObjectiveCard getObjCard() {
        return this.secretObj;
    }

    // given it is possible to draw from a deck (the check is made by the controller), the card passed by
    // parameter is the card that needs to be added to the current Hand, which will be an array of 2
    // elements
    // First cycle find the position i of the card previously played (cards[i] is null)
    // Then the new card is added to the position i
    // This will guarantee a Hand with the correct number of cards
    public void chooseCard(Card card) {
        int nullIndex = 0;
        for (int i = 0; i < maxNumberOfCards; i++) {
            if (this.cards[i] == null)
                nullIndex = i;
        }
        cards[nullIndex] = card;
    }

    // choose the secret objective card between two different cards
    // used only once for each player at the beginning of the game
    public void selectObj(ObjectiveCard objective) {
        this.secretObj = objective;
    }

    // returns true if it is possible to draw from the deck passed by parameter
    // check if that's not empty with a boolean
    public boolean drawCard(Deck deck) {
        return deck.getNumberOfCards() > 0;
    }
}
