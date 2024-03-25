package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.PlayerGround;

public class FlatRule implements ScoreRule {

    private final int flatPoints;

    public FlatRule(int flatPoints){
        this.flatPoints = flatPoints;
    }
    @Override
    public int calculatePoints(PlayerGround gameBoard) {
        return flatPoints;
    }
}