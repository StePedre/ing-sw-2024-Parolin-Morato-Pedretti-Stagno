package it.polimi.ingsw.Controller;


import it.polimi.ingsw.Model.*;

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
}
