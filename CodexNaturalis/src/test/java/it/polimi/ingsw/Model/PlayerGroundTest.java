package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.ScoreRules.FlatRule;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;
import it.polimi.ingsw.View.TUI;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PlayerGroundTest {
    Game game = new Game();
    Player player = new Player("Silvia");
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
    void placeCard() throws InvalidPositionException, MissingResourcesException {  //tests lots of methods
        PlayerGround pg = new PlayerGround();
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
        card0.getBackRes().add(Resource.FOX);
        card0.flipCard();
        pg.placeCard(card0, centralPos);
        player.setPlayerGround(pg);
        game.getPlayers().add(player);
        FlatRule rule = new FlatRule(1);
        ObjectiveCard[] commoObj = new ObjectiveCard[2];
        commoObj[0] = new ObjectiveCard(12, rule);
        commoObj[1] = new ObjectiveCard(13, rule);
        game.setCommonObj(commoObj);
        TUI tui = new TUI();
        tui.showGround(game, player);
        Position pos = new Position(41, 43);
        pg.placeCard(card1, pos);
        tui.showGround(game, player);

        tui.showCard(card0);
        tui.showCard(card1);

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