package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.PlayerGround;
import it.polimi.ingsw.Model.Position;
import it.polimi.ingsw.Model.Resource;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class CompositionRule deals with calculating points from a specific type of ObjectiveCard at the end of the game.
 * This type of rule is applied when the objective is to place three cards in a specific composition
 * (e.g. 3 cards placed diagonally). The composition is described by the offset and the color of each card.
 * There is a number of points per composition, which means there may be more than one composition.
 */

public class CompositionRule implements ScoreRule, Serializable {
    private static final long serialVersionUID = 13L;
    private final String name = "CR";
    private final Position[] offSets;
    private final Resource[] colors;
    private final int numberOfPointsPerComposition;

    /**
     * Class constructor.
     *
     * @param offSets are the offsets of coordinates x and y of the second and the third card of a composition.
     * @param colors are the colors of the three cards.
     * @param numberOfPointsPerComposition is the score assigned to player for each composition.
     */
    public CompositionRule(Position[] offSets, Resource[] colors, int numberOfPointsPerComposition){
        this.offSets = offSets;
        this.colors = colors;
        this.numberOfPointsPerComposition = numberOfPointsPerComposition;
    }

    /**
     * This method only multiplies the number of compositions of the same type (described by offsets and colours)
     * by the points given by that composition. To find out how many compositions of the same type are there,
     * a method from class PlayerGround is called.
     *
     * @param gameBoard is the instance of the player's play ground, where compositions are looked for.
     * @return the total score from that objective.
     */
    @Override
    public int calculatePoints(PlayerGround gameBoard) {

        return gameBoard.calculateNumberOfCompositions(offSets, colors) * numberOfPointsPerComposition;
    }

    /**
     * This method returns the string of the colors of the cards of the composition in this format
     * "colorCard1, colorCard2, colorCard3".
     *
     * @return string of colors.
     */
    public String getColors(){
        String compositionColors = "";
        for(Resource color: colors){
            compositionColors = compositionColors.concat(", ").concat(color.name());
        }
        return compositionColors;
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
     * This method returns the coordinates of the second and third card of the composition.
     *
     * @return array of 4 coordinates, 2 per card.
     */
    public int[] getOffsets(){
        int[] offsets = new int[4];
        int i = 0;
        for(Position pos: offSets){
            offsets[i] = pos.getX();
            offsets[i+1] = pos.getY();
            i = i+2;
        }
        return offsets;
    }

    /**
     * This method returns the points given for each composition placed on the ground.
     *
     * @return card points.
     */
    public int getPoints() {
        return this.numberOfPointsPerComposition;
    }

    /**
     * return the colors of the resources
     *
     * @return an array of resources
     */
    public Resource[] getResources(){
        return colors;
    }

}