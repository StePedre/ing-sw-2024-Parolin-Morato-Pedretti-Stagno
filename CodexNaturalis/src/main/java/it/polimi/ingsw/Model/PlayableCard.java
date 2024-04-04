package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;

import java.util.ArrayList;
import java.util.HashMap;


public class PlayableCard extends Card{
    private HashMap<Resource, Integer> requirements;

    public PlayableCard(int id, ScoreRule rule, Corner[] corners, Corner[] backCorners, HashMap<Resource, Integer> req, Resource color) {
        super(id, rule, corners, backCorners, color);
        this.requirements=req;
    }

    public HashMap<Resource, Integer> getRequirements() {
        return requirements;
    }

    public PlayableCard clone(){
        PlayableCard card = new PlayableCard(this.getId(),this.getRule(),this.getCorners(),this.getBackCorners(),this.requirements,this.getColor());
        return card;
    }
}
