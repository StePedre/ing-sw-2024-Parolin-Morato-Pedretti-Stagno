package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;

import java.util.ArrayList;


public class PlayableCard extends Card{
    private ArrayList<Resource> requirements;

    public PlayableCard(int id, ScoreRule rule, Corner[] corners, Corner[] backCorners, ArrayList<Resource> req, Resource color) {
        super(id, rule, corners, backCorners, color);
        this.requirements=req;
    }

    public ArrayList<Resource> getRequirements() {
        return requirements;
    }

    public PlayableCard clone(){
        PlayableCard card = new PlayableCard(this.getId(),this.getRule(),this.getCorners(),this.getBackCorners(),this.requirements,this.getColor());
        return card;
    }
}
