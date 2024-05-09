package it.polimi.ingsw.Model;

import it.polimi.ingsw.View.TUI;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * The Player class implements a player attending a game session.
 * It contains: player's nickname, hand of cards, player ground where played cards are placed, and the instance of the
 * game. It also provides string which will contain a message (of maximum length 255 charachters) the player may want
 * to send in a chat.
 */

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nickname;
    private Hand hand;
    private PlayerGround playerGround;
    private Game game;
    private int reachedObjNo;
    private static final int maxLengthMsg = 255;
    // still a temporary value
    private String message;

    public Player(String nickname, Hand hand, PlayerGround playerGround, Game game, int reachedObjNo, String message) {
        this.nickname = nickname;
        this.hand = hand;
        this.playerGround = playerGround;
        this.game = game;
        this.reachedObjNo = reachedObjNo;
        this.message = message;
    }

    /**
     * Class constructor with nickname as a parameter.
     * It initializes also the hand, the player ground and the game instance as empty.
     *
     * @param name is the nickname of the new player
     */
    // it doesn't make sense to create a player without his nickname
    // need to add the constructor without parameters (?)
    public Player(String name) {
        this.nickname = name;
        this.hand = new Hand();
        this.playerGround = new PlayerGround();
        this.game = null;
    }
    public Player(String name,PlayerGround pg, Hand hand, int reachedObjNo){
        this.playerGround=pg;
        this.nickname=name;
        this.hand=hand;
        this.reachedObjNo=reachedObjNo;
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
     * The method sets the player ground of the player passed as a parameter.
     *
     * @param playerGround to be set for the player.
     */
    public void setPlayerGround(PlayerGround playerGround) {
        this.playerGround = playerGround;
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
     * The method gets the nickname of the player.
     *
     * @return player's nickname as a string.
     */
    public String getNickname() {
        return this.nickname;
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
     * The method gets the player ground of the player.
     *
     * @return player's PlayerGround.
     */
    public PlayerGround getPlayerGround() {
        return this.playerGround;
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
     * The method gets the message a player is willing to send. It uses the standard input (keyboard) to get the message
     * to send in a chat. If the message is too long only the admissible chars will be sent (message will be cut).
     *
     * @return a message, as a string, to be sent.
     */
    public String getMessage() {
        Scanner sc = new Scanner(System.in);
        message = sc.nextLine();
        if (message.length() > maxLengthMsg) {
            message = message.substring(0, maxLengthMsg);
        }
        return message;
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
     * The method sends a message to the global chat, where every player can read it. The global chat can be obtained
     * from the game instance.
     *
     * @exception RemoteException because class Chat extends UnicastRemoteObject.
     */

    public void setReachedObjNo(int num){
        this.reachedObjNo = num;
    }

    /**
     * The method sends a message to the global chat, where every player can read it. The global chat can be obtained
     * from the game instance.
     *
     * @exception RemoteException because class Chat extends UnicastRemoteObject.
     */
    /*public void sendMsg() throws RemoteException {
        String messageToSend;
        messageToSend = getMessage();
        game.getChat().send(messageToSend);
    }*/

    /**
     * The method sends a message to a private chat, specifying the addressee as a parameter.
     * The private chat is obtained by the game instance (see proper method).
     *
     * @param player is the addressee of the message, the other participant in the private chat.
     * @exception RemoteException because class Chat extends UnicastRemoteObject.
     */
    /*public void sendMsg(Player player) throws RemoteException, NotExistingChatException {
        String messageToSend;
        messageToSend = getMessage();
        game.getPrivateChat(this, player).send(messageToSend);
    }*/
}
