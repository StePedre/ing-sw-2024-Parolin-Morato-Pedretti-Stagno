package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * RoundController class implements the controller in charge of managing the round rotation. There are two main
 * elements: players, which is the list of players, and round, which indicates the current round. When -1, the game
 * has not started yet. Almost every method is synchronized, which means each thread calling the method needs to wait
 * for another invocation (if existing) to finish.
 */
public class RoundController {
    private static ArrayList<Player> players = null;
    private static int round = -1;
    private final Random rand = new Random();
    private static final Object lock= new Object();

    public RoundController(ArrayList<Player> players) {
        synchronized (lock) {
            if (this.players == null) {
                this.players = players;
            }
        }
    }
    public void nextRound(){
        synchronized (lock) {
            if (round == players.size() - 1)
                round = 0;
            else
                round++;
        }
    }

    public void setFirstPlayer(){
        synchronized (lock) {
            if (round == -1) {
                round = rand.nextInt(players.size());
            }
        }
    }
    public Player getCurrentPlayer(){
        return players.get(round);
    }

}
