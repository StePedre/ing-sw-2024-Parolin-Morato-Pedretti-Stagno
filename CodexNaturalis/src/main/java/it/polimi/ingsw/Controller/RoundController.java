package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RoundController {
    private static ArrayList<Player> players = null;
    private static int round = -1;
    private Random rand = new Random();

    public RoundController(ArrayList<Player> players) {
        synchronized (players) {
            if (this.players == null) {
                this.players = players;
            }
        }
    }
    public void nextRound(){
        synchronized (players) {
            if (round == players.size() - 1)
                round = 0;
            else
                round++;
        }
    }
    public void setFirstPlayer(){
        synchronized (players) {
            if (round == -1) {
                round = rand.nextInt(players.size());
            }
        }
    }
    public Player getCurrentPlayer(){
        return players.get(round);
    }

}
