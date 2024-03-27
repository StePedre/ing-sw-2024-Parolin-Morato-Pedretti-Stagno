package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.PlayerGround;
import it.polimi.ingsw.Model.Position;
import it.polimi.ingsw.Model.Resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CompositionRule implements ScoreRule {

    private final Position[] offSets;
    private final Resource[] colors;
    private final int numberOfPointsPerComposition;

    public CompositionRule(Position[] offSets, Resource[] colors, int numberOfPointsPerComposition){
        this.offSets = offSets;
        this.colors = colors;
        this.numberOfPointsPerComposition = numberOfPointsPerComposition;
    }
    @Override
    public int calculatePoints(PlayerGround gameBoard) {

        return gameBoard.calculateNumberOfCompositions(offSets, colors) * numberOfPointsPerComposition;
    }

}