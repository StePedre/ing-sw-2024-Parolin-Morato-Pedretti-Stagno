package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.PlayerGround;

import java.io.Serializable;

/**
 * Class FlatRule is used to calculate the points from a PlayableCard that always gives the same amount of points
 * when it's placed on the ground. It may be 0.
 */
public class FlatRule implements ScoreRule, Serializable {
    private static final long serialVersionUID = 15L;
    private final String name = "FR";
    private final int flatPoints;

    /**
     * Class constructor.
     *
     * @param flatPoints is the number of points always given by placing the card.
     */
    public FlatRule(int flatPoints){
        this.flatPoints = flatPoints;
    }

    /**
     * This method only returns the number of points that are always given by placing the card on the ground.
     *
     * @param gameBoard is the instance of the player's play ground, where the card has been placed in.
     * @return the points given by the card.
     */
    @Override
    public int calculatePoints(PlayerGround gameBoard) {
        return flatPoints;
    }

    /**
     * This method returns the name (initials) of the rule.
     *
     * @return class name (in short).
     */
    public String getName(){
        return this.name;
    }

    /**
     * This method returns the points always given by the card when placed.
     *
     * @return card points.
     */
    public int getPoints() {
        return this.flatPoints;
    }

}