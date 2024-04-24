package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;

/**
 * The Card class is an abstraction that has two specifications: PlayableCard and StarterCard.
 * Each object of class Card has: numeric id, scoring rule, main resource, four front corners
 * and four back corners. It can be positioned with the front visible, or flipped.
 */
public abstract class Card {
    private int id;
    private ScoreRule rule;
    private Resource color;
    private Corner[] corners;
    private Corner[] backCorners;
    private boolean isFlipped;

    /**
     * Class constructor
     *
     * @param id identifying number of the card
     * @param rule points gained by playing this card
     * @param corners four corners placed in the front
     * @param backCorners fours corners placed in the back
     * @param color main resource of the card. Each resource matches one colour
     */
    public Card(int id, ScoreRule rule, Corner[] corners, Corner[] backCorners, Resource color){
        this.id=id;
        this.rule=rule;
        this.isFlipped=false;
        this.corners=corners;
        this.backCorners=backCorners;
        this.color = color;
    }

    /**
     * The method gets the id of a card, so it returns an object of class int.
     *
     * @return card id
     */
    public int getId() {
        return id;
    }

    /**
     * The method gets the four corners from the back of the card.
     *
     * @return all back corners
     */
    public Corner[] getBackCorners() {
        return backCorners;
    }

    /**
     * The method gets the four corners from the front of the card.
     *
     * @return all front corners
     */
    public Corner[] getCorners() {
        return corners;
    }

    /**
     * The method gets to know whether the card is flipped or not.
     *
     * @return true if the card is flipped, otherwise returns false.
     */
    public boolean getFlip() {
        return isFlipped;
    }

    /**
     * The method gets the type and number of points carried by the card.
     *
     * @return the scoring rule of the card.
     */
    public ScoreRule getRule() {
        return rule;
    }

    /**
     * The method gets the color of the card, which matches a specific resource.
     *
     * @return the main resource of the card
     */
    public Resource getColor(){
        return color;
    }

    /**
     * The method flips the card by changing the value of attribute isFlipped
     * from true to false and vice versa.
     */
    public void flipCard(){
        isFlipped=!isFlipped;
    }

    /**
     * The method compares if two cards, the one that called the method and
     * the one set as a parameter, are equals or not.
     *
     * @param  card is the card to be compared
     * @return true or false depending on whether two cards are equal or not
     */
    public boolean equals(Card card){
        return this.id==card.id;
    }
}
