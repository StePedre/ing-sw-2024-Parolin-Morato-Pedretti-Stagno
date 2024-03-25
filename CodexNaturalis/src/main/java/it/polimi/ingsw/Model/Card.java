package it.polimi.ingsw.Model;

public abstract class Card {
    private int id;
    private ScoreRule rule;
    private boolean isFlipped;
    private Corner[] corners;
    private Corner[] backCorners;
    public Card(int id, ScoreRule rule, Corner[] corners, Corner[] backCorners){
        this.id=id;
        this.rule=rule;
        this.isFlipped=false;
        this.corners=corners;
        this.backCorners=backCorners;
    }

    public int getId() {
        return id;
    }

    public Corner[] getBackCorners() {
        return backCorners;
    }

    public Corner[] getCorners() {
        return corners;
    }

    public boolean getFlip() {
        return isFlipped;
    }

    public ScoreRule getRule() {
        return rule;
    }
    public void flipCard(){
        isFlipped=!isFlipped;
    }
}
