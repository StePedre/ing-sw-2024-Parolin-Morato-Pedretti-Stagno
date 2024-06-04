package it.polimi.ingsw.Controller;


import it.polimi.ingsw.Model.*;

import java.util.Random;

public class PlayerController {


    /**
     * set the first card of a player
     *
     * @param starterCard the first card to set
     * @throws InvalidPositionException position unavailable
     */
    public void setFirstCard(StarterCard starterCard, PlayerGround pg) throws InvalidPositionException {
        pg.placeCard(starterCard,new Position(42,42));
    }

    /**
     * pick a random starter card from game
     *
     * @param game teh game which the card are picked
     * @return the starter card
     */
    public StarterCard pickCard(Game game){
        return game.getOneStarterCard();
    }

    /**
     * pick 2 random objective card from game
     *
     * @param game the game which the card are picked
     * @return an array contained the objective card
     */
    public ObjectiveCard[] pickObjCard(Game game){
        return game.pickPlayerObj();
    }

    /**
     * set the secret objective card of the player
     *
     * @param obj the objective card to set
     */
    public void setObjSecret(ObjectiveCard obj, Hand hand){
        hand.setSecretObj(obj);
    }

    /**
     * populate the hand of a player
     *
     * @param game the game where the card are picked
     * @param player the player in whom hand will be populated
     */
    public synchronized void populateHand (Game game,Player player) {
        Deck deckResource;
        Deck deckGold;
        int indexRandCard1, indexRandCard2, indexRandCard3;
        // these are the index of the random cards to add to each hand

        deckResource = game.getDecks()[0];
        deckGold = game.getDecks()[1];

        Random rand = new Random();
        PlayableCard[] cards = new PlayableCard[3];
        // +2 avoids the selection of cards that are revealed on the ground
        cards[0] = deckResource.drawCard(rand.nextInt(deckResource.getNumberOfCards()));
        cards[1] = deckResource.drawCard(rand.nextInt(deckResource.getNumberOfCards()));
        cards[2] = deckGold.drawCard(rand.nextInt(deckGold.getNumberOfCards()));
        // cards in positions 0 and 1 are resources
        // card in position 2 is gold
        player.getHand().addHand(cards);
    }
}
