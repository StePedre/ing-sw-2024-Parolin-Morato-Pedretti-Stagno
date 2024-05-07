package it.polimi.ingsw.Controller;


import it.polimi.ingsw.Model.*;

import java.util.ArrayList;
import java.util.Random;

public class PlayerController {
    PlayerGround pg = null;
    Hand hand = null;
    public PlayerController(PlayerGround pg,Hand hand) {
        this.pg=pg;
        this.hand=hand;
    }
    public void setFirtCard(StarterCard starterCard) throws InvalidPositionException {
        pg.placeCard(starterCard,new Position(42,42));
    }
    public StarterCard pickCard(Game game) throws InvalidPositionException {
        return game.getOneStarterCard();
    }
    public ObjectiveCard[] pickObjCard(Game game){
        return game.pickPlayerObj();
    }
    public void setObjSecret(ObjectiveCard obj){
        hand.setSecretObj(obj);
    }
    public void populateHand (Game game,Player player) {
        Deck deckResource;
        Deck deckGold;
        int indexRandCard1, indexRandCard2, indexRandCard3;
        // these are the index of the random cards to add to each hand

        deckResource = game.getDecks()[0];
        deckGold = game.getDecks()[1];

        Random rand = new Random();
        Card[] cards = new Card[3];

        indexRandCard1 = rand.nextInt(deckResource.getNumberOfCards() - 2) + 2;
        indexRandCard2 = rand.nextInt(deckResource.getNumberOfCards() - 2) + 2;
        indexRandCard3 = rand.nextInt(deckGold.getNumberOfCards() - 2) + 2;
        // +2 avoids the selection of cards that are revealed on the ground
        cards[0] = deckResource.drawCard(indexRandCard1);
        cards[1] = deckResource.drawCard(indexRandCard2);
        cards[2] = deckGold.drawCard(indexRandCard3);
        // cards in positions 0 and 1 are resources
        // card in position 2 is gold
        Hand hand = new Hand(cards);
        player.setHand(hand);
    }
}
