package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * RoundController class implements the controller in charge of managing the round rotation. There are two main
 * elements: players, which is the list of players, and round, which indicates the current round. When -1, the game
 * has not started yet. Almost every method is synchronized, which means each thread calling the method needs to wait
 * for another invocation (if existing) to finish.
 */
public class RoundController implements Serializable {
    private ArrayList<Player> players = new ArrayList<Player>();
    private int round = -1;
    private final Random rand = new Random();

    public RoundController() {

    }
    public synchronized void setplayers(ArrayList<Player> player) {
        if (players.isEmpty()) {
            this.players = player;
        }
    }

    public synchronized void nextRound(){
            if (round == players.size() - 1)
                round = 0;
            else
                round++;
    }

    public synchronized void setFirstPlayer(){
        if (round == -1) {
            round = rand.nextInt(players.size());
        }
    }
    public synchronized Player getCurrentPlayer(){
        return players.get(round);
    }

}
