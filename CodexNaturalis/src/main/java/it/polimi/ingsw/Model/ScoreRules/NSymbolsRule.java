package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.PlayerGround;
import it.polimi.ingsw.Model.Resource;

public class NSymbolsRule implements ScoreRule {

    private final Resource symbol;
    private final int numberOfResources;
    private final int numberOfPointsPerResources;

    public NSymbolsRule(Resource symbol, int numberOfResources, int numberOfPointsPerResources) {
        this.symbol = symbol;
        this.numberOfResources = numberOfResources;
        this.numberOfPointsPerResources = numberOfPointsPerResources;
    }
    @Override
    public int calculatePoints(PlayerGround gameBoard) {
        return (gameBoard.getTotalResources().get(symbol) / numberOfResources) * numberOfPointsPerResources;
    }
}