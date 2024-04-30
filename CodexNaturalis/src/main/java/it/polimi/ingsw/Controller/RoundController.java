package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Player;
import java.util.Arrays;
import java.util.Random;

public class RoundController {
    private static Player[] players = null;
    private static int round = -1;
    private Random rand = new Random();

    public RoundController(Player[] players, int numPlayers) {
        synchronized (players) {
            if (this.players == null) {
                this.players = Arrays.copyOf(players, numPlayers);
            }
        }
    }
    public void nextRound(){
        synchronized (players) {
            if (round == players.length - 1)
                round = 0;
            else
                round++;
        }
    }
    public void setFirstPlayer(){
        synchronized (players) {
            if (round == -1) {
                round = rand.nextInt(players.length);
            }
        }
    }
    public Player getCurrentPlayer(){
        return players[round];
    }

}
