package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.PlayerGround;
import it.polimi.ingsw.Model.Resource;

import java.io.Serializable;

/**
 * Class NSymbolsRule is used to calculate points given by the presence on the player's ground of a specific resource.
 * This can be required both by an ObjectiveCard or by a PlayableCard. The number of points for each resource may vary,
 * as well as the number of times the same resource needs to appear and, of course, the resource itself.
 */
public class NSymbolsRule implements ScoreRule, Serializable {

    private final String name = "NSR";
    private final Resource symbol;
    private final int numberOfResources;
    private final int numberOfPointsPerResources;

    /**
     * Class constructor.
     *
     * @param symbol is the resource to look for to get the points.
     * @param numberOfResources is the number of resources required to be on the ground to get the points.
     * @param numberOfPointsPerResources are the points given each time the required amount of resources is found.
     */
    public NSymbolsRule(Resource symbol, int numberOfResources, int numberOfPointsPerResources) {
        this.symbol = symbol;
        this.numberOfResources = numberOfResources;
        this.numberOfPointsPerResources = numberOfPointsPerResources;
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
     * This method returns the required symbol.
     *
     * @return rule symbol.
     */
    public Resource getSymbol(){
        return this.symbol;
    }

    /**
     * This method returns the points given when rule is respected.
     *
     * @return card points.
     */
    public int getPoints() {
        return this.numberOfPointsPerResources;
    }

    /**
     * This method gets the total number of time a resource of the given type is present on the ground.
     * It calculates then how many times the group of X resources appears, where X stands for numberOfResources.
     * Finally, it multiplies the obtained result for the number of points given each time the group appears.
     *
     * @param gameBoard is the instance of the player's play ground, where required resource is looked for.
     * @return the total score from the rule.
     */
    @Override
    public int calculatePoints(PlayerGround gameBoard) {
        return (gameBoard.getTotalResources().get(symbol) / numberOfResources) * numberOfPointsPerResources;
    }
}