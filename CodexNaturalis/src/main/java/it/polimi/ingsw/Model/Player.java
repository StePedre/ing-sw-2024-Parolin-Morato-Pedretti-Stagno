package it.polimi.ingsw.Model;

public class Player {
    private String nickname;
    private Hand hand;
    private PlayerGround playerGround;
    private Game game;
    // maximum length of the message -- 255 is still a temporary value
    private static final maxLengthMsg = 255;
    // string which will contain the message the player want to send in chat
    private string message[maxLengthMsg];

    // constructor of player with name (which is the nickname)
    // it doesn't make sense to create a player without his nickname
    // need to add the constructor without parameters (?)
    public Player(String name) {
        this.nickname = name;
        this.hand = new Hand();
        this.playerGround = new PlayerGround();
        this.game = new Game();
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

    // send a message to the public chat
    public void sendMsg(){


    }

    // send a message to a specified player using the private chat
    public void sendMsg(Player player) {


    }
}
