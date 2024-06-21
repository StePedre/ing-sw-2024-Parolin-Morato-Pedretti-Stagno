package it.polimi.ingsw.Controller;


import it.polimi.ingsw.Model.*;

import java.io.Serializable;
import java.util.Random;

public class PlayerController implements Serializable {

    /**
     * The method picks a random starter card from the game.
     *
     * @param game is the game the cards are picked from.
     * @return the starter card.
     */
    public StarterCard pickCard(Game game){
        return game.getOneStarterCard();
    }

    /**
     * The method picks 2 random objective cards from game.
     *
     * @param game is the game the card are picked from.
     * @return an array containing the objective card.
     */
    public ObjectiveCard[] pickObjCard(Game game){
        return game.pickPlayerObj();
    }

    /**
     * The method populates the hand of a player.
     *
     * @param game is the game the cards are picked from.
     * @param player is the player whom hand will be populated.
     */
    public synchronized void populateHand (Game game,Player player) {
        Deck deckResource;
        Deck deckGold;

        deckResource = game.getDecks()[0];
        deckGold = game.getDecks()[1];

        Random rand = new Random();
        PlayableCard[] cards = new PlayableCard[3];
        cards[0] = deckResource.drawCard(rand.nextInt(deckResource.getNumberOfCards()));
        cards[1] = deckResource.drawCard(rand.nextInt(deckResource.getNumberOfCards()));
        cards[2] = deckGold.drawCard(rand.nextInt(deckGold.getNumberOfCards()));
        // cards in positions 0 and 1 are resources
        // card in position 2 is gold
        player.getHand().addHand(cards);
    }

    /**
     * The method places the first card of a player.
     *
     * @param starterCard is the first card to place.
     * @throws InvalidPositionException if the position is unavailable.
     */
    public void setFirstCard(StarterCard starterCard, PlayerGround pg) throws InvalidPositionException {
        pg.placeCard(starterCard,new Position(42,42));
    }

    /**
     * The method sets the secret objective card of the player.
     *
     * @param obj is the objective card to set.
     */
    public void setObjSecret(ObjectiveCard obj, Hand hand){
        hand.setSecretObj(obj);
    }

}
