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
 * It also has a Player to save the last player of the round, a boolean that states if it's the last turn and a boolean
 * to state if the round is ending.
 */
public class RoundController implements Serializable {
    private ArrayList<Player> players = new ArrayList<Player>();
    private int round = -1;
    private final Random rand = new Random();
    private Player LastPlayer;
    private boolean lastTurn = false;
    private boolean isEnding = false;

    /**
     * Class constructor.
     * It initializes the players.
     */
    public RoundController() {
        players = new ArrayList<>();
    }

    /**
     * The method adds a player to the list of Player playing the game.
     *
     * @param player is the Player to be added.
     */
    public synchronized void addPlayer(Player player){
        players.add(player);
    }

    /**
     * The method gets the player that currently is playing.
     *
     * @return such player.
     */
    public Player getCurrentPlayer(){
        return players.get(round);
    }

    /**
     * The method gets the last Player of the round.
     *
     * @return such player.
     */
    public Player getLastPlayer() {
        return LastPlayer;
    }

    /**
     * The method gets the boolean that tells if the round is ending.
     *
     * @return such boolean.
     */
    public synchronized boolean isEnding() {return isEnding;}

    /**
     * The method gets the boolean that tells if the turn is the last one.
     *
     * @return such boolean.
     */
    public synchronized boolean isLastTurn() {
        return lastTurn;
    }

    /**
     * The method firstly checks if it is the last turn to play.
     * If not, a normal round is about to start then the round variable is increased.
     */
    public synchronized void nextRound(){
        if(isEnding && LastPlayer.equals(players.get(round))){
            isEnding = false;
            lastTurn = true;
        }
        if (round == players.size() - 1) {
            round = 0;
        }else {
            round++;
        }
    }

    /**
     * The method sets the ending of the turn. It is called once all the players have completed their round of play.
     */
    public synchronized void setEnding(){isEnding = true;}

    /**
     * The method sets the first player to play in a turn. This method is only called at the start of a new game.
     * It sets as the last player the one at a random position, with the random number that changes with the number of
     * players in the game.
     */
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

}
