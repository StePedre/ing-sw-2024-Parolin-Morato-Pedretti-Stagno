package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Player;
import java.util.Arrays;
import java.util.Random;

public class RoundController {
    private static Player[] players = null;
    private static int round = -1;
    private Random rand = new Random();

    RoundController(Player[] players, int numPlayers) {
        if(this.players==null) {
            this.players = Arrays.copyOf(players, numPlayers);
        }
    }
    public void nextRound(){
        if(round== players.length-1)
            round=0;
        else
            round++;
    }
    public void setFirstPlayer(){
        if(round==-1) {
            round = rand.nextInt(players.length);
        }
    }
    public Player getCurrentPlayer(){
        return players[round];
    }

}
