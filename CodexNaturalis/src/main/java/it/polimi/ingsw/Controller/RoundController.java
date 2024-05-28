package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Player;

import java.io.Serializable;
import java.util.ArrayList;
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
    private Player LastPlayer;
    private boolean lastTurn = false;
    private boolean isEnding = false;

    public RoundController() {

    }

    /**
     * Initialize the list of player
     *
     * @param player list of players who partecipate at the game
     */
    public synchronized void setPlayers(ArrayList<Player> player) {
        if (players.isEmpty()) {
            this.players = player;
        }
    }

    public synchronized void nextRound(){
        if(isEnding && LastPlayer.equals(players.get(round))){
            isEnding = false;
            lastTurn = true;
        }
        if(!(lastTurn && LastPlayer.equals(players.get(round)))) {
            if (round == players.size() - 1)
                round = 0;
            else
                round++;
        }
    }

    public Player getLastPlayer() {
        return LastPlayer;
    }

    public synchronized boolean isLastTurn() {
        return lastTurn;
    }
    public synchronized boolean isEnding() {return isEnding;}

    public synchronized void setFirstPlayer(){
        if (round == -1) {
            round = rand.nextInt(players.size());
            if(round == 0){
                LastPlayer = players.getLast();
            }else{
                LastPlayer = players.get(round - 1);
            }
        }
    }
    public synchronized Player getCurrentPlayer(){
        return players.get(round);
    }
    public synchronized void setEnding(){isEnding = true;}
}
