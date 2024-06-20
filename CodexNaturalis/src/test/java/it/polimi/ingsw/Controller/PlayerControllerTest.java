package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.View.TUI;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {

    Player player = new Player("silvia");
    Game game = new Game();
    ParsingController pc = new ParsingController();
    PlayerController playerc = new PlayerController();

    @Test
    void populateHandAndGetSecretCommonObj() throws IOException, ParseException {
        game.setDecks(new Deck[]{pc.createResDeck(), pc.createGoldDeck()});
        playerc.populateHand(game, player);
        playerc.setObjSecret(pc.createObjectiveCardsArray().get(7), player.getHand());
        TUI tui = new TUI();
        game.setOtherObjs(pc.createObjectiveCardsArray());
        game.setCommonObj(playerc.pickObjCard(game));
        tui.showHand(game, player);
    }
    @Test
    void setFirstCard() throws IOException, ParseException, InvalidPositionException {
        game.setStarterCards(pc.createStarterCardsArray());
        StarterCard starter = game.getOneStarterCard();
        playerc.setFirstCard(starter, player.getPlayerGround());
        assertEquals(player.getPlayerGround().getGround()[42][42], starter);
    }

}