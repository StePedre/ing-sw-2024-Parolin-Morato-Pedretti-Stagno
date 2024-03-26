package it.polimi.ingsw.Model;

public class Hand {
    private Card cards[];
    private ObjCard secretObj;
    // used as max size of array of cards in hand
    private static final maxNumberOfCards = 3;

    // constructor with no parameters
    public Hand() {
        this.cards = new Card[maxNumberOfCards];
        this.secretObj = new ObjCard;
    }

    // constructor with cards
    // cycle for is used in order to prevent a mutable hand from the outside
    // (it creates a new array of Card and it copies the values of the array passed as a parameter)
    public Hand(Card cards[]) {
        this.cards = new Card[maxNumberOfCards];
        for (int i = 0; i < maxNumberOfCards; i++) {
            this.cards[i] = cards[i];
        }
        this.secretObj = new ObjCard;
    }

    // setter
    public void setSecretObj(ObjCard secretObj) {
        this.secretObj = secretObj;
    }

    // getter
    public Card getCard(int position) {
        return this.cards[position];
    }

    public ObjCard getObjCard() {
        return this.secretObj;
    }

    // choose which card is going to be played
    // this card will be removed from the hand
    // hand will be a temporary array of two elements
    public void chooseCard(Card card) {

    }

    // choose the secret objective card between two different cards
    // used only once for each player at the beginning of the game
    public void selectObj(ObjCard objective) {

    }

    // returns true if, after a card is being placed, the player decides to draw from a deck
    // (don't care at this point if the player will draw from the resources deck or the gold one)
    // returns false if, after a card is being placed, the player decides to pick a card between the four already revealed next to the decks
    // (don't care at this point if the player will pick a resourse or a gold car)
    public boolean drawCard(Deck deck) {

    }
}
