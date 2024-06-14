package it.polimi.ingsw.Model;

import it.polimi.ingsw.View.TUI;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * The Player class implements a player attending a game session.
 * It contains: player's nickname, hand of cards, player ground where cards are placed, the instance of the
 * game, the number of reached objectives and the color the Player choose at the start of a game.
 */

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nickname;
    private Hand hand;
    private PlayerGround playerGround;
    private Game game;
    private int reachedObjNo;
    private String color;

    /**
     * Class constructor with all the parameters.
     *
     * @param nickname is the nickname of the player.
     * @param hand is the hand of the player.
     * @param playerGround is the player's player ground.
     * @param game is the instance of the game the player is in.
     * @param reachedObjNo is the number of reached objectives during the game.
     */
    public Player(String nickname, Hand hand, PlayerGround playerGround, Game game, int reachedObjNo) {
        this.nickname = nickname;
        this.hand = hand;
        this.playerGround = playerGround;
        this.game = game;
        this.reachedObjNo = reachedObjNo;
    }

    /**
     * Class constructor with nickname as a parameter.
     * It initializes also the hand, the player ground and the game instance as empty.
     *
     * @param name is the nickname of the new player
     */
    public Player(String name) {
        this.nickname = name;
        this.hand = new Hand();
        this.playerGround = new PlayerGround();
        this.game = null;
    }

    /**
     * Class constructor with some parameters.
     *
     * @param name is the nickname of the player.
     * @param hand is the hand of the player.
     * @param pg is the player's player ground.
     * @param reachedObjNo is the number of reached objectives during the game.
     */
    public Player(String name,PlayerGround pg, Hand hand, int reachedObjNo){
        this.playerGround=pg;
        this.nickname=name;
        this.hand=hand;
        this.reachedObjNo=reachedObjNo;
    }

    /**
     * The method gets the color associated with this player.
     *
     * @return player's color.
     */
    public String getColor(){
        return color;
    }

    /**
     * The method gets the game in which the player is participating.
     *
     * @return instance of the Game the player is attending.
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * The method gets the hand of the player.
     *
     * @return player's Hand.
     */
    public Hand getHand() {
        return this.hand;
    }

    /**
     * The method gets the nickname of the player.
     *
     * @return player's nickname as a string.
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * The method gets the player ground of the player.
     *
     * @return player's PlayerGround.
     */
    public PlayerGround getPlayerGround() {
        return this.playerGround;
    }

    /**
     * The method gets the number of objectives (out of 3) that a player has reached. One is secret and two are common.
     *
     * @return the number of reached objectives.
     */
    public int getReachedObjNo(){
        return reachedObjNo;
    }

    /**
     * Set the color of the player in the game
     *
     * @param s the color choose by the player
     */
    public void setColor(String s){
        this.color = s;
    }

    /**
     * The method sets the instance of the game passed as a parameter.
     *
     * @param game to be set for the player.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * The method sets the hand of the player passed as a parameter.
     *
     * @param hand to be set for the player
     */
    public void setHand(Hand hand) {
        this.hand = hand;
    }

    /**
     * The method sets the nickname of the player.
     *
     * @param nickname is the name typed by the player.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * The method sets the player ground of the player passed as a parameter.
     *
     * @param playerGround to be set for the player.
     */
    public void setPlayerGround(PlayerGround playerGround) {
        this.playerGround = playerGround;
    }

    /**
     * The method sets the number of objectives (out of 3) that a player has reached. One is secret and two are common.
     *
     * @param num is the number of objectives reached to be set.
     */
    public void setReachedObjNo(int num){
        this.reachedObjNo = num;
    }

}
