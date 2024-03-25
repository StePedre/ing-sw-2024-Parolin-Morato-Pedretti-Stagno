package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;
import java.util.*;

public class PlayerGround {
    private int playerScore;
    private Map<Card, Position> cardPosition;
    private Card[][] ground;
    private Set<Position> availablePositions;
    private HashMap<Resource, Integer> totalResources;

    // Variable used for the covered corners
    // maybe it'll be removed
    private int numberOfLastCoveredCorners;

    // Constructor of PlayerGround
    // TO DO: the size of the ground should adjust based on the number of players (?)
    public PlayerGround(){
        playerScore = 0;
        cardPosition = new HashMap<>();
        ground = new Card[82][82];

        availablePositions = new Set<>();
        initializePosition();

        totalResources = new HashMap<>();
        initializeResources();
    }

    private void initializeResources() {
        for (Resource resource : Resource.values()) {
            totalResources.put(resource, 0);
        }
    }

    private void initializePosition(){
        Position initialPosition = new Position(41,41);
        availablePositions.add(initialPosition);
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public Map<Card, Position> getCardPosition() {
        return cardPosition;
    }

    public Card[][] getGround() {
        return ground;
    }

    public Set<Position> getAvailablePositions() {
        return availablePositions;
    }

    public HashMap<Resource, Integer> getTotalResources() {
        return totalResources;
    }

    public int getNumberOfLastCoveredCorners(){
        return numberOfLastCoveredCorners;
    }

    public void placeCard(Card card, Position position){

        //TO DO.
        raiseScore(card.getRule());
    }

    // Method used in PlaceCard and for scoring Objectives
    public void raiseScore(ScoreRule rule){
        playerScore += rule.calculatePoints(this);
    }

    //TO DO.

}