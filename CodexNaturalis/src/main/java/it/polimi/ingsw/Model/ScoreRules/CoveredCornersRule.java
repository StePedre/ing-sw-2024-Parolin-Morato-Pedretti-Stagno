package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.PlayerGround;
import it.polimi.ingsw.Model.Position;
import it.polimi.ingsw.Model.Resource;

/**
 * Class CoveredCornersRule deals with calculating points from a specific type of PlayableCard when it gets placed.
 * This type of rule is applied when the card gives points for each corner that is covered by other cards.
 */
public class CoveredCornersRule implements ScoreRule {

    /**
     * This method only multiplies the number of covered corners of the card by 2 (this is always the number of points).
     * To find out how many covered corners are there, a method from class PlayerGround is called.
     *
     * @param gameBoard is the instance of the player's play ground, where the card is looked for.
     * @return the total score from the rule.
     */
    @Override
    public int calculatePoints(PlayerGround gameBoard) {
        return gameBoard.calculateNumberOfCoveredCorners()*2;
    }

}