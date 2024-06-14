package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * PlayableCard is a specification of class Card, so it inherits all
 * its methods and attributes. Moreover, an object from PlayableCard
 * may have some requirements in order to be played: if there are any,
 * they are of type Resource.
 * One method is added in this extending class: the clone method.
 */
public class PlayableCard extends Card implements Serializable {
    private static final long serialVersionUID = 6L;
    private HashMap<Resource, Integer> requirements;



    /**
     * Class constructor. All parameters except the last one are described in Card class
     * and set using the super class constructor.
     *
     * @param req describes number and type of each requirement needed to play the card.
     *            May be zero.
     */
    public PlayableCard(int id, ScoreRule rule, Corner[] corners, Corner[] backCorners, Resource color, HashMap<Resource, Integer> req) {
        super(id, rule, corners, backCorners, color);
        this.requirements=req;
    }

    /**
     * The method clones the current card with all its attributes' value.
     *
     * @return an object of type PlayableCard which is exactly like the caller.
     */
    public PlayableCard clone(){
        PlayableCard card = new PlayableCard(this.getId(),this.getRule(),this.getCorners(),this.getBackCorners(), this.getColor(), this.requirements);
        return card;
    }

    /**
     * The method gets the full set of resources and the amount of each one of them.
     *
     * @return hash map of each resource and its amount.
     */
    public HashMap<Resource, Integer> getRequirements() {
        return requirements;
    }

}
