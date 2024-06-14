package it.polimi.ingsw.Model;

import java.io.Serializable;

/**
 * The class Move represents a possible move made by a player during a game.
 * Every move has the Card played and the Position in which the card has been played.
 *
 */
public class Move implements Serializable {
    private final Card card;
    private final Position pos;

    /**
     * Class constructor.
     *
     * @param card is the card played in that particular move.
     * @param pos is the position in which the card has been played.
     */
    public Move(Card card,Position pos){
        this.card = card;
        this.pos=pos;
    }

    /**
     * The method gets the Card played in a move.
     *
     * @return the Card played.
     */
    public Card getCard() {
        return card;
    }

    /**
     * The method gets the Position of a played card.
     *
     * @return the position in which the card has been played.
     */
    public Position getPos() {
        return pos;
    }
}
