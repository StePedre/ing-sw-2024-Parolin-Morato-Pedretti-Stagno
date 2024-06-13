package it.polimi.ingsw.Model;

import java.io.Serializable;

public class Move implements Serializable {
    private final Card card;
    private final Position pos;
    public Move(Card card,Position pos){
        this.card = card;
        this.pos=pos;
    }

    public Card getCard() {
        return card;
    }

    public Position getPos() {
        return pos;
    }
}
