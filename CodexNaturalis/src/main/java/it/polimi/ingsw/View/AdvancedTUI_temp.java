package it.polimi.ingsw.View;

import it.polimi.ingsw.Controller.ParsingController;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.ScoreRules.CompositionRule;
import it.polimi.ingsw.Model.ScoreRules.NSymbolsRule;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

import static it.polimi.ingsw.Model.Resource.*;

public class AdvancedTUI_temp {

    public static void printGround(PlayerGround playerGround){
        Card[][] ground = playerGround.getGround();
        Set<Position> availablePosition = new HashSet<>(playerGround.getAvailablePositions());
        Set<Position> unavailablePosition = new HashSet<>(playerGround.getUnavailablePositions());
        availablePosition.addAll(unavailablePosition);

        /*Map<Integer, Position> numbers = playerGround.getAvailableNumbers();

        for (Map.Entry<Integer, Position> entry : numbers.entrySet()) {
            Integer number = entry.getKey();
            Position position = entry.getValue();
            System.out.println("Number: " + number + ", Position: X: " + position.getX() + " Y: " + position.getY());
        }*/

        int maxX = availablePosition.stream()
                .mapToInt(Position::getX)
                .max()
                .orElse(0);

        int maxY = availablePosition.stream()
                .mapToInt(Position::getY)
                .max()
                .orElse(0);

        int minX = availablePosition.stream()
                .mapToInt(Position::getX)
                .min()
                .orElse(0);

        int minY = availablePosition.stream()
                .mapToInt(Position::getY)
                .min()
                .orElse(0);

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
            contentLine4 += getGridLine5(ground[minX][maxY - j], false);
        }
        System.out.println("                   " + space + contentLine4 );

        contentLine4 = "";



        if(lengthX == 2){
            lengthX++;
        }
        for (int i = 0; i <= lengthX; i++) {
            for (int j = lengthY; j >= 0; j--) {

                Card currentCard = ground[minX + i][maxY - j];
                contentLine1 += getGridLine1(currentCard);
                contentLine2 += getGridLine2(currentCard, minX + i, maxY - j, playerGround);
                contentLine3 += getGridLine3(currentCard);

                if (currentCard == null && i+1 <=  lengthX) {
                        contentLine4 += getGridLine5(ground[minX + i + 1][maxY - j], false);

                } else {
                        contentLine4 += getGridLine4(currentCard, true);
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


private static String printResourceLegend(int position, HashMap<Resource, Integer> totalResources){
        if(position % 2 == 0){
            return "                   ";
        }
        switch (position){
            case 15: return "   RESOURCES:      ";
            case 13:{
                int value = totalResources.get(MUSHROOM);
                String string = resourceColor(MUSHROOM) + "   Mushrooms:  " + value + "  " + "\u001B[0m";
                if (value >= 10) {
                    return string;
                }
                return string + " ";
            }
            case 11:{
                int value = totalResources.get(LEAF);
                String string = resourceColor(LEAF) +"   Plants:  "+ value +"     " + "\u001B[0m";
                if (value >= 10) {
                    return  string;
                }
                return string + " ";
            }
            case 9:{
                int value = totalResources.get(BUG);
                String string = resourceColor(BUG) +"   Bugs:  " + value + "       " + "\u001B[0m";
                if (value >= 10) {
                    return  string;
                }
                return string + " ";
            }
            case 7:{
                int value = totalResources.get(FOX);
                String string = resourceColor(FOX) +"   Animals:  "+ value +"    "+ "\u001B[0m";
                if (value >= 10) {
                    return  string;
                }
                return string + " ";
            }
            case 5:{
                int value = totalResources.get(PLUME);
                String string = resourceColor(PLUME) +"   Plumes:  " + value + "      " + "\u001B[0m";
                if (value >= 10) {
                    return  string;
                }
                return string + " ";
            }
            case 3:{
                int value = totalResources.get(SCROLL);
                String string = resourceColor(SCROLL) +"   Scrolls:  " + value + "    " + "\u001B[0m";
                if (value >= 10) {
                    return  string;
                }
                return string + " ";
            }
            case 1:{
                int value = totalResources.get(POTION);
                String string = resourceColor(POTION) +"   Potions:  " + value +"    " + "\u001B[0m";
                if (value >= 10) {
                    return string;
                }
                return string + " ";
            }
            default: return "                   ";
        }
}


    public static void printCard(PlayableCard card){
        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();

        for(int i = 0; i <2; i++) {
            topBorder.append(cornerColor(card.getShowedCorners()[0])).append(cardColor(card)).append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀").append("\u001B[0m").append(cornerColor(card.getShowedCorners()[2])).append("  ");
            contentLine1.append(cardColor(card)).append("█").append(cardContent(card)).append(cardColor(card)).append("█  ");
            contentLine2.append(cardColor(card)).append("█").append(cardResource(card)).append(cardColor(card)).append("█  ");
            contentLine3.append(cardColor(card)).append("█").append(cardRequirements(card)).append(cardColor(card)).append("█  ");
            buttonBorder.append(cornerColor(card.getShowedCorners()[1])).append(cardColor(card)).append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄").append("\u001B[0m").append(cornerColor(card.getShowedCorners()[3])).append("  ");
        card.flipCard();
        }
        System.out.println("                                                                                 " + topBorder);
        System.out.println("                                                                                 " + contentLine1);
        System.out.println("                                                                                 " + contentLine2);
        System.out.println("                                                                                 " + contentLine3);
        System.out.println("                                                                                 " + buttonBorder);
    }

    public static void printDecks(Deck deck1, Deck deck2, String space){
        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();

        String ANSI_RESET = "\u001B[0m";

        ArrayList<PlayableCard> cards1 = deck1.getCards();
        cards1.getFirst().flipCard();

        for(int i = 0; i <= 2; i++){
            topBorder.append(cornerColor(cards1.get(i).getShowedCorners()[0])).append(cardColor(cards1.get(i))).append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀").append(ANSI_RESET).append(cornerColor(cards1.get(i).getShowedCorners()[2])).append("  ");
            contentLine1.append(cardColor(cards1.get(i))).append("█").append(cardContent(cards1.get(i))).append(cardColor(cards1.get(i))).append("█  ");
            contentLine2.append(cardColor(cards1.get(i))).append("█").append(cardResource(cards1.get(i))).append(cardColor(cards1.get(i))).append("█  ");
            contentLine3.append(cardColor(cards1.get(i))).append("█").append(cardRequirements(cards1.get(i))).append(cardColor(cards1.get(i))).append("█  ");
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
            contentLine1.append(cardColor(cards2.get(i))).append("█").append(cardContent(cards2.get(i))).append(cardColor(cards2.get(i))).append("█  ");
            contentLine2.append(cardColor(cards2.get(i))).append("█").append(cardResource(cards2.get(i))).append(cardColor(cards2.get(i))).append("█  ");
            contentLine3.append(cardColor(cards2.get(i))).append("█").append(cardRequirements(cards2.get(i))).append(cardColor(cards2.get(i))).append("█  ");
            buttonBorder.append(cornerColor(cards2.get(i).getShowedCorners()[1])).append(cardColor(cards2.get(i))).append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄").append(ANSI_RESET).append(cornerColor(cards2.get(i).getShowedCorners()[3])).append("  ");
        }

        System.out.println("                        "+ topBorder);
        System.out.println("                        "+ contentLine1);
        System.out.println("                        "+ contentLine2);
        System.out.println("                        "+ contentLine3);
        System.out.println("                        "+ buttonBorder);
    }



    public static void printObjectives(ObjectiveCard[] objectiveCards){
        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();

        for(int i = 0; i< objectiveCards.length; i++) {
            topBorder.append("\u001B[33m").append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀    ");
            contentLine1.append("\u001B[33m").append("█").append(firstLineObjective(objectiveCards[i])).append("\u001B[33m").append("█    ");
            contentLine2.append("\u001B[33m").append("█").append(secondLineObjective(objectiveCards[i])).append("\u001B[33m").append("█    ");
            contentLine3.append("\u001B[33m").append("█").append(thirdLineObjective(objectiveCards[i])).append("\u001B[33m").append("█    ");
            buttonBorder.append("\u001B[33m").append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄    ").append("\u001B[0m");

        }

        System.out.println("                                                                                 " + topBorder);
        System.out.println("                                                                                 " + contentLine1);
        System.out.println("                                                                                 " + contentLine2);
        System.out.println("                                                                                 " + contentLine3);
        System.out.println("                                                                                 " + buttonBorder);
}

    public static void printStartingCard(StarterCard card){

        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();

        String ANSI_RESET = "\u001B[0m";
        for(int i =0; i<2; i++) {
            topBorder.append(cornerColor(card.getShowedCorners()[0])).append("\u001B[0m").append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀").append(ANSI_RESET).append(cornerColor(card.getShowedCorners()[2])).append("  ");
            contentLine1.append("\u001B[0m").append("█").append(firstCardContent(card, 1)).append("\u001B[0m").append("█  ");
            contentLine2.append("\u001B[0m").append("█").append(firstCardContent(card, 2)).append("\u001B[0m").append("█  ");
            contentLine3.append("\u001B[0m").append("█").append(firstCardContent(card, 3)).append("\u001B[0m").append("█  ");
            buttonBorder.append(cornerColor(card.getShowedCorners()[1])).append("\u001B[0m").append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄").append(ANSI_RESET).append(cornerColor(card.getShowedCorners()[3])).append("  ");
            card.flipCard();
        }
        card.flipCard();
        System.out.println("                                                                                 " + topBorder);
        System.out.println("                                                                                 " + contentLine1);
        System.out.println("                                                                                 " + contentLine2);
        System.out.println("                                                                                 " + contentLine3);
        System.out.println("                                                                                 " + buttonBorder);
    }
    private static String firstCardContent(StarterCard starterCard, int state){
        if(!starterCard.getFlip()) {
            if (state == 1) {
                Corner corner = new Corner("top", starterCard.getBackRes().getFirst(), true);
                return "          " + cornerColor(corner) + "         ";
            }
            if (state == 2 && !starterCard.getBackRes().get(1).equals(BLANK)) {
                //return "█    first card    █
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
    public static void printHand(Hand hand, ObjectiveCard[] objectiveCards){

        PlayableCard[] cards = hand.getCards();
        ObjectiveCard objective = hand.getObjCard();

        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();

        String ANSI_RESET = "\u001B[0m";


        for(int i = 0; i<cards.length; i++){
            if(cards[i] !=null) {
                topBorder.append(cornerColor(cards[i].getShowedCorners()[0])).append(cardColor(cards[i])).append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀").append(ANSI_RESET).append(cornerColor(cards[i].getShowedCorners()[2])).append("  ");
                contentLine1.append(cardColor(cards[i])).append("█").append(cardContent(cards[i])).append(cardColor(cards[i])).append("█  ");
                contentLine2.append(cardColor(cards[i])).append("█").append(cardResource(cards[i])).append(cardColor(cards[i])).append("█  ");
                contentLine3.append(cardColor(cards[i])).append("█").append(cardRequirements(cards[i])).append(cardColor(cards[i])).append("█  ");
                buttonBorder.append(cornerColor(cards[i].getShowedCorners()[1])).append(cardColor(cards[i])).append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄").append(ANSI_RESET).append(cornerColor(cards[i].getShowedCorners()[3])).append("  ");
            }
        }
        //print Secret Objective
        topBorder.append("\u001B[33m").append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀ ");
        contentLine1.append("\u001B[33m").append("█").append(firstLineObjective(objective)).append("\u001B[33m").append("█ ");
        contentLine2.append("\u001B[33m").append("█").append(secondLineObjective(objective)).append("\u001B[33m").append("█ ");
        contentLine3.append("\u001B[33m").append("█").append(thirdLineObjective(objective)).append("\u001B[33m").append("█ ");
        buttonBorder.append("\u001B[33m").append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄ ").append("\u001B[0m");

        topBorder.append("                                   ");
        contentLine1.append("                                   ");
        contentLine2.append("                                   ");
        contentLine3.append("                                   ");
        buttonBorder.append("                                   ");

        if(objectiveCards != null) {
            for (int i = 0; i < objectiveCards.length; i++) {
                topBorder.append("\u001B[33m").append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀    ");
                contentLine1.append("\u001B[33m").append("█").append(firstLineObjective(objectiveCards[i])).append("\u001B[33m").append("█    ");
                contentLine2.append("\u001B[33m").append("█").append(secondLineObjective(objectiveCards[i])).append("\u001B[33m").append("█    ");
                contentLine3.append("\u001B[33m").append("█").append(thirdLineObjective(objectiveCards[i])).append("\u001B[33m").append("█    ");
                buttonBorder.append("\u001B[33m").append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄    ").append("\u001B[0m");

            }
        }
        // Print the card
        System.out.println("                        " + topBorder);
        System.out.println("                        " + contentLine1);
        System.out.println("                        " + contentLine2);
        System.out.println("                        " + contentLine3);
        System.out.println("                        " + buttonBorder);
    }

    private static String getGridLine1(Card card){
        if(card == null) return "                  ";
        if(card.getId() >= 81 && card.getId() <= 86){
            return getFirstGridLine(card, 1, true);
        }
        if(card.getId() == -1){
            return cardColor(card) + "█                 █" + "\u001B[0m";
        }
        return cardColor(card) + "█                 █" + "\u001B[0m";
    }
    private static String getGridLine2(Card card, int positionX, int positionY, PlayerGround ground){
        if(card == null) return "                  ";
        if(card.getId() >= 81 && card.getId() <= 86){
            return getFirstGridLine(card, 2, true);
        }
        if(card.getId() == -1){
            Position position = new Position(positionX,positionY);
            int value = findPosition(position, ground);
            if(value <10) {
                return cardColor(card) + "█        "+ value +"        █" + "\u001B[0m";
            }
            if(value < 100){
                return cardColor(card) + "█        "+ value +"       █" + "\u001B[0m";
            }
            return cardColor(card) + "█       "+ value +"       █" + "\u001B[0m";
        }

        return cardColor(card) + "█                 █" + "\u001B[0m";
    }
    private static String getGridLine3(Card card){
        if(card == null) return "                  ";
        if(card.getId() >= 81 && card.getId() <= 86){
            return getFirstGridLine(card, 3, true);
        }
        if(card.getId() == -1){
            return cardColor(card) + "█                 █" + "\u001B[0m";
        }
        return cardColor(card) + "█                 █" + "\u001B[0m";
    }
    private static String getGridLine4(Card card, boolean state){
        if(card == null) return "                  ";
        if(card.getId() >= 81 && card.getId() <= 86){
            return getFirstGridLine(card, 4, state);
        }
        if(card.getId() == -1){
            return cardColor(card) + " ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀ " + "\u001B[0m";

        }
        if (state) {
            return cornerColor(card.getShowedCorners()[1]) + cardColor(card) + "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀" + "\u001B[0m" + cornerColor(card.getShowedCorners()[3]) + "\u001B[0m";
        }
        return cornerColor(card.getShowedCorners()[0]) + cardColor(card) + "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀" + "\u001B[0m" + cornerColor(card.getShowedCorners()[2]) + "\u001B[0m";

    }

    private static String getGridLine5(Card card, boolean state){
        if(card == null) return "                  ";
        if(card.getId() >= 81 && card.getId() <= 86){
            return getFirstGridLine(card, 4, state);
        }
        if(card.getId() == -1){
            return cardColor(card) + " ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄ " + "\u001B[0m";
        }
        if (state) {
            return cornerColor(card.getShowedCorners()[1]) + cardColor(card) + "▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄" + "\u001B[0m" + cornerColor(card.getShowedCorners()[3]) + "\u001B[0m";
        }
        return cornerColor(card.getShowedCorners()[0]) + cardColor(card) + "▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄" + "\u001B[0m" + cornerColor(card.getShowedCorners()[2]) + "\u001B[0m";


    }


    private static String getFirstGridLine(Card card, int state, boolean status){
        StarterCard starterCard = (StarterCard) card;
        if(state == 1 && !starterCard.getFlip()){
            Corner corner = new Corner("top",starterCard.getBackRes().getFirst(), true);
            return " █        "+ cornerColor(corner) +"       █";
        }
        if(state == 2 && !starterCard.getFlip() && !starterCard.getBackRes().get(1).equals(BLANK)){
            //return "█    first card    █
            Corner corner = new Corner("top",starterCard.getBackRes().get(1), true);
            return " █        "+ cornerColor(corner) +"       █";
        }
        if(state == 3 && !starterCard.getFlip() && !starterCard.getBackRes().get(2).equals(BLANK)){
            Corner corner = new Corner("top",starterCard.getBackRes().get(2), true);
            return " █        "+ cornerColor(corner) +"       █";
        }
        if(state == 4 && !status){
            return cornerColor(card.getShowedCorners()[0]) + "████████████████" + "\u001B[0m" + cornerColor(card.getShowedCorners()[2]) + "\u001B[0m";
        }
        if(state == 4){
            return cornerColor(card.getShowedCorners()[1]) + "████████████████" + "\u001B[0m" + cornerColor(card.getShowedCorners()[3]) + "\u001B[0m";
        }
        return " █                █";
    }

    private static String firstLineObjective(ObjectiveCard objectiveCard){
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
                if( sumX == -3){
                    return "       " + cornerColor(corner) + "\u001B[33m" + "  " + compositionRule.getPoints() + " points ";
                }
                else if( sumX == -1 || sumX == 1){
                    return "    " + cornerColor(corner) + "\u001B[33m" + "     " + compositionRule.getPoints() + " points ";
                }
                else if( sumX == 3){
                    return " " + cornerColor(corner) + "\u001B[33m" + "        "+ compositionRule.getPoints() + " points ";
                }
                else if(compositionRule.getOffsets()[0] ==-1){
                    return "       " + cornerColor(corner) + "\u001B[33m" + "  " + compositionRule.getPoints() + " points ";
                }
                else if(compositionRule.getOffsets()[2] == 1){
                    return " " + cornerColor(corner) + "\u001B[33m" + "        "+ compositionRule.getPoints() + " points ";
                }
            }
            case null, default -> {
                return "                   ";
            }
        }
        return "                   ";

    }

    private static String secondLineObjective(ObjectiveCard objectiveCard){
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
                return "\u001B[0m" + "     P   T   S     ";
            }
            case "CR" -> {
                CompositionRule compositionRule = (CompositionRule) rule;
                int sumY = compositionRule.getOffsets()[1] + compositionRule.getOffsets()[3];
                int sumX = compositionRule.getOffsets()[0] + compositionRule.getOffsets()[2];
                Resource resource = compositionRule.getResources()[1];
                Corner corner = new Corner("top", resource, true);

                    return "    " + cornerColor(corner) + "\u001B[33m" + "        per   ";
            }
            case null, default -> {
                return "";
            }
        }
    }

    private static String thirdLineObjective(ObjectiveCard objectiveCard){
        ScoreRule rule = objectiveCard.getRule();
        switch (rule.getName()){
            case "NSR", "OER" -> {
                return "     resources     ";
            }
            case "CR" -> {
                CompositionRule compositionRule = (CompositionRule) rule;
                int sumY = compositionRule.getOffsets()[1] + compositionRule.getOffsets()[3];
                int sumX = compositionRule.getOffsets()[0] + compositionRule.getOffsets()[2];
                Resource resource = compositionRule.getResources()[2];
                Corner corner = new Corner("top", resource, true);
                if( sumX == -3 || sumX == -1){
                    return " " + cornerColor(corner) + "\u001B[33m" + "           comp  ";
                }
                else if( sumX == 3 || sumX == 1){
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
    private static String cardContent(Card card){
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

    private static String cardRequirements(PlayableCard card){
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
            if(times ==1){
                return "       "+ result.toString() + "      ";
            }
            else if(times == 2){
                return "    "+ result.toString() + "   ";
            }
        }
        return "                   ";
    }

    private static String cardResource(Card card){
        if(card.getFlip()){
            Corner corner = new Corner("top", card.getColor(), true);
            return "         " + cornerColor(corner) + "         ";
        }
        return "                   ";
    }

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

    private static String cornerColor(Corner corner){
        if(corner.getAvailability()) {
            switch (corner.getCornerRes()) {
                case LEAF -> {
                    return "\u001B[32m"+"@" + "\u001B[0m";
                }
                case BUG -> {
                    return "\u001B[35m"+"@" + "\u001B[0m";
                }
                case FOX -> {
                    return "\u001B[34m"+"@" + "\u001B[0m";
                }
                case MUSHROOM -> {
                    return "\u001B[31m"+"@" + "\u001B[0m";
                }
                case PLUME -> {
                    return "\u001B[0m"+"P";
                }
                case POTION -> {
                    return "\u001B[0m"+"T";
                }
                case SCROLL -> {
                    return "\u001B[0m"+"S";
                }
                case null, default -> {
                    return "\u001B[0m"+"@" + "\u001B[0m";
                }
            }
        }
        return "\u001B[30m"+"@"+ "\u001B[0m";
    }

    public static int findPosition(Position position, PlayerGround ground) {
        for (Map.Entry<Integer, Position> entry : ground.getAvailableNumbers().entrySet()) {
            if (entry.getValue().equals(position)) {
                return entry.getKey();
            }
        }
        return 0;
    }


    public static void main(String[] args) throws IOException, ParseException {

        Random rand = new Random();
        ParsingController pc = new ParsingController();
        ArrayList<PlayableCard> cards = pc.parsingPlayableCards();
        ArrayList<ObjectiveCard> objectiveCards = pc.createObjectiveCardsArray();
        Deck resDeck = pc.createResDeck();
        Deck goldDeck = pc.createGoldDeck();
        PlayableCard[] handCards = new PlayableCard[3];
        ObjectiveCard[] commonObj = new ObjectiveCard[2];
        for(int i = 0; i < 3; i++){
            handCards[i] = cards.get(rand.nextInt(cards.size()));
        }
        Hand hand = new Hand(handCards);
        hand.setSecretObj(objectiveCards.get(rand.nextInt(objectiveCards.size())));
        commonObj[0] = objectiveCards.get(rand.nextInt(objectiveCards.size()));
        commonObj[1] = objectiveCards.get(rand.nextInt(objectiveCards.size()));
        ObjectiveCard[] objectiveCardsArray = new ObjectiveCard[objectiveCards.size()];
        objectiveCardsArray = objectiveCards.toArray(objectiveCardsArray);
        resDeck.shuffle();
        goldDeck.shuffle();
        printObjectives(commonObj);
        printHand(hand, objectiveCardsArray);
        printObjectives(objectiveCardsArray);
    }
}
