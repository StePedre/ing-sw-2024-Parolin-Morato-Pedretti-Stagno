package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The PlayerGround class represents the table of a player in a game session. In Player class, each player is
 * associated with a PlayerGround. It contains the current player's score and all the cards played on the ground,
 * in a matrix where each Card is mapped to a Position (made of two coordinates).
 * There are also a set of available positions and a set of unavailable ones.
 * The PlayerGround also counts the total number of resource of each type and has a variable used for calculate
 * covered corners (which means a new Card cannot be attached there).
 */

public class PlayerGround implements Serializable {
    private static final long serialVersionUID = 2L;
    private int playerScore;
    private Map<Position, Card> cardPosition;
    private Card[][] ground;
    private Set<Position> availablePositions;
    private Set<Position> unavailablePositions;
    private HashMap<Resource, Integer> totalResources;
    private Position lastPositionPlaced;

    private Card availabilityCard;

    // TO DO: the size of the ground should adjust based on the number of players (?)

    /**
     * Class constructor without parameters.
     * It initializes player's score to 0.
     * It creates a new squared Card matrix (the ground) with the maximum possible dimension.
     * It creates a new hash map for resource counting and two new hash sets for available and unavailable positions,
     * initializing resources map and positions sets by calling two auxiliary methods.
     */
    public PlayerGround(){
        playerScore = 0;
        cardPosition = new HashMap<>();
        ground = new Card[84][84];
        unavailablePositions = new HashSet<>();
        availablePositions = new HashSet<>();
        initializePosition();

        totalResources = new HashMap<>();
        initializeResources();
        availabilityCard = new PlayableCard(-1,null,null,null,null,null);
    }

    /**
     * The method initialize the resources map by giving count 0 to each type of Resource (there are 7).
     */
    private void initializeResources() {
        for (Resource resource : Resource.values()) {
            totalResources.put(resource, 0);
        }
    }

    /**
     * The method initialize the set of available positions with the first card, put in the center of the matrix.
     */
    private void initializePosition(){
        Position initialPosition = new Position(42,42);
        availablePositions.add(initialPosition);
    }

    /**
     * The method gets the score (a number) of the player associated with this PlayerGround.
     *
     * @return the Player's score.
     */
    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int i) { this.playerScore=i;
    }

    /**
     * The method gets the map between each position and its card (if present).
     *
     * @return map between Position and Card.
     */
    public Map<Position, Card> getCardPosition() {
        return cardPosition;
    }

    /**
     * The method gets the whole ground, which is a matrix. Some cells may be without a value, if there is no element
     * of the Position-Card map that matches cell coordinates with a card.
     *
     * @return the Player's ground.
     */
    public Card[][] getGround() {
        return ground;
    }

    /**
     * The method gets the set of available positions where it's possible to place a card.
     *
     * @return set of available Positions.
     */
    public Set<Position> getAvailablePositions() {
        return availablePositions;
    }

    public Set<Position> getUnavailablePositions() {
        return unavailablePositions;
    }

    /**
     * The method gets the map between each resource and its amount, which can be zero.
     *
     * @return map between Resource and Integer (their count).
     */
    public HashMap<Resource, Integer> getTotalResources() {
        return totalResources;
    }

    /**
     * The method place the starter card (which means no other playable card and no objective card) on the ground.
     * Firstly it checks if the desired position is in the set of available position: if not, it throws an exception.
     * When the first card must be attached, there should be only one available position: the center of the ground.
     * Then, if the card is proposed face down, a method to update resources count is called.
     * The method addCard is invoked if no exception arose.
     *
     * @param starterCard is the first card to be attached on the ground.
     * @param position is the desired position where to place the card.
     * @exception InvalidPositionException arises when the desired position does not belong to available positions set.
     */
    public void placeCard(StarterCard starterCard, Position position) throws InvalidPositionException{
        if (!checkPosition(position, availablePositions)) {
            throw new InvalidPositionException("Invalid position");
        }
        if(!starterCard.getFlip()){
            updateMultipleResources(starterCard.getBackRes());
        }
        addCard(starterCard, position);

    }

    /**
     * The method place a playable card (which means no starter card and no objective card) on the ground.
     * Firstly it checks if the card's requirements are satisfied: if not, it throws an exception.
     * Then it checks if the desired position is in the set of available position: if not, it throws an exception.
     * Finally, if the card is proposed face down, a method to update resources count is called.
     * The method addCard is invoked if no exception arose.
     *
     * @param playableCard is the card to be attached on the ground.
     * @param position is the desired position where to place the card.
     * @exception MissingResourcesException arises when trying to place a card without fulfilling its requirements.
     */
    public void placeCard(PlayableCard playableCard, Position position) throws MissingResourcesException, InvalidPositionException {
        if (!checkRequirements(playableCard)) {
            throw new MissingResourcesException("Required resources are missing");
        }
        if (!checkPosition(position,availablePositions)) {
            throw new InvalidPositionException("Invalid position");
        }
        // Adds the back resource if the playableCard is flipped

        if(playableCard.getFlip()){
        updateSingleResource(1, playableCard.getColor());
        }
        addCard(playableCard, position);
    }

    /**
     * The method checks if the requirements to place a card, passed as a parameter, are fulfilled.
     * This happens in two situations: the trivial one, when there are no requirements, and the more complex one.
     * For the more complex, it works as follows: for each type of requirements (which is a Resource), if the number
     * of requested resource is higher than the number already present on the ground, the requirements are
     * automatically not satisfied.
     *
     * @param card needs to have its requirements checked.
     * @return true if the requirements are satisfied, false otherwise.
     */
    private boolean checkRequirements(PlayableCard card){
        if(card.getRequirements() == null){
            return true;
        }
        // If requirements are not null, iterates through the requirements of the card
        // and compare the value to that of the resource inside totalResources
        for (Map.Entry<Resource, Integer> resource : card.getRequirements().entrySet()) {
            Resource key = resource.getKey();
            Integer value = resource.getValue();

            if (totalResources.containsKey(key)) {
                Integer value2 = totalResources.get(key);
                if (value > value2) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * The method is used for everything regarding adding a card. By invoking specific methods, it removes the resources
     * of the covered corners, adds the resources from the corners of the new card, updates the availablePositions and
     * unavailablePositions sets and raises the score based on the card ScoreRule. Moreover, it adds the card to the
     * ground and the Position-Card map.
     *
     * @param newCard is the card to be added.
     * @param position is the desired position for that card to be added in.
     */
    private void addCard(Card newCard, Position position){
        removeCornersResources(position);
        addCornersResources(newCard);
        updatePositionsAvailability(newCard,position);
        raiseScore(newCard.getRule());
        ground[position.getX()][position.getY()] = newCard;
        cardPosition.put(position, newCard);
    }

    /**
     * The method is used to remove from the total resource count the resource on a card's corner (previously placed)
     * covered by a card added in the position passed as a parameter. For each of the four possible positions around
     * the placePosition, it gets the corresponding card and, if not null, it calculates the index of the
     * corner that will be covered if a card is placed in the placePosition.
     * Finally, it gets that corner resource (if present) and updates the resource count.
     *
     * @param placePosition is where the new card has been placed, used to calculate which corner has been covered.
     */
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
                    Resource resourceToRemove = card.getShowedCorners()[cornerPos].getCornerRes();
                    updateSingleResource(-1,resourceToRemove);
                }

            }
        }
    }

    /**
     * The method updates the total number of resources by getting all the resources present on the card's front
     * corners (if not flipped) or back corners (if flipped).
     *
     * @param card is the newly placed card and its corner resources must be added to the total resources count.
     */
    private void addCornersResources(Card card){
        Corner[] corners = card.getShowedCorners();
        // Iterates through all the corners of the card
        for(int i = 0; i<4; i++){
            Resource resourceToAdd = corners[i].getCornerRes();
            updateSingleResource(1,resourceToAdd);
        }
    }

    /**
     * The method updates one single type of resource, passed as a parameter, increasing the total count of it by
     * a value passed as a parameter as well. All resources are, in fact, mapped with their total count.
     *
     * @param i is the value the resource needs to be increased by.
     * @param resource its count must be increased by the other parameter i.
     */
    private void updateSingleResource(int i, Resource resource){
        if( resource != null){
            int value = totalResources.get(resource);
            value += i;
            totalResources.put(resource, value);
        }

    }

    /**
     * The method increases by one more than one resource type at the same time.
     *
     * @param resources is the list of resources that needs to be incremented.
     */
    private void updateMultipleResources(ArrayList<Resource> resources){
        resources.forEach(r ->{
            updateSingleResource(1,r);
        });
    }

    /**
     * The method adds newly available positions to set availablePositions and unavailable positions to set
     * unavailablePositions. To do that, it calculates the four possible positions around the position of the last added
     * card. For each new position, if the ground is free in that position and if is not already set as unavailable, it
     * calculates the index of the corresponding covering corner and checks its availability.
     * If the corner is not available, e.g. hidden corner, the new position becomes unavailable and gets removed from
     * available ones.
     * Finally, the position of the last added card becomes unavailable, and it's marked as the last placed position.
     *
     * @param card is the last added card.
     * @param placePosition is the position of the last added card.
     */
    private void updatePositionsAvailability(Card card, Position placePosition) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                // Calculates one of the 4 position around the placePosition
                int newX = placePosition.getX() + calculateOffset(i);
                int newY = placePosition.getY() + calculateOffset(j);
                Position newPosition = new Position(newX, newY);
                // checks if the ground is free in that position and if is not unavailable
                if ((ground[newX][newY] == null || ground[newX][newY].getId() ==-1) && !checkPosition(newPosition, unavailablePositions)) {
                    // calculates the index of the corresponding covering corner and checks its availability
                    int cornerPosition = j + 2 * i;
                    if (card.getShowedCorners()[cornerPosition].getAvailability()) {
                        availablePositions.add(newPosition);
                        ground[newX][newY] = availabilityCard;
                    } else {
                        // if the corner is not available, the newPosition becomes unavailable
                        unavailablePositions.add(newPosition);
                        availablePositions.remove(getExactPosition(newPosition, availablePositions));
                        ground[newX][newY] = null;
                    }
                }

            }
        }
        // Removes the current position from the availablePositions
        availablePositions.remove(getExactPosition(placePosition, availablePositions));
        lastPositionPlaced = placePosition;
    }


    /**
     * The method, used when a new card is added, only raises the player's score by calculating the points given
     * by the new card. Each card has a ScoreRule that automatically calculates how many points that card gives.
     *
     * @param rule is the scoring rule for which the points need to be calculated.
     */
    public void raiseScore(ScoreRule rule){
        playerScore += rule.calculatePoints(this);
    }

    /** The method calculates the number of corners covered by placing a card in the lastPositionPlaced. This method is
     * accessed only by CoveredCornersRule, which needs the said number to assign points.
     *
     * @return the number of covered corners.
     */
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


    /**
     * The method calculates how many of a given composition of cards are inside ground. It is accessed only by
     * CompositionRule, if there is need. For each card on the ground, if the color matches the first one of the
     * composition, the next card positioned in the correct offset (given by the composition) is checked. If the color
     * matches again, the third one is checked in the same way. If the composition is find, the count is raised.
     * Since the cards that match a composition can only be used once, when a composition match is found, all cards
     * are set as taken in the proper set takenPositions. Therefore, when checking a possible composition match, it must
     * be also checked if those cards are not already inside takenPositions.
     *
     * @param colors is the list of resources (more specifically, their colours) that appear in the composition.
     * @param offSets is the list of positions that constitute the composition, e.g. three position one above the other.
     * @return number on the ground of a specific composition of cards.
     */
    // Early version of the method
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
            if (ground[p.getX()][p.getY()].getColor() == colors[0] && !checkPosition(p, takenPositions)) {
                // Calculates the nex position thanks to the offsets of the composition
                int secondX = p.getX() + offSets[1].getX();
                int secondY = p.getY() + offSets[1].getY();
                Position secondPosition = new Position(secondX, secondY);
                // Checks if the second position has a card with a color equal to the second color of the composition and
                // Checks if the second position is already inside takenPositions
                if (ground[secondX][secondY].getColor() == colors[1] && !checkPosition(secondPosition, takenPositions)) {
                    int thirdX = p.getX() + offSets[2].getX();
                    int thirdY = p.getY() + offSets[2].getY();
                    Position thirdPosition = new Position(thirdX, thirdY);
                    if (ground[thirdX][thirdY].getColor() == colors[2] && !checkPosition(thirdPosition, takenPositions)) {
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


    /** The method is used by different methods involving positions. It calculates the offset from a numeric value
     * passed as a parameter.
     *
     * @param value is the starting index to calculate the offset.
     * @return is -1 if the parameter equals 0, otherwise it's 1.
     */
    private int calculateOffset(int value){
        return value == 0 ? -1 : 1;
    }

    private boolean checkPosition(Position position, Set<Position> positions){
        for(Position p : positions){
            if(p.getX() == position.getX() && p.getY() == position.getY()){
                return true;
            }
        }
        return false;
    }

    private Position getExactPosition(Position position, Set<Position> positions){
        for(Position p : positions){
            if(position.getX() == p.getX() && position.getY() == p.getY()){
                return p;
            }

        }
        return position;
    }

}