package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Controller.ParsingController;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.ScoreRules.CompositionRule;
import it.polimi.ingsw.Model.ScoreRules.NSymbolsRule;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

import static it.polimi.ingsw.Model.Resource.*;

/**
 * The class TUIGraphicGenerator implements all the methods that needs to print Cards, PlayerGrounds, Decks, Resources
 * when using the text user interface.
 * It has a String that simulates a large space to print (it is a String full of spaces) and a String, the ANSI_RESET that
 * resets color and text effect to default.
 * Every method is called from the TUI class.
 *
 * The print of every Card is made like this:
 * the card is divided in five different horizontal layers, and every layer is built individually, with colors, borders,
 * resources.
 * Once all the layers have been done, they are printed using System.out.println().
 *
 * The layers are:
 * - Top border: which contains the two top corners and the top border of the card.
 * - Content line 1: which could contain a resource (in a starter card) or a score rule (in an objective card).
 * - Content line 2: which could contain a resource (in a starter card or in a playable card if flipped) or a score rule
 *                   (in an objective card).
 * - Content line 3: which could contain a resource (in a starter card) or a score rule (in an objective card).
 * - Bottom border: which contains the two bottom corners and the bottom border of the card.
 *
 * To show the available positions in the player ground, a shadow card with a number in its center is displayed.
 * This helps the player selecting in which area they want to place a card, by writing the corresponding number of the
 * shadow card.
 */

public class TUIGraphicGenerator {
    static String ANSI_RESET = "\u001B[0m";
    static String largeSpace = "                                                                                 ";

    /**
     * This method maps the color of a card to its content color in ANSI code.
     *
     * @param card is the card whose resource needs to be mapped.
     * @return the string containing the generated content.
     */
    private static String cardColor(Card card){
        if(card.getId() == -1) return "\u001B[90m";
        switch(card.getColor()){
            case LEAF -> {
                return "\u001B[32m";
            }
            case BUG -> {
                return "\u001B[35m";
            }
            case FOX -> {
                return "\u001B[34m";
            }
            case MUSHROOM -> {
                return "\u001B[31m";
            }
            case null, default -> {
                return "\u001B[33m";
            }
        }
    }

    /**
     * This method maps the Resource of a corner to its correct content in ANSI code.
     *
     * @param corner is the corner whose resources needs to be mapped.
     * @return the string containing the generated content.
     */
    private static String cornerColor(Corner corner){
        if(corner.getAvailability()) {
            switch (corner.getCornerRes()) {
                case LEAF -> {
                    return "\u001B[32m"+"@" + ANSI_RESET;
                }
                case BUG -> {
                    return "\u001B[35m"+"@" + ANSI_RESET;
                }
                case FOX -> {
                    return "\u001B[34m"+"@" + ANSI_RESET;
                }
                case MUSHROOM -> {
                    return "\u001B[31m"+"@" + ANSI_RESET;
                }
                case PLUME -> {
                    return ANSI_RESET+"P";
                }
                case POTION -> {
                    return ANSI_RESET+"T";
                }
                case SCROLL -> {
                    return ANSI_RESET+"S";
                }
                case null, default -> {
                    return ANSI_RESET+"@" + ANSI_RESET;
                }
            }
        }
        return "\u001B[30m"+"@"+ ANSI_RESET;
    }

    /**
     * This method find a Position in the PlayerGround.
     *
     * @param position is the position to find.
     * @param ground is the player ground in which to find the position.
     * @return the key of the found position.
     */
    public static int findPosition(Position position, PlayerGround ground) {
        for (Map.Entry<Integer, Position> entry : ground.getAvailableNumbers().entrySet()) {
            if (entry.getValue().equals(position)) {
                return entry.getKey();
            }
        }
        return 0;
    }

    /**
     * This method generates the Resources of a starter card.
     *
     * @param starterCard is the starter card whose resources are needed.
     * @param state is the number of the resource to get.
     * @return the generated resource.
     */
    private static String generateFirstCardContent(StarterCard starterCard, int state){
        if(!starterCard.getFlip()) {
            if (state == 1) {
                Corner corner = new Corner("top", starterCard.getBackRes().getFirst(), true);
                return "          " + cornerColor(corner) + "         ";
            }
            if (state == 2 && !starterCard.getBackRes().get(1).equals(BLANK)) {
                Corner corner = new Corner("top", starterCard.getBackRes().get(1), true);
                return "          " + cornerColor(corner) + "         ";
            }
            if (state == 3 && !starterCard.getBackRes().get(2).equals(BLANK)) {
                Corner corner = new Corner("top", starterCard.getBackRes().get(2), true);
                return "          " + cornerColor(corner) + "         ";
            }
        }
        return "                    ";

    }

    /**
     * This method is used to actually generates different layers of content of a card, depending on the value of the int
     * state variable:
     * - 1: generates the first layer of content.
     * - 2: generates the second layer of content.
     * - 3: generates the third layer of content.
     * - 4: generates the top or the bottom border, depending on the value of the boolean state.
     *
     * @param card is the card whose content needs to be generated.
     * @param state tells which layer needs to be generated.
     * @param status tells if the card is flipped or not
     * @return the string containing the generated layer of content.
     */
    private static String generateGridFirstCardContent(Card card, int state, boolean status){
        StarterCard starterCard = (StarterCard) card;
        if(state == 1 && !starterCard.getFlip()){
            Corner corner = new Corner("top",starterCard.getBackRes().getFirst(), true);
            return "█        "+ cornerColor(corner) +"        █";
        }
        if(state == 2 && !starterCard.getFlip() && !starterCard.getBackRes().get(1).equals(BLANK)){
            Corner corner = new Corner("top",starterCard.getBackRes().get(1), true);
            return "█        "+ cornerColor(corner) +"        █";
        }
        if(state == 3 && !starterCard.getFlip() && !starterCard.getBackRes().get(2).equals(BLANK)){
            Corner corner = new Corner("top",starterCard.getBackRes().get(2), true);
            return "█        "+ cornerColor(corner) +"        █";
        }
        if(state == 4 && !status){
            return cornerColor(card.getShowedCorners()[0]) + "█████████████████" + ANSI_RESET + cornerColor(card.getShowedCorners()[2]) + ANSI_RESET;
        }
        if(state == 4){
            return cornerColor(card.getShowedCorners()[1]) + "█████████████████" + ANSI_RESET + cornerColor(card.getShowedCorners()[3]) + ANSI_RESET;
        }
        return "█                 █";
    }

    /**
     * This method generates the first layer of content of a card.
     * If the card is a starter one, there is might be a resource to print.
     * Otherwise, only the side borders are generated.
     *
     * @param card is the card needed to generate its layer of content.
     * @return the string containing the generated layer of content.
     */
    private static String generateGridLine1(Card card){
        if(card == null) return "                   ";
        if(card.getId() >= 81 && card.getId() <= 86){
            return generateGridFirstCardContent(card, 1, true);
        }
        return cardColor(card) + "█                 █" + ANSI_RESET;
    }

    /**
     * This method generates the second layer of content of a card.
     * If the card is a starter one, there is might be a resource to print.
     * Otherwise, only the side borders are generated.
     * If the card is not a starter one, but it is flipped, then there is a resource to print.
     * If the card is in an available position to be placed (its id == -1), it is generated a number that helps the player
     * selecting the position in which they want to place their card.
     *
     * @param card is the card needed to generate its layer of content.
     * @param positionX is the x value of the available position.
     * @param positionY is the y value of the available position.
     * @param ground is the player ground in which to search for the position.
     * @return the string containing the generated layer of content.
     */
    private static String generateGridLine2(Card card, int positionX, int positionY, PlayerGround ground){
        if(card == null) return "                   ";
        if(card.getId() >= 81 && card.getId() <= 86){
            return generateGridFirstCardContent(card, 2, true);
        }
        if(card.getId() == -1){
            Position position = new Position(positionX,positionY);
            int value = findPosition(position, ground);
            if(value <10) {
                return cardColor(card) + "█        "+ value +"        █" + ANSI_RESET;
            }
            if(value < 100){
                return cardColor(card) + "█        "+ value +"       █" + ANSI_RESET;
            }
            return cardColor(card) + "█       "+ value +"       █" + ANSI_RESET;
        }
        if (card.getFlip()) {
            return cardColor(card) + "█        @        █" + ANSI_RESET;
        }
        return cardColor(card) + "█                 █" + ANSI_RESET;
    }

    /**
     * This method generates the third layer of content of a card.
     * If the card is a starter one, there is might be a resource to print.
     * Otherwise, only the side borders are generated.
     *
     * @param card is the card needed to generate its layer of content.
     * @return the string containing the generated layer of content.
     */
    private static String generateGridLine3(Card card){
        if(card == null) return "                   ";
        if(card.getId() >= 81 && card.getId() <= 86){
            return generateGridFirstCardContent(card, 3, true);
        }
        if(card.getId() == -1){
            return cardColor(card) + "█                 █" + ANSI_RESET;
        }
        return cardColor(card) + "█                 █" + ANSI_RESET;
    }

    /**
     * This method generates the top border of a card.
     * If the card is a starter one or a playable card, the top corners and the top border are generated.
     * Otherwise, only the top border is generated (this means that the card is a shadow one).
     *
     * @param card is the card needed to generate its layer of content.
     * @return the string containing the generated layer of content.
     */
    private static String generateGridLine4(Card card){
        if(card == null) return "                   ";
        if(card.getId() >= 81 && card.getId() <= 86){
            return generateGridFirstCardContent(card, 4, true);
        }
        if(card.getId() == -1){
            return cardColor(card) + " ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀ " + ANSI_RESET;

        }
        return cornerColor(card.getShowedCorners()[1]) + cardColor(card) + "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀" + ANSI_RESET + cornerColor(card.getShowedCorners()[3]) + ANSI_RESET;
    }

    /**
     * This method generates the bottom border of a card.
     * If the card is a starter one or a playable card, the bottom corners and the bottom border are generated.
     * Otherwise, only the bottom border is generated (this means that the card is a shadow one).
     *
     * @param card is the card needed to generate its layer of content.
     * @return the string containing the generated layer of content.
     */
    private static String generateGridLine5(Card card){
        if(card == null) return "                   ";
        if(card.getId() >= 81 && card.getId() <= 86){
            return generateGridFirstCardContent(card, 4, false);
        }
        if(card.getId() == -1){
            return cardColor(card) + " ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄ " + ANSI_RESET;
        }
        return cornerColor(card.getShowedCorners()[0]) + cardColor(card) + "▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄" + ANSI_RESET + cornerColor(card.getShowedCorners()[2]) + ANSI_RESET;
    }

    /**
     * This method generates the first line of content of a card during the draw phase.
     * It gets the name of the score rule and generates the relative information of that rule.
     *
     * @param card is the card needed to generate its layer of content.
     * @return the string containing the generated layer of content.
     */
    private static String generateHandAndDeckLine1(Card card){
        if(card.getFlip()) return "                   ";
        switch (card.getRule().getName()){
            case "FR" -> {
                int points = card.getRule().getPoints();
                if(points == 0){
                    return "                   ";
                }
                return "     Points: "+ card.getRule().getPoints() +"     ";
            }
            case "CCR" -> {
                return " Covered corners: "+ card.getRule().getPoints() ;
            }

            case "NSR" -> {
                NSymbolsRule rule = (NSymbolsRule) card.getRule();
                String resource = null;
                switch(rule.getSymbol()){
                    case SCROLL -> resource = "S";
                    case PLUME ->  resource = "P";
                    case POTION -> resource = "T";
                }
                return "  Points per "+ resource + ": " + rule.getPoints()+"  ";
            }
            case null, default -> {
                return "                   ";
            }
        }

    }

    /**
     * This method generates the second line of content of a card during the draw phase.
     * If the card is flipped the correct resource is generated.
     *
     * @param card is the card needed to generate its layer of content.
     * @return the string containing the generated layer of content.
     */
    private static String generateHandAndDeckLine2(Card card){
        if(card.getFlip()){
            Corner corner = new Corner("top", card.getColor(), true);
            return "         " + cornerColor(corner) + "         ";
        }
        return "                   ";
    }

    /**
     * This method generates the third line of content of a card during the draw phase.
     * It prints the requirements needed to play a certain card, so it scans among the requirements (that are stored in
     * an HashMap) and generates the correct number needed of each Resource.
     *
     * @param card is the card needed to generate its layer of content.
     * @return the string containing the generated layer of content.
     */
    private static String generateHandAndDeckLine3(PlayableCard card){
        HashMap<Resource, Integer> map = card.getRequirements();
        StringBuilder result = new StringBuilder();
        int times = 0;
        if(card.getFlip()) return  "                   ";
        if(map != null){
            for (Map.Entry<Resource, Integer> entry : map.entrySet()) {
                Resource resource = entry.getKey();
                Corner corner = new Corner("top",resource,true);
                Integer value = entry.getValue();
                if(value != 0) {
                    result.append(cornerColor(corner)).append(" : ").append(value).append(" ");
                    times++;
                }
            }
            if(times == 1){
                return "       "+ result + "      ";
            }
            else if(times == 2){
                return "    "+ result + "   ";
            }
        }
        return "                   ";
    }

    /**
     * This method generates the first layer of content of an objective card.
     * It gets the score rule associated to that card and generates the relatives content.
     *
     * @param objectiveCard is the objective card whose content needs to be generated.
     * @return the string containing the generated layer of content.
     */
    private static String generateObjectiveLine1(ObjectiveCard objectiveCard){
        ScoreRule rule = objectiveCard.getRule();
        switch (rule.getName()){
            case "NSR", "OER" -> {
                return "   " + rule.getPoints() + " Points each   ";
            }
            case "CR" -> {
                CompositionRule compositionRule = (CompositionRule) rule;
                int sumY = compositionRule.getOffsets()[1] + compositionRule.getOffsets()[3];
                int sumX = compositionRule.getOffsets()[0] + compositionRule.getOffsets()[2];
                Resource resource = compositionRule.getResources()[0];
                Corner corner = new Corner("top", resource, true);
                if( (sumX == 3 && sumY == -3) || sumY == -2){
                    return "       " + cornerColor(corner) + "\u001B[33m" + "  " + compositionRule.getPoints() + " points ";
                }
                else if( sumX == 5){
                    return "    " + cornerColor(corner) + "\u001B[33m" + "     " + compositionRule.getPoints() + " points ";
                }
                else if( (sumX == 3 && sumY == 3 )|| sumY == 2){
                    return " " + cornerColor(corner) + "\u001B[33m" + "        "+ compositionRule.getPoints() + " points ";
                }
            }
            case null, default -> {
                return "                   ";
            }
        }
        return "                   ";

    }

    /**
     * This method generates the second layer of content of an objective card.
     * It gets the score rule associated to that card and generates the relatives content.
     *
     * @param objectiveCard is the objective card whose content needs to be generated.
     * @return the string containing the generated layer of content.
     */
    private static String generateObjectiveLine2(ObjectiveCard objectiveCard){
        ScoreRule rule = objectiveCard.getRule();
        switch (rule.getName()){
            case "NSR" -> {
                NSymbolsRule symbolsRule = (NSymbolsRule) rule;
                Resource symbol = symbolsRule.getSymbol();
                Corner corner = new Corner("top", symbol, true);
                String resource = cornerColor(corner);
                if(symbol == PLUME || symbol ==  SCROLL || symbol ==  POTION){
                    return "       " + resource + "   " + resource + "       ";
                }
                return "      " + resource + "  " + resource + "  " + resource + "      ";
            }
            case "OER" -> {
                return ANSI_RESET + "     P   T   S     ";
            }
            case "CR" -> {
                CompositionRule compositionRule = (CompositionRule) rule;
                Resource resource = compositionRule.getResources()[1];
                Corner corner = new Corner("top", resource, true);

                return "    " + cornerColor(corner) + "\u001B[33m" + "        per   ";
            }
            case null, default -> {
                return "";
            }
        }
    }

    /**
     * This method generates the third layer of content of an objective card.
     * It gets the score rule associated to that card and generates the relatives content.
     *
     * @param objectiveCard is the objective card whose content needs to be generated.
     * @return the string containing the generated layer of content.
     */
    private static String generateObjectiveLine3(ObjectiveCard objectiveCard){
        ScoreRule rule = objectiveCard.getRule();
        switch (rule.getName()){
            case "NSR", "OER" -> {
                return "     resources     ";
            }
            case "CR" -> {
                CompositionRule compositionRule = (CompositionRule) rule;
                int sumY = compositionRule.getOffsets()[1] + compositionRule.getOffsets()[3];
                Resource resource = compositionRule.getResources()[2];
                Corner corner = new Corner("top", resource, true);
                if( sumY == -3 || sumY == -1){
                    return " " + cornerColor(corner) + "\u001B[33m" + "           comp  ";
                }
                else if( sumY == 3 || sumY == 1){
                    return "       " + cornerColor(corner) + "\u001B[33m" + "     comp  ";

                }else{
                    return "    " + cornerColor(corner) + "\u001B[33m" + "        comp  ";
                }
            }
            case null, default -> {
                return "                   ";
            }
        }
    }

    /**
     * This method prints a card with its correct corner resources, its resources and its score.
     * Every card is printed with their correct color.
     *
     * @param card the card to print.
     */
    public static void printCard(PlayableCard card){
        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();

        for(int i = 0; i <2; i++) {
            topBorder.append(cornerColor(card.getShowedCorners()[0])).append(cardColor(card)).append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀").append(ANSI_RESET).append(cornerColor(card.getShowedCorners()[2])).append("  ");
            contentLine1.append(cardColor(card)).append("█").append(generateHandAndDeckLine1(card)).append(cardColor(card)).append("█  ");
            contentLine2.append(cardColor(card)).append("█").append(generateHandAndDeckLine2(card)).append(cardColor(card)).append("█  ");
            contentLine3.append(cardColor(card)).append("█").append(generateHandAndDeckLine3(card)).append(cardColor(card)).append("█  ");
            buttonBorder.append(cornerColor(card.getShowedCorners()[1])).append(cardColor(card)).append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄").append(ANSI_RESET).append(cornerColor(card.getShowedCorners()[3])).append("  ");
            card.flipCard();
        }
        System.out.println(largeSpace + topBorder);
        System.out.println(largeSpace + contentLine1);
        System.out.println(largeSpace + contentLine2);
        System.out.println(largeSpace + contentLine3);
        System.out.println(largeSpace + buttonBorder);
    }

    /**
     * This method prints the decks. In other words, it prints the first card of each deck.
     * It gets the first card of a deck, it flips it and then prints it, with the correct corners, resources and color.
     *
     * @param deck1 is the Resource deck whose first card needs to be printed.
     * @param deck2 is the Golden deck whose first card needs to be printed.
     * @param space is a String used to create space between the decks.
     */
    public static void printDecks(Deck deck1, Deck deck2, String space){
        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();

        ArrayList<PlayableCard> cards1 = deck1.getCards();
        cards1.getFirst().flipCard();

        for(int i = 0; i <= 2; i++){
            topBorder.append(cornerColor(cards1.get(i).getShowedCorners()[0])).append(cardColor(cards1.get(i))).append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀").append(ANSI_RESET).append(cornerColor(cards1.get(i).getShowedCorners()[2])).append("  ");
            contentLine1.append(cardColor(cards1.get(i))).append("█").append(generateHandAndDeckLine1(cards1.get(i))).append(cardColor(cards1.get(i))).append("█  ");
            contentLine2.append(cardColor(cards1.get(i))).append("█").append(generateHandAndDeckLine2(cards1.get(i))).append(cardColor(cards1.get(i))).append("█  ");
            contentLine3.append(cardColor(cards1.get(i))).append("█").append(generateHandAndDeckLine3(cards1.get(i))).append(cardColor(cards1.get(i))).append("█  ");
            buttonBorder.append(cornerColor(cards1.get(i).getShowedCorners()[1])).append(cardColor(cards1.get(i))).append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄").append(ANSI_RESET).append(cornerColor(cards1.get(i).getShowedCorners()[3])).append("  ");
        }
        ArrayList<PlayableCard> cards2 = deck2.getCards();
        cards2.getFirst().flipCard();

        topBorder.append(space);
        contentLine1.append(space);
        contentLine2.append(space);
        contentLine3.append(space);
        buttonBorder.append(space);

        for(int i = 0; i <= 2; i++){
            topBorder.append(cornerColor(cards2.get(i).getShowedCorners()[0])).append(cardColor(cards2.get(i))).append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀").append(ANSI_RESET).append(cornerColor(cards2.get(i).getShowedCorners()[2])).append("  ");
            contentLine1.append(cardColor(cards2.get(i))).append("█").append(generateHandAndDeckLine1(cards2.get(i))).append(cardColor(cards2.get(i))).append("█  ");
            contentLine2.append(cardColor(cards2.get(i))).append("█").append(generateHandAndDeckLine2(cards2.get(i))).append(cardColor(cards2.get(i))).append("█  ");
            contentLine3.append(cardColor(cards2.get(i))).append("█").append(generateHandAndDeckLine3(cards2.get(i))).append(cardColor(cards2.get(i))).append("█  ");
            buttonBorder.append(cornerColor(cards2.get(i).getShowedCorners()[1])).append(cardColor(cards2.get(i))).append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄").append(ANSI_RESET).append(cornerColor(cards2.get(i).getShowedCorners()[3])).append("  ");
        }

        System.out.println("                        "+ topBorder);
        System.out.println("                        "+ contentLine1);
        System.out.println("                        "+ contentLine2);
        System.out.println("                        "+ contentLine3);
        System.out.println("                        "+ buttonBorder);
    }

    /**
     * This method prints the PlayerGround of a player, with their available position.
     * It prints the starter card, the objective cards, the secret objective one, the hand and a legend that explain the
     * printed symbols in relation to the Resources.
     *
     * @param playerGround is the PlayerGround to display.
     */
    public static void printGround(PlayerGround playerGround){
        Card[][] ground = playerGround.getGround();
        Set<Position> availablePosition = new HashSet<>(playerGround.getAvailablePositions());
        Set<Position> unavailablePosition = new HashSet<>(playerGround.getUnavailablePositions());
        availablePosition.addAll(unavailablePosition);

        int maxX = availablePosition.stream().mapToInt(Position::getX).max().orElse(0);

        int maxY = availablePosition.stream().mapToInt(Position::getY).max().orElse(0);

        int minX = availablePosition.stream().mapToInt(Position::getX).min().orElse(0);

        int minY = availablePosition.stream().mapToInt(Position::getY).min().orElse(0);

        int lengthX = maxX - minX;
        int lengthY = maxY - minY;

        String contentLine1 = "";
        String contentLine2 = "";
        String contentLine3 = "";
        String contentLine4 = "";
        String space = "";
        
        if(lengthY <=11) {
            for (int i = 0; i <= 3 - lengthY / 2; i++) {
                space += "                   ";
            }
        }
        
        for (int j = lengthY; j >= 0; j--) {
            contentLine4 += generateGridLine5(ground[minX][maxY - j]);
        }
        System.out.println("                   " + space + contentLine4 );
        
        contentLine4 = "";
        
        if(lengthX == 2){
            lengthX++;
        }
        
        for (int i = 0; i <= lengthX; i++) {
            for (int j = lengthY; j >= 0; j--) {

                Card currentCard = ground[minX + i][maxY - j];
                contentLine1 += generateGridLine1(currentCard);
                contentLine2 += generateGridLine2(currentCard, minX + i, maxY - j, playerGround);
                contentLine3 += generateGridLine3(currentCard);

                if (currentCard == null && i+1 <=  lengthX) {
                        contentLine4 += generateGridLine5(ground[minX + i + 1][maxY - j]);

                } else {
                        contentLine4 += generateGridLine4(currentCard);
                }
            }
                System.out.println(printResourceLegend((lengthX-i+1)*4, playerGround.getTotalResources()) + space + contentLine1);
                System.out.println(printResourceLegend((lengthX-i+1)*4-1, playerGround.getTotalResources()) + space + contentLine2);
                System.out.println(printResourceLegend((lengthX-i+1)*4-2, playerGround.getTotalResources()) + space + contentLine3);
                System.out.println(printResourceLegend((lengthX-i+1)*4-3, playerGround.getTotalResources()) + space + contentLine4);
            contentLine1 = "";
            contentLine2 = "";
            contentLine3 = "";
            contentLine4 = "";
        }
    }

    /**
     * This method prints both the hand and the secret objective card of a player, and the common objective cards.
     *
     * @param hand is the hand to print.
     * @param objectiveCards is the list of common objective cards to print.
     */
    public static void printHand(Hand hand, ObjectiveCard[] objectiveCards){

        PlayableCard[] cards = hand.getCards();
        ObjectiveCard objective = hand.getObjCard();

        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();
        //print Hand
        for(int i = 0; i<cards.length; i++){
            if(cards[i] !=null) {
                topBorder.append(cornerColor(cards[i].getShowedCorners()[0])).append(cardColor(cards[i])).append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀").append(ANSI_RESET).append(cornerColor(cards[i].getShowedCorners()[2])).append("  ");
                contentLine1.append(cardColor(cards[i])).append("█").append(generateHandAndDeckLine1(cards[i])).append(cardColor(cards[i])).append("█  ");
                contentLine2.append(cardColor(cards[i])).append("█").append(generateHandAndDeckLine2(cards[i])).append(cardColor(cards[i])).append("█  ");
                contentLine3.append(cardColor(cards[i])).append("█").append(generateHandAndDeckLine3(cards[i])).append(cardColor(cards[i])).append("█  ");
                buttonBorder.append(cornerColor(cards[i].getShowedCorners()[1])).append(cardColor(cards[i])).append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄").append(ANSI_RESET).append(cornerColor(cards[i].getShowedCorners()[3])).append("  ");
            }
        }
        //print Secret Objective
        topBorder.append("\u001B[33m").append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀ ");
        contentLine1.append("\u001B[33m").append("█").append(generateObjectiveLine1(objective)).append("\u001B[33m").append("█ ");
        contentLine2.append("\u001B[33m").append("█").append(generateObjectiveLine2(objective)).append("\u001B[33m").append("█ ");
        contentLine3.append("\u001B[33m").append("█").append(generateObjectiveLine3(objective)).append("\u001B[33m").append("█ ");
        buttonBorder.append("\u001B[33m").append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄ ").append(ANSI_RESET);

        topBorder.append("                                   ");
        contentLine1.append("                                   ");
        contentLine2.append("                                   ");
        contentLine3.append("                                   ");
        buttonBorder.append("                                   ");

        if(objectiveCards != null) {
            for (int i = 0; i < objectiveCards.length; i++) {
                topBorder.append("\u001B[33m").append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀    ");
                contentLine1.append("\u001B[33m").append("█").append(generateObjectiveLine1(objectiveCards[i])).append("\u001B[33m").append("█    ");
                contentLine2.append("\u001B[33m").append("█").append(generateObjectiveLine2(objectiveCards[i])).append("\u001B[33m").append("█    ");
                contentLine3.append("\u001B[33m").append("█").append(generateObjectiveLine3(objectiveCards[i])).append("\u001B[33m").append("█    ");
                buttonBorder.append("\u001B[33m").append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄    ").append(ANSI_RESET);

            }
        }
        // Print the card
        System.out.println("                        " + topBorder);
        System.out.println("                        " + contentLine1);
        System.out.println("                        " + contentLine2);
        System.out.println("                        " + contentLine3);
        System.out.println("                        " + buttonBorder);
    }

    /**
     * This method prints an objective card with its score and score rule.
     *
     * @param objectiveCards the objective card to print.
     */
    public static void printObjectives(ObjectiveCard[] objectiveCards){
        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();

        for(int i = 0; i< objectiveCards.length; i++) {
            topBorder.append("\u001B[33m").append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀    ");
            contentLine1.append("\u001B[33m").append("█").append(generateObjectiveLine1(objectiveCards[i])).append("\u001B[33m").append("█    ");
            contentLine2.append("\u001B[33m").append("█").append(generateObjectiveLine2(objectiveCards[i])).append("\u001B[33m").append("█    ");
            contentLine3.append("\u001B[33m").append("█").append(generateObjectiveLine3(objectiveCards[i])).append("\u001B[33m").append("█    ");
            buttonBorder.append("\u001B[33m").append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄    ").append(ANSI_RESET);

        }

        System.out.println(largeSpace + topBorder);
        System.out.println(largeSpace + contentLine1);
        System.out.println(largeSpace + contentLine2);
        System.out.println(largeSpace + contentLine3);
        System.out.println(largeSpace + buttonBorder);
    }

    /**
     * This method gets the resource legend that associate each symbol to their correct Resource.
     * If the int position is even, it returns a string full of spaces in order to get more space between two rows.
     * Otherwise, depending on the value of the position, it creates a string that contains the name of the corresponding
     * Resource, with their correct color, and their value.
     * The values are read from the HashMap passed as a parameter.
     *
     * @param position is the position in which to read the resources.
     * @param totalResources is the HashMap that contains the values of each Resource.
     * @return such string.
     */
    private static String printResourceLegend(int position, HashMap<Resource, Integer> totalResources){
        if(position % 2 == 0){
            return "                   ";
        }
        switch (position){
            case 15: return "   RESOURCES:      ";
            case 13:{
                int value = totalResources.get(MUSHROOM);
                String string = resourceColor(MUSHROOM) + "   Mushrooms:  " + value + "  " + ANSI_RESET;
                if (value >= 10) {
                    return string;
                }
                return string + " ";
            }
            case 11:{
                int value = totalResources.get(LEAF);
                String string = resourceColor(LEAF) +"   Plants:  "+ value +"     " + ANSI_RESET;
                if (value >= 10) {
                    return  string;
                }
                return string + " ";
            }
            case 9:{
                int value = totalResources.get(BUG);
                String string = resourceColor(BUG) +"   Bugs:  " + value + "       " + ANSI_RESET;
                if (value >= 10) {
                    return  string;
                }
                return string + " ";
            }
            case 7:{
                int value = totalResources.get(FOX);
                String string = resourceColor(FOX) +"   Animals:  "+ value +"    "+ ANSI_RESET;
                if (value >= 10) {
                    return  string;
                }
                return string + " ";
            }
            case 5:{
                int value = totalResources.get(PLUME);
                String string = resourceColor(PLUME) +"   Plumes (P):  " + value + "  " + ANSI_RESET;
                if (value >= 10) {
                    return  string;
                }
                return string + " ";
            }
            case 3:{
                int value = totalResources.get(SCROLL);
                String string = resourceColor(SCROLL) +"   Scrolls (S):  " + value + ANSI_RESET;
                if (value >= 10) {
                    return  string;
                }
                return string + " ";
            }
            case 1:{
                int value = totalResources.get(POTION);
                String string = resourceColor(POTION) +"   Potions (T):  " + value + ANSI_RESET;
                if (value >= 10) {
                    return string;
                }
                return string + " ";
            }
            default: return "                   ";
        }
}

    /**
     * This method prints the rooms available with their participants and their total number of player allowed.
     * This it printed every time a player selects they want to join another room.
     *
     * @param rooms is the list of rooms already created.
     */
    public static void printRoomsTable(ArrayList<Room> rooms) {
        int maxNameLength = "Room Name".length();
        int maxParticipantsDisplayLength = "Participants".length();

        for (Room room : rooms) {
            if (room.getName().length() > maxNameLength) {
                maxNameLength = room.getName().length();
            }
            String participantsDisplay = room.getGame().getPlayers().size() + "/" + room.getGame().getExpPlayers();
            if (participantsDisplay.length() > maxParticipantsDisplayLength) {
                maxParticipantsDisplayLength = participantsDisplay.length();
            }
        }
        System.out.printf("%-" + maxNameLength + "s | %" + maxParticipantsDisplayLength + "s%n", "Room Name", "Participants");
        System.out.println("-".repeat(maxNameLength) + "-+-" + "-".repeat(maxParticipantsDisplayLength));
        for (Room room : rooms) {
            String participantsDisplay = room.getGame().getPlayers().size() + "/" + room.getGame().getExpPlayers();
            System.out.printf("%-" + maxNameLength + "s | %" + maxParticipantsDisplayLength + "s%n", room.getName(), participantsDisplay);
        }
        System.out.println("\n");
    }

    /**
     * This method prints a starter card with its correct corner resources and its resources.
     *
     * @param card the starter card to print.
     */
    public static void printStartingCard(StarterCard card){

        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();

        for(int i =0; i<2; i++) {
            topBorder.append(cornerColor(card.getShowedCorners()[0])).append(ANSI_RESET).append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀").append(ANSI_RESET).append(cornerColor(card.getShowedCorners()[2])).append("  ");
            contentLine1.append(ANSI_RESET).append("█").append(generateFirstCardContent(card, 1)).append(ANSI_RESET).append("█  ");
            contentLine2.append(ANSI_RESET).append("█").append(generateFirstCardContent(card, 2)).append(ANSI_RESET).append("█  ");
            contentLine3.append(ANSI_RESET).append("█").append(generateFirstCardContent(card, 3)).append(ANSI_RESET).append("█  ");
            buttonBorder.append(cornerColor(card.getShowedCorners()[1])).append(ANSI_RESET).append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄").append(ANSI_RESET).append(cornerColor(card.getShowedCorners()[3])).append("  ");
            card.flipCard();
        }
        card.flipCard();
        System.out.println(largeSpace + topBorder);
        System.out.println(largeSpace + contentLine1);
        System.out.println(largeSpace + contentLine2);
        System.out.println(largeSpace + contentLine3);
        System.out.println(largeSpace + buttonBorder);
    }

    /**
     * This method maps the Resource to its correct content in ANSI code.
     *
     * @param resource is the resource that needs to be mapped.
     * @return the string containing the generated content.
     */
    private static String resourceColor(Resource resource){
        switch(resource){
            case LEAF -> {
                return "\u001B[32m";
            }
            case BUG -> {
                return "\u001B[35m";
            }
            case FOX -> {
                return "\u001B[34m";
            }
            case MUSHROOM -> {
                return "\u001B[31m";
            }
            case null, default -> {
                return "\u001B[33m";
            }
        }
    }

}
