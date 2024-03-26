package it.polimi.ingsw.Model.ScoreRules;
import java.util.HashMap;
import it.polimi.ingsw.Model.PlayerGround;
import it.polimi.ingsw.Model.Resource;

public class OneOfEachRule implements ScoreRule {
    @Override
    public int calculatePoints(PlayerGround gameBoard) {

        HashMap<Resource, Integer> resources = gameBoard.getTotalResources();

        return Math.min(Math.min(resources.get(Resource.PLUME), resources.get(Resource.POTION)), resources.get(Resource.SCROLL)) * 3;
    }
}