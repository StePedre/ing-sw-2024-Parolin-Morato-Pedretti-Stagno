package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;

public abstract class Card {
    private int id;
    private ScoreRule rule;
    private boolean isFlipped;
    private Corner[] corners;
    private Corner[] backCorners;

    private Resource color;
    public Card(int id, ScoreRule rule, Corner[] corners, Corner[] backCorners, Resource color){
        this.id=id;
        this.rule=rule;
        this.isFlipped=false;
        this.corners=corners;
        this.backCorners=backCorners;
        this.color = color;
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

    public Resource getColor(){
        return color;
    }
    public void flipCard(){
        isFlipped=!isFlipped;
    }
    public boolean equals(Card card){
        return this.id==card.id;
    }
}
