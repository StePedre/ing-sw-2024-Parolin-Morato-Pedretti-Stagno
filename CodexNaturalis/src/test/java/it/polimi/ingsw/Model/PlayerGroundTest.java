package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.ScoreRules.FlatRule;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PlayerGroundTest {

    PlayerGround pg = new PlayerGround();
    Corner[] corners = new Corner[4];
    Corner[] backCorners = new Corner[4];
    Resource color = Resource.FOX;
    Resource color0 = Resource.BLANK;
    ArrayList<Resource> backres = new ArrayList<>();
    HashMap<Resource, Integer> req = new HashMap<>();
    FlatRule rule = new FlatRule(1);
    FlatRule rule0 = new FlatRule(0);
    PlayableCard card1 = new PlayableCard(1, rule, corners, backCorners, color, req);
    @Test
    void placeCard() throws InvalidPositionException {  //tests lots of methods
        corners[0] = new Corner("TLF", Resource.LEAF, true);
        corners[1] = new Corner("TRF", Resource.LEAF, true);
        corners[2] = new Corner("BLF", Resource.BLANK, true);
        corners[3] = new Corner("BRF", Resource.NOTVISIBLE, false);
        backCorners[0] = new Corner("TLB", Resource.BLANK, true);
        backCorners[1] = new Corner("TRB", Resource.BLANK, true);
        backCorners[2] = new Corner("BLB", Resource.BLANK, true);
        backCorners[3] = new Corner("BRB", Resource.BLANK, true);
        /* PLACING STARTER CARD TEST*/
        StarterCard card0 = new StarterCard(0, rule0, corners, backCorners, color0, backres);
        Position centralPos = new Position(42, 42);
        pg.getAvailablePositions().add(centralPos);
        card0.getBackRes().add(Resource.FOX);
        card0.flipCard();
        pg.placeCard(card0, centralPos);
        for(Position p: pg.getAvailablePositions()){
            System.out.println(p.getX() + ", " + p.getY());
        }
        System.out.println(pg.getTotalResources().get(Resource.FOX));
        /* CARD NON STARTER TO BE TESTED (TO DO)
        Position posValid = new Position(43, 44);
        card1.getRequirements().put(Resource.LEAF, 3); */

    }

    @Test
    void raiseScore() {

    }

    @Test
    void calculateNumberOfCoveredCorners() {
    }

    @Test
    void calculateNumberOfCompositions() {
    }
}