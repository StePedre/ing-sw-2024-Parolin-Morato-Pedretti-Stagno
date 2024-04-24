package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;

import java.util.ArrayList;

/**
 * StarterCard is a specification of class Card, so it inherits all
 * its methods and attributes. Moreover, an object from StarterCard
 * has one or more back Resource(s).
 * One method is added in this extending class: the clone method.
 */
public class StarterCard extends Card{
    private ArrayList<Resource> backRes;

    /**
     * Class constructor. All parameters except the last one are described in Card class
     * and set using the super class constructor.
     *
     * @param backRes is the list of resources on the back of the card.
     */
    public StarterCard(int id, ScoreRule rule, Corner[] corners, Corner[] backCorners, Resource color, ArrayList<Resource> backRes) {
        super(id, rule, corners, backCorners,color);
        this.backRes=backRes;
    }

    /**
     * The method gets the list of resources displayed on the back of the card.
     *
     * @return a list of resources that appears on the back of the card.
     *         May be just one.
     */
    public ArrayList<Resource> getBackRes() {
        return backRes;
    }

    /**
     * The method clones the current card with all its attributes' value.
     *
     * @return an object of type PlayableCard which is exactly like the caller.
     */
    public StarterCard clone(){
        return new StarterCard(this.getId(),this.getRule(),this.getCorners(),this.getBackCorners(),this.getColor(),this.backRes);
    }


}
