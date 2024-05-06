package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.ParsingController;
import it.polimi.ingsw.Controller.RoundController;
import it.polimi.ingsw.Model.Deck;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.ObjectiveCard;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Room implements Serializable {
    private final String nameRoom;
    private Game game = null;
    private RoundController roundController = null;
    public Room(String nameRoom) {
        this.nameRoom = nameRoom;
        game = new Game();
        roundController = new RoundController();
        /*parse(game);
        //creare deck
        Deck[] decks = new Deck[2];
        game.setDecks(decks);
        //seleziononare carta obj comune
        ObjectiveCard[] obj = new ObjectiveCard[2];
        game.setCommonObj(obj);*/
    }
    public Game getGame() {
        return this.game;
    }
    public String getName() {
        return nameRoom;
    }
    public Room getRoom(){
        return this;
    }
    public RoundController getRoundController() {
        return roundController;
    }
    public void parse(Game game) {
        ParsingController pc = new ParsingController();
        try {
            game.setStarterCards(pc.createStarterCardsArray());
            ArrayList<ObjectiveCard> list = pc.createObjectiveCardsArray();
            //altro parsing, chiedi a stefano
            Random rand = new Random();
            ObjectiveCard[] objs = {list.remove(rand.nextInt(list.size())),list.remove(rand.nextInt(list.size()))};
            game.setCommonObj(objs);
            game.setOtherObjs(list);
            //parsing deck
            Deck[] decks = {pc.createResDeck(),pc.createGoldDeck()};
            game.setDecks(decks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
