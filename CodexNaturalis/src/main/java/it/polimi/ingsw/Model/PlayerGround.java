package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerGround {
    private int playerScore;
    private Map<Position, Card> cardPosition;
    private Card[][] ground;
    private Set<Position> availablePositions;

    private Set<Position> unavailablePositions;
    private HashMap<Resource, Integer> totalResources;

    // Variable used for calculate covered corners
    private Position lastPositionPlaced;

    // Constructor of PlayerGround
    // TO DO: the size of the ground should adjust based on the number of players (?)
    public PlayerGround(){
        playerScore = 0;
        cardPosition = new HashMap<>();
        ground = new Card[84][84];
        unavailablePositions = new HashSet<>();
        availablePositions = new HashSet<>();
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
        Position initialPosition = new Position(42,42);
        availablePositions.add(initialPosition);
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public Map<Position, Card> getCardPosition() {
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

    // Used for placing a PlayableCard into the PlayerGround. See the method addCard
    public void placeCard(PlayableCard playableCard, Position position) throws MissingResourcesException, InvalidPositionException {
        if (!checkRequirements(playableCard)) {
            throw new MissingResourcesException("Required resources are missing");
        }
        if (!availablePositions.contains(position)) {
            throw new InvalidPositionException("Invalid position");
        }
        // Adds the back resource if the playableCard is flipped

        if(playableCard.getFlip()){
        updateSingleResource(1, playableCard.getColor());
        }
        addCard(playableCard, position);
    }

    // Used for placing the StarterCard. See the Method addCard
    public void placeCard(StarterCard starterCard, Position position) throws InvalidPositionException{
        if (!availablePositions.contains(position)) {
            throw new InvalidPositionException("Invalid position");
        }
        if(!starterCard.getFlip()){
            //TO DO
        }
        addCard(starterCard, position);

    }
    // Method used in addCard and for scoring Objectives
    public void raiseScore(ScoreRule rule){
        playerScore += rule.calculatePoints(this);
    }

    private boolean checkRequirements(PlayableCard card){
        //TO DO
        return true;
    }

    // Used for everything regarding adding a card. It removes the resources of the covered corners, adds the
    // resources of the new card, updates the availablePositions and unavailablePositions, adds the card to the ground
    // and raises the score based on the card ScoreRule
    private void addCard(Card newCard, Position position){
        removeCornersResources(position);
        addCornersResources(newCard);
        updatePositionsAvailability(newCard,position);
        ground[position.getX()][position.getY()] = newCard;
        cardPosition.put(position, newCard);
        raiseScore(newCard.getRule());
    }

    private void removeCornersResources(Position placePosition){
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                // Calculates one of the 4 position around the placePosition and gets the corresponding card
                int newX = placePosition.getX() + calculateOffset(i);
                int newY = placePosition.getY() + calculateOffset(j);
                Card card = ground[newX][newY];
                if (card != null) {
                    // Calculates the index of the corresponding corner that is covered if you place the card in the placePosition
                    int cornerPos = 3 - (j + 2 * i);
                    Resource resourceToRemove = card.getCorners()[cornerPos].getCornerRes();
                    updateSingleResource(-1,resourceToRemove);
                }

            }
        }
    }

    private void addCornersResources(Card card){
        Corner[] corners;
        if(card.getFlip()){
            corners = card.getBackCorners();
        }else{
            corners = card.getCorners();
        }
        // Iterates through all the corners of the card
        for(int i = 0; i<4; i++){
            Resource resourceToAdd = corners[i].getCornerRes();
            updateSingleResource(1,resourceToAdd);
        }
    }

    private void updateSingleResource(int i, Resource resource){
        if( resource != null){
            int value = totalResources.get(resource);
            value += i;
            totalResources.put(resource, value);
        }

    }
    // Used for adding available positions to availablePositions and unavailable positions to unavailablePositions
    private void updatePositionsAvailability(Card card, Position placePosition) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                // Calculates one of the 4 position around the placePosition
                int newX = placePosition.getX() + calculateOffset(i);
                int newY = placePosition.getY() + calculateOffset(j);
                Position newPosition = new Position(newX, newY);
                // checks if the ground is free in that position and if is not unavailable
                if (ground[newX][newY] == null && !unavailablePositions.contains(newPosition)) {
                    // calculates the index of the corresponding covering corner and checks its availability
                    int cornerPosition = j + 2 * i;
                    if (card.getCorners()[cornerPosition].getAvailability()) {
                        availablePositions.add(newPosition);
                    } else {
                        // if the corner is not available, the newPosition becomes unavailable
                        unavailablePositions.add(newPosition);
                        availablePositions.remove(newPosition);
                    }
                }

            }
        }
        // Removes the current position from the availablePositions
        availablePositions.remove(placePosition);
        lastPositionPlaced = placePosition;
    }

    // Method needed to calculate the number of corners covered by placing a card in the lastPositionPlaced.
    // This is accessed only by CoveredCornersRule
    public int calculateNumberOfCoveredCorners() {
        int count = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int newX = lastPositionPlaced.getX() + calculateOffset(i);
                int newY = lastPositionPlaced.getY() + calculateOffset(j);
                Card card = ground[newX][newY];
                if (card != null) {
                    count++;
                }

            }
        }
        return count;
    }

    // Early version of the method used for calculating how many of a given composition of cards are inside ground.
    // Uses nested ifs because it doesn't know from the start if it needs to save the positions,
    // and because the possible compositions are only 3 cards.
    // This Method is accessed only by CompositionRule
    public int calculateNumberOfCompositions(Position[] offSets,Resource[] colors) {
        // takenPosition is needed to save every position of a composition that was already found
        // count is an AtomicInteger because it's accessed inside a lambda function
        Set<Position> takenPositions = new HashSet<>(){};
        AtomicInteger count = new AtomicInteger();
        count.set(0);
        Set<Position> positions = cardPosition.keySet();
        // For each position occupied by cards, do the following:
        positions.forEach(p -> {
            // Checks if the current position has a card with a color equal to the first color of the composition and
            // Checks if the current position is already inside takenPositions
            if (ground[p.getX()][p.getY()].getColor() == colors[0] && !takenPositions.contains(p)) {
                // Calculates the nex position thanks to the offsets of the composition
                int secondX = p.getX() + offSets[1].getX();
                int secondY = p.getY() + offSets[1].getY();
                Position secondPosition = new Position(secondX, secondY);
                // Checks if the second position has a card with a color equal to the second color of the composition and
                // Checks if the second position is already inside takenPositions
                if (ground[secondX][secondY].getColor() == colors[1] && !takenPositions.contains(secondPosition)) {
                    int thirdX = p.getX() + offSets[2].getX();
                    int thirdY = p.getY() + offSets[2].getY();
                    Position thirdPosition = new Position(thirdX, thirdY);
                    if (ground[thirdX][thirdY].getColor() == colors[2] && !takenPositions.contains(thirdPosition)) {
                        // Composition found; increments the atomicInteger and saves the 3 positions found into takenPositions
                        count.set(count.getAndIncrement());
                        takenPositions.add(p);
                        takenPositions.add(secondPosition);
                        takenPositions.add(thirdPosition);
                    }
                }
            }
        });
        return count.get();
    }


    // Used for calculating the offSets needed for a bunch of stuff
    private int calculateOffset(int value){
        return value == 0 ? -1 : 1;
    }


}