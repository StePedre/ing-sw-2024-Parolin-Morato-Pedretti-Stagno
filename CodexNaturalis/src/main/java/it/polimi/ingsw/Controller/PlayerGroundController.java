package it.polimi.ingsw.Controller;


import it.polimi.ingsw.Model.*;

import java.util.ArrayList;
import java.util.Random;

public class PlayerGroundController {
    PlayerGround pg = null;
    public PlayerGroundController(PlayerGround pg) {
        this.pg=pg;
    }
    public void setFirtCard(StarterCard starterCard) throws InvalidPositionException {
        pg.placeCard(starterCard,new Position(42,42));
    }
    public StarterCard pickCard(Game game) throws InvalidPositionException {
        return game.getOneStarterCard();
    }
}
