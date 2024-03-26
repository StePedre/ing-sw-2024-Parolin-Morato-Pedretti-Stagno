package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;

import java.util.ArrayList;


public class PlayableCard extends Card{
    private ArrayList<Resource> requirements;
    private int points;
    private Resource backResource;
    public PlayableCard(int id, ScoreRule rule, Corner[] corners, Corner[] backCorners, int point, ArrayList<Resource> req, Resource backResource) {
        super(id, rule, corners, backCorners);
        this.points=point;
        this.requirements=req;
        this.backResource=backResource;
    }

    public String getColor() {
        return backResource.getColor();
    }

    public int getPoints() {
        return points;
    }

    public Resource getBackResource() {
        return backResource;
    }

    public ArrayList<Resource> getRequirements() {
        return requirements;
    }
}
