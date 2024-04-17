package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;


public class PlaceCardController {
    public PlaceCardController() {

    }
    public void place(PlayableCard card,Player player,Position pos) {
        PlayerGround ground = player.getPlayerGround();
        try{
            ground.placeCard(card,pos);
        }
        catch(InvalidPositionException e){
            System.out.println("Invalid position");
        }
        catch (MissingResourcesException e){
            System.out.println("Missing Resource");
        }

    }
}
