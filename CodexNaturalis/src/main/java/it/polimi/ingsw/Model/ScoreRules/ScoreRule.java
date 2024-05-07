package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.PlayerGround;

import java.io.Serializable;

/**
 * ScoreRule interface includes one method from ScoreRule package that may be invoked.
 * The method requires the instance of the play ground, and its behaviour depends on the type of ScoreRule.
 * For further information, see each ScoreRule class.
 */
public interface ScoreRule {
    int calculatePoints(PlayerGround gameBoard);

    String getName();

    int getPoints();
}