package it.polimi.ingsw.Model.ScoreRules;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import it.polimi.ingsw.Model.PlayerGround;
import it.polimi.ingsw.Model.Resource;

/**
 * Class OneOfEachRule deals with calculating points from a specific type of ObjectiveCard at the end of the game.
 * This type of rule is applied when the objective is to have a set (or more than one set) of three resources (plume,
 * potion and scroll). It gives three points for each said set.
 */
public class OneOfEachRule implements ScoreRule, Serializable {
    @Serial
    private static final long serialVersionUID = 17L;
    private final String name = "OER";

    /**
     * This method finds the minimal number between the total amount of plume appearances, potion appearances and
     * scroll appearances. This gives the number of sets where all three are visible, and it's finally multiplied by 3.
     *
     * @param gameBoard is the instance of the player's play ground, where sets are looked for.
     * @return the total score from that objective.
     */
    @Override
    public int calculatePoints(PlayerGround gameBoard) {

        HashMap<Resource, Integer> resources = gameBoard.getTotalResources();

        return Math.min(Math.min(resources.get(Resource.PLUME), resources.get(Resource.POTION)), resources.get(Resource.SCROLL)) * 3;
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
     * This method returns the points always given when the rule is respected.
     *
     * @return card points.
     */
    public int getPoints() {
        return 3;
    }

}