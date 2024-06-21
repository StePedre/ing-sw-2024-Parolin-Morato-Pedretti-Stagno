package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * The PlayerGround class represents the table of a player in a game session. In Player class, each player is
 * associated with a PlayerGround. It contains the current player's score and all the cards played on the ground,
 * in a matrix where each Card is mapped to a Position (made of two coordinates, x and y).
 * There is a map of Integer and Position that is used to link the available position to a specific Integer (used in TUI).
 * There are also a set of available positions and a set of unavailable ones.
 * The PlayerGround also counts the total number of resource of each type and has a variable used to calculate
 * covered corners (which means a new Card cannot be attached there).
 * It keeps track of the last position in which a Card is played, and of the moves played during a game.
 */

public class PlayerGround implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private int playerScore;
    private final Map<Position, Card> cardPosition;

    private final Map<Integer, Position> availableNumbers;
    private final Card[][] ground;
    private final Set<Position> availablePositions;
    private final Set<Position> unavailablePositions;
    private final HashMap<Resource, Integer> totalResources;
    private Position lastPositionPlaced;
    private final Card availabilityCard;

    private final ArrayList<Move> moves = new ArrayList<>();

    /**
     * Class constructor without parameters.
     * It initializes player's score to 0.
     * It creates a new squared Card matrix (the ground) with the maximum possible dimension.
     * It creates a new hash map for resource counting and two new hash sets for available and unavailable positions,
     * initializing resources map and positions sets by calling two auxiliary methods.
     * It creates a new hash map to link the available position to a specific Integer.
     * The availability Card is set to have an id of -1.
     */
    public PlayerGround(){
        playerScore = 0;
        cardPosition = new HashMap<>();
        ground = new Card[84][84];
        unavailablePositions = new HashSet<>();
        availablePositions = new HashSet<>();
        availableNumbers = new HashMap<>();
        initializePosition();

        totalResources = new HashMap<>();
        initializeResources();
        availabilityCard = new PlayableCard(-1,null,null,null,null,null);
    }

    /**
     * The method adds a position passed as a parameter to the availableNumber list. This list works as the list of
     * available position, but it links every position to an Integer. This helps the player selecting the position when
     * using the TUI.
     * It checks if the position is already contained, otherwise find the lowest available key and then put the position
     * in the list linked to that lowestAvailableKey.
     *
     * @param position is the position to be added to the list.
     */
    private void addAvailableNumber(Position position) {
        if (availableNumbers.containsValue(position)) {
            return;
        }
        int lowestAvailableKey = 1;
        while (availableNumbers.containsKey(lowestAvailableKey)) {
            lowestAvailableKey++;
        }
        availableNumbers.put(lowestAvailableKey, position);
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
        removeCornersResources(position, newCard);
        addCornersResources(newCard);
        updatePositionsAvailability(newCard,position);
        if(!newCard.getFlip()){
        raiseScore(newCard.getRule());
        }
        ground[position.getX()][position.getY()] = newCard;
        cardPosition.put(position, newCard);
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
        int[] count = {0};
        Set<Position> positions = cardPosition.keySet();
        // For each position occupied by cards, do the following:
        positions.forEach(p -> {
            // Checks if the current position has a card with a color equal to the first color of the composition and
            // Checks if the current position is already inside takenPositions
            if (ground[p.getX()][p.getY()] != null && ground[p.getX()][p.getY()].getColor() == colors[0] && !takenPositions.contains(p)) {
                // Calculates the nex position thanks to the offsets of the composition
                int secondX = p.getX() + offSets[0].getX();
                int secondY = p.getY() + offSets[0].getY();
                Position secondPosition = new Position(secondX, secondY);
                // Checks if the second position has a card with a color equal to the second color of the composition and
                // Checks if the second position is already inside takenPositions
                if (ground[secondX][secondY] != null && ground[secondX][secondY].getColor() == colors[1] && !takenPositions.contains(secondPosition)) {
                    int thirdX = p.getX() + offSets[1].getX();
                    int thirdY = p.getY() + offSets[1].getY();
                    Position thirdPosition = new Position(thirdX, thirdY);
                    if (ground[thirdX][thirdY] != null && ground[thirdX][thirdY].getColor() == colors[2] && !takenPositions.contains(thirdPosition)) {
                        // Composition found; increments the atomicInteger and saves the 3 positions found into takenPositions
                        count[0]+=1;
                        takenPositions.add(p);
                        takenPositions.add(secondPosition);
                        takenPositions.add(thirdPosition);
                    }
                }
            }
        });
        return count[0];
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
                if (card != null && card.getId()!=-1) {
                    count++;
                }

            }
        }
        return count;
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
    public boolean checkRequirements(PlayableCard card){
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
     * The method gets the map of Integer and Position that links available positions to Integers.
     *
     * @return the map said before.
     */
    public Map<Integer, Position> getAvailableNumbers(){ return availableNumbers;}

    /**
     * The method gets the set of available positions where it's possible to place a card.
     *
     * @return set of available Positions.
     */
    public Set<Position> getAvailablePositions() {
        return availablePositions;
    }

    /**
     * The method gets the whole ground, which is a matrix of Cards. Some cells may be without a value if there is no element
     * of the Position-Card map that matches cell coordinates with a card.
     *
     * @return the Player's ground.
     */
    public Card[][] getGround() {
        return ground;
    }

    /**
     * The method gets the list of Move played during a game.
     *
     * @return the list of Move.
     */
    public ArrayList<Move> getMoves(){
        return moves;
    }

    /**
     * The method gets the score (a number) of the player associated with this PlayerGround.
     *
     * @return the Player's score.
     */
    public int getPlayerScore() {
        return playerScore;
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
     * The method return the Set of unavailable position
     *
     * @return the Set of unavailable position
     */
    public Set<Position> getUnavailablePositions() {
        return unavailablePositions;
    }

    /**
     * The method initialize the set of available positions with the first card, put in the center of the matrix.
     */
    private void initializePosition(){
        Position initialPosition = new Position(42,42);
        availablePositions.add(initialPosition);
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
        if (!availablePositions.contains(position)) {
            throw new InvalidPositionException("Invalid position");
        }
        if(!starterCard.getFlip()){
            updateMultipleResources(starterCard.getBackRes());
        }
        addCard(starterCard, position);
        moves.add(new Move(starterCard,position));

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
        if (!checkRequirements(playableCard) && !playableCard.getFlip()) {
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
        moves.add(new Move(playableCard,position));
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

    /**
     * The method removes the position passed as a parameter from the availableNumbers list.
     * Firstly it finds the key linked to that position.
     * Then, if the key is not null, proceeds to remove it.
     *
     * @param position is the position to remove from the list.
     */
    private void removeAvailableNumber(Position position) {
        Integer keyToRemove = null;
        for (Map.Entry<Integer, Position> entry : availableNumbers.entrySet()) {
            if (entry.getValue().equals(position)) {
                keyToRemove = entry.getKey();
                break;
            }
        }
        if (keyToRemove != null) {
            availableNumbers.remove(keyToRemove);
        }
    }

    /**
     * This method
     */
    public void updateMissingNumbers() {
        if (availableNumbers == null || availableNumbers.isEmpty()) {
            return;
        }
        boolean missingNumbersExist = true;

        while (missingNumbersExist) {
            missingNumbersExist = false;
            List<Integer> keys = new ArrayList<>(availableNumbers.keySet());
            Collections.sort(keys);
            int missingNumber = -1;
            for (int i = 0; i < keys.size() - 1; i++) {
                if (keys.get(i + 1) != keys.get(i) + 1) {
                    missingNumber = keys.get(i) + 1;
                    break;
                }
            }
            if (missingNumber != -1) {
                int highestNumber = keys.getLast();
                Position highestPosition = availableNumbers.get(highestNumber);
                availableNumbers.put(missingNumber, highestPosition);
                availableNumbers.remove(highestNumber);
                missingNumbersExist = true;
            }
        }
    }



    /**
     * The method is used to remove from the total resource count the resource on a card's corner (previously placed)
     * covered by a card added in the position passed as a parameter. For each of the four possible positions around
     * the placePosition, it gets the corresponding card and, if not null, it calculates the index of the
     * corner that will be covered if a card is placed in the placePosition.
     * Finally, it gets that corner resource (if present) and updates the resource count.
     *
     * @param placePosition is where the new card has been placed, used to calculate which corner has been covered.
     * @param newCard is the Card that is played in that placePosition.
     */
    private void removeCornersResources(Position placePosition, Card newCard){
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                // Calculates one of the 4 position around the placePosition and gets the corresponding card
                int newX = placePosition.getX() + calculateOffset(i);
                int newY = placePosition.getY() + calculateOffset(j);
                Card card = ground[newX][newY];
                if (card != null && card.getId() != -1) {
                    // Calculates the index of the corresponding corner that is covered if you place the card in the placePosition
                    int newCornerPos = i + 2 * j;
                    int cornerPos = 3 - (i + 2 * j);

                    Resource resourceToRemove = card.getShowedCorners()[cornerPos].getCornerRes();
                    card.getShowedCorners()[cornerPos].setAvailable(false);
                    updateSingleResource(-1, resourceToRemove);


                }

            }
        }
    }

    /**
     * The method set the player score.
     *
     * @param i the value of the score of the player to be set.
     */
    public void setPlayerScore(int i) { this.playerScore=i;}

    /**
     * The method increases by one more than one resource type at the same time.
     *
     * @param resources is the list of resources that needs to be incremented.
     */
    private void updateMultipleResources(ArrayList<Resource> resources){
        resources.forEach(r -> updateSingleResource(1,r));
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
        removeAvailableNumber(placePosition);
        // Removes the current position from the availablePositions
        availablePositions.remove(placePosition);
        unavailablePositions.add(placePosition);
        lastPositionPlaced = placePosition;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                // Calculates one of the 4 position around the placePosition
                int newX = placePosition.getX() + calculateOffset(i);
                int newY = placePosition.getY() + calculateOffset(j);
                Position newPosition = new Position(newX, newY);
                // checks if the ground is free in that position and if is not unavailable
                if ((ground[newX][newY] == null || ground[newX][newY].getId() ==-1) && !unavailablePositions.contains(newPosition)) {
                    // calculates the index of the corresponding covering corner and checks its availability
                    int cornerPosition = i + 2 * j;
                    if (card.getShowedCorners()[cornerPosition].getAvailability()) {
                        availablePositions.add(newPosition);
                        addAvailableNumber(newPosition);
                        ground[newX][newY] = availabilityCard;
                    } else {
                        // if the corner is not available, the newPosition becomes unavailable
                        unavailablePositions.add(newPosition);
                        availablePositions.remove(newPosition);
                        removeAvailableNumber(newPosition);
                        ground[newX][newY] = null;
                    }
                }

            }
        }
        updateMissingNumbers();
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

}