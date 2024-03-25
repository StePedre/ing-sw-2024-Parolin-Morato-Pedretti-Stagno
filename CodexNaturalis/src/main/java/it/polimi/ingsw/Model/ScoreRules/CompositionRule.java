package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.PlayerGround;
import it.polimi.ingsw.Model.Color;

public class CompositionRule implements ScoreRule {

    private final int[] offSets;
    private final Color[] colors;
    private final int numberOfPointsPerComposition;

    public CompositionRule(int[] offSets, Color[] colors, int numberOfPointsPerComposition){
        this.offSets = offSets;
        this.colors = colors;
        this.numberOfPointsPerComposition = numberOfPointsPerComposition;
    }
    @Override
    public int calculatePoints(PlayerGround gameBoard) {
        //TO DO
        return 0;
    }
}