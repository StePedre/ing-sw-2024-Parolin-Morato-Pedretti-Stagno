package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;


public class PlaceCardController {
    public PlaceCardController() {

    }
    public static void place(PlayableCard card,Player player,Position pos) throws InvalidPositionException, MissingResourcesException {
        PlayerGround ground = player.getPlayerGround();
        ground.placeCard(card,pos);

    }
    public static void removeFromHand(PlayableCard card, Player player){
        Hand hand = player.getHand();
        hand.removeCard(card);
    }
}
