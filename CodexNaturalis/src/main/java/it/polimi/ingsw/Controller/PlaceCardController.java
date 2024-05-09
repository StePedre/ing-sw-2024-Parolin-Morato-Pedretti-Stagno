package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;


public class PlaceCardController {
    public PlaceCardController() {

    }
    public static void place(PlayableCard card,Player player,Position pos) throws InvalidPositionException, MissingResourcesException {
        PlayerGround ground = player.getPlayerGround();
        try{
            ground.placeCard(card,pos);
        }
        catch(InvalidPositionException e){
            throw e;
        }
        catch (MissingResourcesException e){
            throw e;
        }

    }
}
