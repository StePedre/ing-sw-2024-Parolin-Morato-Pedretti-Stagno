package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.ParsingController;
import it.polimi.ingsw.Model.ScoreRules.CompositionRule;
import it.polimi.ingsw.Model.ScoreRules.FlatRule;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;
import it.polimi.ingsw.View.TUI;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PlayerGroundTest {
    Game game = new Game();
    Player player = new Player("Silvia");

    ParsingController pc = new ParsingController();

    PlayerGround pg = new PlayerGround();
    StarterCard card0 = pc.createStarterCardsArray().getFirst();
    PlayableCard card1 = pc.parsingPlayableCards().getFirst();
    ObjectiveCard card2 = pc.createObjectiveCardsArray().getFirst();
    ObjectiveCard card3 = pc.createObjectiveCardsArray().get(2);

    PlayerGroundTest() throws IOException, ParseException {
    }

    @Test
    void placeAndAddCard() throws InvalidPositionException, MissingResourcesException {
        Position centralPos = new Position(42, 42);
        card0.getBackRes().add(Resource.FOX);
        card0.flipCard();
        pg.placeCard(card0, centralPos);
        player.setPlayerGround(pg);
        game.getPlayers().add(player);
        game.setCommonObj(new ObjectiveCard[]{card2, card3});
        Position pos = new Position(41, 43);
        pg.placeCard(card1, pos);
        TUI tui = new TUI();
        tui.showGround(game, player);
    }

    /* Through visualization in the textual interface, the following method tests
    all the private methods inside addCard(), which is private as well and invoked by placeCard();
     */
    @Test
    void addCard() throws MissingResourcesException, InvalidPositionException {
        pg.placeCard(card0, new Position(42, 42));
        game.setCommonObj(new ObjectiveCard[]{card2, card3});
        for(Position pos: player.getPlayerGround().getAvailablePositions()){
            player.getPlayerGround().placeCard(card1, pos);
            break;
        }
        System.out.println(card1.getRule().getName());
        assertEquals(player.getPlayerGround().getPlayerScore(), card1.getRule().getPoints());
        for(Corner c: card1.getCorners()){
            System.out.println(c.getPos() + " " + c.getAvailability() + " " + c.getCornerRes());
        }
        System.out.println(card1.getRequirements());
        TUI tui = new TUI();
        tui.showGround(game, player);
    }

    @Test
    void checkRequirements() throws IOException, ParseException {
        assertFalse(pg.checkRequirements(pc.createGoldDeck().drawCard(7)));
        assertTrue(pg.checkRequirements(pc.createResDeck().drawCard(3)));
    }

    @Test
    void calculateNumberOfCoveredCorners() {

    }

    @Test
    void calculateNumberOfCompositions() throws IOException, ParseException, MissingResourcesException, InvalidPositionException {
    }
}