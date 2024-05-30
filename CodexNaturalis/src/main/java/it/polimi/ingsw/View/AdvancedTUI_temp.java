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
        Map<Position, Integer> positionIntegerMap = new HashMap<>();
        int counter = 0;
        for (Position position : availablePosition) {
            positionIntegerMap.put(position, counter);
            counter++;
        }

        availablePosition.addAll(unavailablePosition);

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

        String topBorder = "";
        String contentLine1 =  "";
        String contentLine2 =  "";
        String contentLine3 =  "";
        String contentLine4 = "";

        /* boolean state = true;
        for(int i = lengthY, maxYOffset = maxY; i >=0; i--, maxYOffset--){
            for(int j = 0, minXOffset = minX; j <lengthX ; j++, minXOffset++ ){
                if(ground[minXOffset][maxY] != null) {
                    //contentLine1.append(getGridLine1(ground[minXOffset][maxY]).append(ground[minXOffset][maxY].getId()));
                    contentLine1.append(1);
                }else{
                    //contentLine1.append(getGridLine1(ground[minXOffset][maxY]));
                    contentLine1.append(0);
                }
                //contentLine2.append(getGridLine2(ground[minXOffset][maxY]));
                //contentLine3.append(getGridLine3(ground[minXOffset][maxY]));
                if(state) {
                    //contentLine4.append(getGridLine4(ground[minXOffset][maxY], state));
                }else{
                    int maxY2 = maxY--;
                    //contentLine4.append(getGridLine4(ground[minXOffset][maxY2], state));
                }
                state = !state;
            }
            System.out.println(contentLine1);
            //System.out.println(contentLine2);
            //System.out.println(contentLine3);
            //System.out.println(contentLine4);

        } */
        // scrittura di una matrice 0 e 1 con 1 dove c'è una carta
        Card[][] matrixToPrint = new Card[84][84];
        for(int i = 0, maxYOffset = maxY; i <= lengthY; i++, maxYOffset--){
            for(int j = 0, minXOffset = minX; j <= lengthX; j++, minXOffset++){
                if(ground[minXOffset][maxYOffset]==null){
                    matrixToPrint[i][j] = null;
                }
                else {
                    matrixToPrint[i][j] = ground[minXOffset][maxYOffset];
                }
            }
        }

        System.out.println("\n");
        System.out.println("Your play ground looks like this:\n");
        System.out.println("\n");
        for (int j = lengthX; j >= 0; j--) {
                contentLine4 += getGridLine5(matrixToPrint[j][lengthY], false);
        }
        System.out.println("                       " + contentLine4 + "                       ");
        contentLine4 = "";
        for (int i = lengthY; i >= 0; i--) {
            for (int j = lengthX; j >= 0; j--) {
                contentLine1 += getGridLine1(matrixToPrint[j][i]);
                contentLine2 += getGridLine2(matrixToPrint[j][i],minX + i, maxY - j, playerGround);
                contentLine3 += getGridLine3(matrixToPrint[j][i]);
                if(matrixToPrint[j][i] == null && i-1 >= 0) {
                        contentLine4 += getGridLine5(matrixToPrint[j][i - 1], false);
                }else{
                        contentLine4 += getGridLine4(matrixToPrint[j][i], true);
                }

            }
            System.out.println("                       " + contentLine1 + "                       ");
            System.out.println("                       " + contentLine2 + "                       ");
            System.out.println("                       " + contentLine3 + "                       ");
            System.out.println("                       " + contentLine4 + "                       ");
            contentLine1 = "";
            contentLine2 = "";
            contentLine3 = "";
            contentLine4 = "";
        }

        System.out.println("\n");

    }

    public static void printResources(HashMap<Resource, Integer> totalResources){
        StringBuilder resourceLine1 = new StringBuilder("  ");
        StringBuilder resourceLine2 = new StringBuilder("  ");
            for (Map.Entry<Resource, Integer> entry : totalResources.entrySet()) {
                Resource resource = entry.getKey();
                if(!(resource.equals(NOTVISIBLE) || resource.equals(BLANK))) {
                    if(!(resource.equals(POTION) || resource.equals(SCROLL) || resource.equals(PLUME))) {
                        Corner corner = new Corner("top", resource, true);
                        Integer value = entry.getValue();
                        resourceLine1.append(resourceColor(resource)).append(resource).append("  ").append(cornerColor(corner)).append("\u001B[0m").append(": ").append(value).append("           ");
                    }
                    else{
                        Corner corner = new Corner("top", resource, true);
                        Integer value = entry.getValue();
                        resourceLine2.append(resourceColor(resource)).append(resource).append("  ").append(cornerColor(corner)).append("\u001B[0m").append(": ").append(value).append("           ");
                    }
                }
            }
        System.out.println("                                     RESOURCES:\n");
        System.out.println("    " + resourceLine1 + "\n");
        System.out.println("             " + resourceLine2);
    }



    public static void printDeck(Deck deck){
        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();

        String ANSI_RESET = "\u001B[0m";

        ArrayList<PlayableCard> cards = deck.getCards();
        cards.getFirst().flipCard();

        for(int i = 0; i <= 2; i++){
            topBorder.append(" ").append(cornerColor(cards.get(i).getShowedCorners()[1])).append(cardColor(cards.get(i))).append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀").append(ANSI_RESET).append(cornerColor(cards.get(i).getShowedCorners()[3])).append(" ");
            contentLine1.append(cardColor(cards.get(i))).append(" ▌").append(cardContent(cards.get(i))).append(cardColor(cards.get(i))).append("▌ ");
            contentLine2.append(cardColor(cards.get(i))).append(" ▌").append(cardResource(cards.get(i))).append(cardColor(cards.get(i))).append("▌ ");
            contentLine3.append(cardColor(cards.get(i))).append(" ▌").append(cardRequirements(cards.get(i))).append(cardColor(cards.get(i))).append("▌ ");
            buttonBorder.append(" ").append(cornerColor(cards.get(i).getShowedCorners()[0])).append(cardColor(cards.get(i))).append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄").append(ANSI_RESET).append(cornerColor(cards.get(i).getShowedCorners()[2])).append(" ");
        }

        System.out.println(topBorder);
        System.out.println(contentLine1);
        System.out.println(contentLine2);
        System.out.println(contentLine3);
        System.out.println(buttonBorder);

    }



    public static void printObjectives(ObjectiveCard[] objectiveCards){
        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();

        for(int i = 0; i< objectiveCards.length; i++) {
            topBorder.append("  ").append("\u001B[33m").append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
            contentLine1.append("\u001B[33m").append(" ▌").append(firstLineObjective(objectiveCards[i])).append("\u001B[33m").append("▌ ");
            contentLine2.append("\u001B[33m").append(" ▌").append(secondLineObjective(objectiveCards[i])).append("\u001B[33m").append("▌ ");
            contentLine3.append("\u001B[33m").append(" ▌").append(thirdLineObjective(objectiveCards[i])).append("\u001B[33m").append("▌ ");
            buttonBorder.append("  ").append("\u001B[33m").append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");

        }

        System.out.println("                        " + topBorder);
        System.out.println("                        " + contentLine1);
        System.out.println("                        " + contentLine2);
        System.out.println("                        " + contentLine3);
        System.out.println("                        " + buttonBorder);
}

    public static void printStartingCard(StarterCard card){

        StringBuilder topBorder = new StringBuilder();
        StringBuilder contentLine1 =  new StringBuilder();
        StringBuilder contentLine2 =  new StringBuilder();
        StringBuilder contentLine3 =  new StringBuilder();
        StringBuilder buttonBorder =  new StringBuilder();

        String ANSI_RESET = "\u001B[0m";


            topBorder.append(" ").append(cornerColor(card.getCorners()[1])).append("\u001B[0m").append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀ ").append(ANSI_RESET).append(cornerColor(card.getCorners()[3])).append(" ");
            contentLine1.append("\u001B[0m").append(" ▌").append(firstCardContent(card,1)).append("\u001B[0m").append("▌  ");
            contentLine2.append("\u001B[0m").append(" ▌").append(firstCardContent(card,2)).append("\u001B[0m").append("▌  ");
            contentLine3.append("\u001B[0m").append(" ▌").append(firstCardContent(card,3)).append("\u001B[0m").append("▌  ");
            buttonBorder.append(" ").append(cornerColor(card.getCorners()[0])).append("\u001B[0m").append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄ ").append(ANSI_RESET).append(cornerColor(card.getCorners()[2])).append(" ");

        topBorder.append(" ").append(cornerColor(card.getBackCorners()[1])).append("\u001B[0m").append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀").append(ANSI_RESET).append(cornerColor(card.getBackCorners()[3])).append(" ");
        contentLine1.append("\u001B[0m").append(" ▌").append("                    ").append("\u001B[0m").append("▌ ");
        contentLine2.append("\u001B[0m").append(" ▌").append("                    ").append("\u001B[0m").append("▌ ");
        contentLine3.append("\u001B[0m").append(" ▌").append("                    ").append("\u001B[0m").append("▌ ");
        buttonBorder.append(" ").append(cornerColor(card.getBackCorners()[0])).append("\u001B[0m").append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄").append(ANSI_RESET).append(cornerColor(card.getBackCorners()[2])).append(" ");


        System.out.println("                        " + topBorder);
        System.out.println("                        " + contentLine1);
        System.out.println("                        " + contentLine2);
        System.out.println("                        " + contentLine3);
        System.out.println("                        " + buttonBorder);
    }
    private static String firstCardContent(StarterCard starterCard, int state){
        if(state == 1){
            Corner corner = new Corner("top",starterCard.getBackRes().getFirst(), true);
            return "         "+ "\u200A" + "\u200A" + "\u200A" + "\u200A" +cornerColor(corner) + "\u200A" + "\u200A" + "\u200A" + "\u200A" +"        ";
        }
        if(state == 2 && !starterCard.getBackRes().get(1).equals(BLANK)){
            //return "█    first card    █
            Corner corner = new Corner("top",starterCard.getBackRes().get(1), true);
            return "         "+ "\u200A" + "\u200A" + "\u200A" + "\u200A" +cornerColor(corner) + "\u200A" + "\u200A" + "\u200A" + "\u200A" +"        ";
        }
        if(state == 3 && !starterCard.getBackRes().get(2).equals(BLANK)){
            Corner corner = new Corner("top",starterCard.getBackRes().get(2), true);
            return "         "+ "\u200A" + "\u200A" + "\u200A" + "\u200A" +cornerColor(corner) + "\u200A" + "\u200A" + "\u200A" + "\u200A" +"        ";
        }
        else{
            return "                    ";
        }
    }
    public static void printHand(Hand hand){

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
                topBorder.append(" ").append(cornerColor(cards[i].getShowedCorners()[1])).append(cardColor(cards[i])).append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀").append(ANSI_RESET).append(cornerColor(cards[i].getShowedCorners()[3])).append(" ");
                contentLine1.append(cardColor(cards[i])).append(" ▌").append(cardContent(cards[i])).append(cardColor(cards[i])).append("▌ ");
                contentLine2.append(cardColor(cards[i])).append(" ▌").append(cardResource(cards[i])).append(cardColor(cards[i])).append("▌ ");
                contentLine3.append(cardColor(cards[i])).append(" ▌").append(cardRequirements(cards[i])).append(cardColor(cards[i])).append("▌ ");
                buttonBorder.append(" ").append(cornerColor(cards[i].getShowedCorners()[0])).append(cardColor(cards[i])).append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄").append(ANSI_RESET).append(cornerColor(cards[i].getShowedCorners()[2])).append(" ");
            }
        }
        //print Secret Objective
        topBorder.append("  ").append("\u001B[33m").append("▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
        contentLine1.append("\u001B[33m").append(" ▌").append(firstLineObjective(objective)).append("\u001B[33m").append("▌ ");
        contentLine2.append("\u001B[33m").append(" ▌").append(secondLineObjective(objective)).append("\u001B[33m").append("▌ ");
        contentLine3.append("\u001B[33m").append(" ▌").append(thirdLineObjective(objective)).append("\u001B[33m").append("▌ ");
        buttonBorder.append("  ").append("\u001B[33m").append("▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");
        // Print the card
        System.out.println(topBorder);
        System.out.println(contentLine1);
        System.out.println(contentLine2);
        System.out.println(contentLine3);
        System.out.println(buttonBorder);
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
        if(state) {
            return cornerColor(card.getShowedCorners()[0]) + cardColor(card) + "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀" + "\u001B[0m" + cornerColor(card.getShowedCorners()[2]) + "\u001B[0m";
        }
        return cornerColor(card.getShowedCorners()[1]) + cardColor(card) + "▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀" + "\u001B[0m" + cornerColor(card.getShowedCorners()[3]) + "\u001B[0m";
    }

    private static String getGridLine5(Card card, boolean state){
        if(card == null) return "                  ";
        if(card.getId() >= 81 && card.getId() <= 86){
            return getFirstGridLine(card, 4, state);
        }
        if(card.getId() == -1){
            return cardColor(card) + " ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄ " + "\u001B[0m";
        }
        if(state) {
            return cornerColor(card.getShowedCorners()[0]) + cardColor(card) + "▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄" + "\u001B[0m" + cornerColor(card.getShowedCorners()[2]) + "\u001B[0m";
        }
        return cornerColor(card.getShowedCorners()[1]) + cardColor(card) + "▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄" + "\u001B[0m" + cornerColor(card.getShowedCorners()[3]) + "\u001B[0m";
    }


    private static String getFirstGridLine(Card card, int state, boolean status){
        StarterCard starterCard = (StarterCard) card;
        if(state == 1 && !starterCard.getFlip()){
            Corner corner = new Corner("top",starterCard.getBackRes().getFirst(), true);
            return "█         "+ cornerColor(corner) +"        █";
        }
        if(state == 2 && !starterCard.getFlip() && !starterCard.getBackRes().get(1).equals(BLANK)){
            //return "█    first card    █
            Corner corner = new Corner("top",starterCard.getBackRes().get(1), true);
            return "█         "+ cornerColor(corner) +"        █";
        }
        if(state == 3 && !starterCard.getFlip() && !starterCard.getBackRes().get(2).equals(BLANK)){
            Corner corner = new Corner("top",starterCard.getBackRes().get(2), true);
            return "█         "+ cornerColor(corner) +"        █";
        }
        if(state == 4 && !status){
            return cornerColor(card.getShowedCorners()[1]) + "████████████████" + "\u001B[0m" + cornerColor(card.getShowedCorners()[3]) + "\u001B[0m";
        }
        if(state == 4){
            return cornerColor(card.getShowedCorners()[0]) + "████████████████" + "\u001B[0m" + cornerColor(card.getShowedCorners()[2]) + "\u001B[0m";
        }
        return "█                  █";
    }

    private static String firstLineObjective(ObjectiveCard objectiveCard){
        ScoreRule rule = objectiveCard.getRule();
        switch (rule.getName()){
            case "NSR", "OER" -> {
                return "   " + rule.getPoints() + " Points each    ";
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
                return "                    ";
            }
        }
        return "                    ";
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
                    return "       " + resource + " " + "\u200A" + "\u200A" + "\u200A" +"  " + resource + "       ";
                }
                return "      " + resource + "  " + resource + "  " + resource + "      ";
            }
            case "OER" -> {
                return "\u001B[0m" + "     P   T   S      ";
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
                return "      resources     ";
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
        if(card.getFlip()) return "                    ";
        switch (card.getRule().getName()){
            case "FR" -> {
                int points = card.getRule().getPoints();
                if(points == 0){
                    return "                    ";
                }
                return "     Points: "+ card.getRule().getPoints() +"      ";
            }
            case "CCR" -> {
                return " Covered corners: "+ card.getRule().getPoints() +" ";
            }

            case "NSR" -> {
                NSymbolsRule rule = (NSymbolsRule) card.getRule();
                String resource = null;
                switch(rule.getSymbol()){
                    case SCROLL -> resource = "S";
                    case PLUME ->  resource = "P";
                    case POTION -> resource = "T";
                }
                return "  Points per "+ resource + ": " + rule.getPoints()+"   ";
            }
            case null, default -> {
                return "                    ";
            }
        }

    }

    private static String cardRequirements(PlayableCard card){
        HashMap<Resource, Integer> map = card.getRequirements();
        StringBuilder result = new StringBuilder();
        int times = 0;
        if(card.getFlip()) return  "                    ";
        if(map != null){
            for (Map.Entry<Resource, Integer> entry : map.entrySet()) {
                Resource resource = entry.getKey();
                Corner corner = new Corner("top",resource,true);
                Integer value = entry.getValue();
                if(value != 0) {
                    result.append(cornerColor(corner)).append(" : ").append(value).append("  ");
                    times++;
                }
            }
            if(times ==1){
                return "       "+ result.toString() + "     " + "\u200A" + "\u200A" + "\u200A";
            }
            else if(times == 2){
                return "   "+ result.toString() + "  " + "\u200A";
            }
        }
        return "                    ";
    }

    private static String cardResource(Card card){
        if(card.getFlip()){
            Corner corner = new Corner("top", card.getColor(), true);
            return "         " + cornerColor(corner) + "         " + "\u200A" + "\u200A" + "\u200A";
        }
        return "                    ";
    }

    private static String cardColor(Card card){
        if(card.getId() == -1) return "\u001B[37m";
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
                    return "\u001B[32m"+"◙" + "\u001B[0m";
                }
                case BUG -> {
                    return "\u001B[35m"+"◙" + "\u001B[0m";
                }
                case FOX -> {
                    return "\u001B[34m"+"◙" + "\u001B[0m";
                }
                case MUSHROOM -> {
                    return "\u001B[31m"+"◙" + "\u001B[0m";
                }
                case PLUME -> {
                    return "\u001B[0m"+"P" + "\u200A";
                }
                case POTION -> {
                    return "\u001B[0m"+"T" + "\u200A";
                }
                case SCROLL -> {
                    return "\u001B[0m"+"S" + "\u200A";
                }
                case null, default -> {
                    return "\u001B[0m"+"◙" + "\u001B[0m";
                }
            }
        }
        return "\u001B[30m"+"◙"+ "\u001B[0m";
    }

    public static int findPosition(Position position, PlayerGround ground) {
        for (Map.Entry<Integer, Position> entry : ground.getAvailableNumbers().entrySet()) {
            if (entry.getValue().equals(getExactPosition(position, ground.getAvailablePositions()))) {
                return entry.getKey();
            }
        }
        return 0;
    }

    private static Position getExactPosition(Position position, Set<Position> positions){
        for(Position p : positions){
            if(position.getX() == p.getX() && position.getY() == p.getY()){
                return p;
            }

        }
        return position;
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
        printDeck(resDeck);
        printDeck(goldDeck);
        printObjectives(commonObj);
        printHand(hand);
        printObjectives(objectiveCardsArray);
    }
}
