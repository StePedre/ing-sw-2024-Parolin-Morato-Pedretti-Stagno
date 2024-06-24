package it.polimi.ingsw.Model;

import it.polimi.ingsw.Controller.ParsingController;
import it.polimi.ingsw.Model.ScoreRules.CompositionRule;
import it.polimi.ingsw.Model.ScoreRules.CoveredCornersRule;
import it.polimi.ingsw.Model.ScoreRules.FlatRule;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;
import it.polimi.ingsw.View.TUI;
import it.polimi.ingsw.View.TUIGraphicGenerator;
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
        TUIGraphicGenerator.printGround(player.getPlayerGround());
    }

    /* Through visualization in the textual interface, the following method tests
    all the private methods inside addCard(), which is private as well and invoked by placeCard();
     */
    @Test
    void addCard() throws MissingResourcesException, InvalidPositionException, IOException, ParseException {
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
        game.setDecks(new Deck[]{pc.createResDeck(), pc.createGoldDeck()});
        TUI tui = new TUI();
        tui.yourTurnPlay(game, player);
    }

    @Test
    void checkRequirements() throws IOException, ParseException {
        assertFalse(pg.checkRequirements(pc.createGoldDeck().drawCard(7)));
        assertTrue(pg.checkRequirements(pc.createResDeck().drawCard(3)));
    }

    @Test
    void calculateNumberOfCoveredCorners() throws MissingResourcesException, InvalidPositionException {
        Corner[] frontCorners = {new Corner("TLF", Resource.BUG, true),new Corner("BLF", Resource.MUSHROOM, true),new Corner("TRF", Resource.MUSHROOM, true),new Corner("BRF", Resource.MUSHROOM, true)};
        PlayableCard card = new PlayableCard(0, new FlatRule(0), frontCorners, new Corner[4], Resource.FOX, null);
        PlayableCard card2 = new PlayableCard(0, new CoveredCornersRule(), frontCorners, new Corner[4], Resource.FOX, null);
        Player p = new Player("silvia");
        p.getPlayerGround().getAvailablePositions().add(new Position(42, 44));
        p.getPlayerGround().getAvailablePositions().add(new Position(44, 42));
        p.getPlayerGround().getAvailablePositions().add(new Position(43, 43));
        p.getPlayerGround().placeCard(card, new Position(42, 42));
        p.getPlayerGround().placeCard(card, new Position(44, 42));
        p.getPlayerGround().placeCard(card, new Position(42, 44));
        p.getPlayerGround().placeCard(card2, new Position(43, 43));
        assertEquals(p.getPlayerGround().calculateNumberOfCoveredCorners(), 3);
    }

    @Test
    void calculateNumberOfCompositions() throws IOException, ParseException, MissingResourcesException, InvalidPositionException {
        Corner[] frontCorners = {new Corner("TLF", Resource.BUG, true),new Corner("BLF", Resource.MUSHROOM, true),new Corner("TRF", Resource.MUSHROOM, true),new Corner("BRF", Resource.MUSHROOM, true)};
        PlayableCard card = new PlayableCard(0, new FlatRule(0), frontCorners, new Corner[4], Resource.FOX, null);
        Player p = new Player("silvia");
        p.getPlayerGround().getAvailablePositions().add(new Position(43, 43));
        p.getPlayerGround().getAvailablePositions().add(new Position(44, 44));
        p.getPlayerGround().getAvailablePositions().add(new Position(37, 37));
        p.getPlayerGround().getAvailablePositions().add(new Position(38, 38));
        p.getPlayerGround().getAvailablePositions().add(new Position(39, 39));
        p.getPlayerGround().placeCard(card, new Position(42, 42));
        p.getPlayerGround().placeCard(card, new Position(43, 43));
        p.getPlayerGround().placeCard(card, new Position(44, 44));
        p.getPlayerGround().placeCard(card, new Position(37, 37));
        p.getPlayerGround().placeCard(card, new Position(39, 39));
        p.getPlayerGround().placeCard(card, new Position(38, 38));
        assertEquals(p.getPlayerGround().calculateNumberOfCompositions(new Position[]{new Position(1,1), new Position(2, 2)}, new Resource[]{Resource.FOX, Resource.FOX, Resource.FOX}), 2);
    }
}