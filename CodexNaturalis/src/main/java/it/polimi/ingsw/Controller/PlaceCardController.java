package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;

/**
 * The class PlaceCardController implements the controller in charge of managing the positioning of a card on the player
 * ground.
 * This means that a Card needs to be placed onto the player ground and then needs to be removed from the hand of
 * the player that played that card.
 */

public class PlaceCardController {
    /**
     * This method takes a player, a card and his position and place it in his playground
     *
     * @param card the card to place
     * @param player the player who play the card
     * @param pos the position to place che card
     * @throws InvalidPositionException position not available
     * @throws MissingResourcesException requirement for place the card not respected
     */
    public static void place(PlayableCard card,Player player,Position pos) throws InvalidPositionException, MissingResourcesException {
        PlayerGround ground = player.getPlayerGround();
        ground.placeCard(card,pos);

    }

    /**
     * Remove a card from the player hand
     *
     * @param card the card to remove
     * @param player the player who has the card to remove
     */
    public static void removeFromHand(PlayableCard card, Player player){
        Hand hand = player.getHand();
        hand.removeCard(card);
    }
}
