package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Player;
import java.util.Arrays;
import java.util.Random;

public class RoundController {
    Player[] players;
    int round;
    Random rand = new Random();

    RoundController(Player[] players, int numPlayers) {
        this.players = Arrays.copyOf(players,numPlayers);
        round = rand.nextInt(numPlayers);
    }
    public Player nextRound(){
        if(round== players.length-1)
            round=0;
        else
            round++;
        return players[round];
    }

}
