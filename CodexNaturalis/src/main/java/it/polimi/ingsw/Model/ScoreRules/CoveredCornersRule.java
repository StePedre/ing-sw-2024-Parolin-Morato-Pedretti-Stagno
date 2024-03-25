package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.PlayerGround;

public class CoveredCornersRule implements ScoreRule {
    @Override
    public int calculatePoints(PlayerGround gameBoard) {
        return gameBoard.getNumberOfLastCoveredCorners()*2;
    }
}