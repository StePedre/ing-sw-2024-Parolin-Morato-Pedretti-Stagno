package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;

import java.util.ArrayList;
import java.util.Random;

public class InitGameController {
    private Game game;

    public InitGameController(Game game){
        this.game=game;
    }

    public Player setFirstPlayer (ArrayList<Player> players) {
        Random rand = new Random();
        int indexFirstPlayer = rand.nextInt(players.size());
        return players.get(indexFirstPlayer);
    }
    
}
