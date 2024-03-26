package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;

import java.util.ArrayList;
public class StarterCard extends Card{
    private ArrayList<Resource> backRes;
    public StarterCard(int id, ScoreRule rule, Corner[] corners, Corner[] backCorners, ArrayList<Resource> backRes) {
        super(id, rule, corners, backCorners);
        this.backRes=backRes;
    }

    public ArrayList<Resource> getBackRes() {
        return backRes;
    }
    public StarterCard clone(){
        StarterCard card = new StarterCard(this.getId(),this.getRule(),this.getCorners(),this.getBackCorners(),this.backRes);
        return card;
    }
}
