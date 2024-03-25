package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.PlayerGround;
import it.polimi.ingsw.Model.Resource;

public class OneOfEachRule implements ScoreRule {
    @Override
    public int calculatePoints(PlayerGround gameBoard) {

        HashMap<Resource, Integer> resources = gameBoard.getTotalResource();

        return Math.min(Math.min(resources.get(Resource.PIUMA), resources.get(Resource.POZIONE)), resources.get(Resource.PERGAMENA)) * 3;
    }
}