package it.polimi.ingsw.Model;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Player {
    private String nickname;
    private Hand hand;
    private PlayerGround playerGround;
    private Game game;
    private static final int maxLengthMsg = 255;
    // maximum length of the message -- 255 is still a temporary value
    private String message;
    // string which will contain the message the player want to send in chat

    // constructor of player with name (which is the nickname)
    // it doesn't make sense to create a player without his nickname
    // need to add the constructor without parameters (?)
    public Player(String name) {
        this.nickname = name;
        this.hand = new Hand();
        this.playerGround = new PlayerGround();
        this.game = null;
    }

    // setter
    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void setPlayerGround(PlayerGround playerGround) {
        this.playerGround = playerGround;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    // getter
    public String getNickname() {
        return this.nickname;
    }

    public Hand getHand() {
        return this.hand;
    }

    public PlayerGround getPlayerGround() {
        return this.playerGround;
    }

    public Game getGame() {
        return this.game;
    }

    // use the standard input (keyboard) to get the message to send in chat
    // if the message is too long only the admissible chars will be sent (message will be cut)
    public String getMessage() {
        Scanner sc = new Scanner(System.in);
        message = sc.next();
        if (message.length() > maxLengthMsg) {
            message = message.substring(0, maxLengthMsg);
        }
        return message;
    }

    // send a message to the public chat
    public void sendMsg() throws RemoteException {
        String messageToSend;
        messageToSend = getMessage();
        game.getChat().send(messageToSend);
    }

    // send a message to a specified player using the private chat
    public void sendMsg(Player player) throws RemoteException, NotExistingChatException {
        String messageToSend;
        messageToSend = getMessage();
        game.getPrivateChat(this, player).send(messageToSend);
    }
}
