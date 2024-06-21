package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.ParsingController;
import it.polimi.ingsw.Controller.PlayerController;
import it.polimi.ingsw.Controller.RoundController;
import it.polimi.ingsw.Model.Deck;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.Player;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 *  Room class represents a single game space where 2 to 4 players can enter. Their turns
 *  are managed by a RoundController.
 *  This class implements Serializable interface.
 */

public class Room implements Serializable {
    private final String nameRoom;
    private final Game game;
    private final RoundController roundController;
    private final PlayerController playerController;
    private int playerInRoom;

    /**
     * Class constructor.
     *
     * @param nameRoom is the name to the newborn Room.
     */
    public Room(String nameRoom) {
        this.nameRoom = nameRoom;
        game = new Game();
        roundController = new RoundController();
        playerController = new PlayerController();
        parse(game);
        playerInRoom = 0;
    }

    /**
     * This method increases the number of players in the room.
     */
    public void addPlayerInRoom() {
        this.playerInRoom++;
    }

    /**
     * The method gets the game which is played inside this room instance.
     *
     * @return game associated to the room.
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * The method gets the name given to the room.
     *
     * @return room's name.
     */
    public String getName() {
        return nameRoom;
    }

    /**
     * This method gets the player controller associated to a player in the room.
     *
     * @return such player controller.
     */
    public PlayerController getPlayerController() {
        return playerController;
    }

    /**
     * This method gets the number of players in the room.
     *
     * @return such Integer.
     */
    public int getPlayerInRoom() {
        return playerInRoom;
    }

    /**
     * The method gets the room instance itself.
     *
     * @return this instance.
     */
    public Room getRoom(){
        return this;
    }

    /**
     * The method gets the round controller associated with the game played in the room.
     *
     * @return round controller of the room.
     */
    public RoundController getRoundController() {
        return roundController;
    }

    /**
     * This method tells if the room is full or not.
     * It is synchronized.
     *
     * @return such boolean.
     */
    public synchronized boolean isFull() {
        return game.getExpPlayers() == playerInRoom;
    }

    /**
     * The method initializes each field of the Game associated to the Room.
     *
     * @param game to be initialized.
     */
    public void parse(Game game) {
        ParsingController parse = new ParsingController();
        try {
            game.setStarterCards(parse.createStarterCardsArray());
            ArrayList<ObjectiveCard> list = parse.createObjectiveCardsArray();
            Random rand = new Random();
            ObjectiveCard[] objs = {list.remove(rand.nextInt(list.size())),list.remove(rand.nextInt(list.size()))};
            game.setCommonObj(objs);
            game.setOtherObjs(list);
            //parsing deck
            Deck[] decks = {parse.createResDeck(),parse.createGoldDeck()};
            decks[0].shuffle();
            decks[1].shuffle();
            game.setDecks(decks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method decreases the number of players in the room.
     */
    public void removePlayerInRoom() {
        this.playerInRoom--;
    }

}
