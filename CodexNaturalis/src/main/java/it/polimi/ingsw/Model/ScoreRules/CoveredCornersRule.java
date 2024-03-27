package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.PlayerGround;
import it.polimi.ingsw.Model.Position;
import it.polimi.ingsw.Model.Resource;

public class CoveredCornersRule implements ScoreRule {
    @Override
    public int calculatePoints(PlayerGround gameBoard) {
        return gameBoard.calculateNumberOfCoveredCorners()*2;
    }

}