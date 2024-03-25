package it.polimi.ingsw.Model;
import java.util.ArrayList;


public class PlayableCard extends Card{
    private Color color;
    private ArrayList<Resource> requirements;
    private int points;
    private Resource backResource;
    public PlayableCard(int id, ScoreRule rule, Corner[] corners, Corner[] backCorners,Color color,int point,ArrayList<Resource> req, Resource backResource) {
        super(id, rule, corners, backCorners);
        this.color=color;
        this.points=point;
        this.requirements=req;
        this.backResource=backResource
    }

    public Color getColor() {
        return color;
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
