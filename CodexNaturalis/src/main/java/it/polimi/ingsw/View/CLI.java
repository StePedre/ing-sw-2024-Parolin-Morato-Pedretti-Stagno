package it.polimi.ingsw.View;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.ScoreRules.CompositionRule;
import it.polimi.ingsw.Model.ScoreRules.FlatRule;
import it.polimi.ingsw.Model.ScoreRules.NSymbolsRule;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;
import it.polimi.ingsw.Controller.*;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class CLI {

    private Player player;

    private Game game;
    public CLI(Player player, Game game){
        this.player = player;
        this.game = game;
    }

    public void Welcome() {
        System.out.println("Welcome to Codex Naturalis, " + player.getNickname() + "!\n");
    }

    public void notYourTurn(){
        System.out.println("\nIt's your turn!\nWhat do you want to do? Select the number corresponding to your choice:\n1- Show play ground\n2- Show hand\n3- Show card on ground\n\n");
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        do {
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice>0 && choice<5));
        // if ... ogni opzione (copia da sotto)
    }

    public void yourTurn(){
        System.out.println("\nWhat do you want to do? Select the number corresponding to your choice:\n1- Show play ground\n2- Show hand\n3- Show card on ground\n 4- Play card\n\n");
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        do {
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice>0 && choice<5));
        switch (choice) {
            case 1 -> {
                showGround(); // to do
            }
            case 2 -> {
                showHand();
            }
            case 3 -> {
                System.out.println("Which card do you want to see? Insert coordinates (x first):\n");
                int coordX = scanner.nextInt();
                scanner.nextLine();
                int coordY = scanner.nextInt();
                scanner.nextLine();
                Card cardToShow = player.getPlayerGround().getGround()[coordX][coordY]; // prende carta (x,y) dal ground del player
                showCard(cardToShow);      //sistemare show dei corner, solo quelli che servono -> al limite due metodi
            }
            case 4 -> {
                // play card
            }
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        }

        System.out.println("You placed one card. Now it's time to draw. What do you want to do? Select the number corresponding to your choice:\n1- Show hand\n2- Show decks\n3- Draw card\n\n");
        int choice2 = 0;
        do {
            choice2 = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice2>0 && choice2<5));
        switch ((choice)){
            case 1 -> {
                showHand();
            }
            case 2 -> {
                showDecks(); // to do
            }
            case 3 -> {
                // draw card
            }
        }
    }
    public void showHand(){
       Hand hand = player.getHand();

       int i = 1;

       System.out.println("\nThis is your hand:\n");

       for(Card card: hand.getCards()){
           System.out.println("Card " + i + "-> ");
           showCard(card);
           i++;
       }

        System.out.println("Score rules (legend):\n- FR 'Flat Rule'. Card gives the indicated amount of points each time it is played.\n" +
                "- CCR 'Covered Corners Rule'. Card gives 2 points for each of its covered corners.\n" +
                "- NSR 'N symbols Rule'. Card gives the indicated amount of points for each time the group of N indicated symbols appears on the ground.\n" +
                "- CR 'Composition Rule'. Card gives the indicated amount of points for each time the indicated composition appears on the ground.\n" +
                "- OER 'One of Each Rule'. Card gives 3 points each time there is a set of plume, potion and scroll.\n");
    }

    public String toStringRule(Card card){
        String y = null;
        if(card.getRule().getName().equals("FR")){
            int p = card.getRule().getPoints();
            y = "FR, " + p + " points";
        }
        if(card.getRule().getName().equals("CCR")){
            int p = card.getRule().getPoints();
            y = "CCR, " + p + " points";
        }
        if(card.getRule().getName().equals("NSR")){
            int p = card.getRule().getPoints();
            NSymbolsRule rule = (NSymbolsRule) card.getRule();
            y = "NSR, " + p + " points, required " + rule.getPoints() + " " + rule.getSymbol().name().toLowerCase() + "(s)"; //Matteo?????
        }
        if(card.getRule().getName().equals("CR")){
            int p = card.getRule().getPoints();
            CompositionRule rule = (CompositionRule) card.getRule();
            y = "CR, " + p + " points, required colors and offsets: " + rule.getColors() + " " + Arrays.toString(rule.getOffsets());
        }
        if(card.getRule().getName().equals("OER")){
            int p = card.getRule().getPoints();
            y = "OER, " + p + " points";
        }
        return y;
    }

    public String toStringCorner(Card card){
        StringBuilder z = new StringBuilder("Front corners:\n");
        Corner[] cornersF = card.getCorners();
        Corner[] cornersB = card.getBackCorners();
        for(int i = 0; i<4; i++){
           z.append("- ").append(cornersF[i].getPos()).append(" ").append(cornersF[i].getCornerRes().name().toLowerCase()).append(" ").append(cornersF[i].getAvailability()).append("\n");
        }
        z.append("Back corners:\n");
        for(int i = 0; i<4; i++){
            z.append("- ").append(cornersB[i].getPos()).append(" ").append(cornersB[i].getCornerRes().name().toLowerCase()).append(" ").append(cornersB[i].getAvailability()).append("\n");
        }
        return z.toString();
    }

    public void showCard(Card card){
        String y,z;
        y = toStringRule(card);
        z = toStringCorner(card);
        System.out.println("Color: " + card.getColor() + ", rule: " + y + ", " + z + "\n");
    }
    public void showGround(){
        // stampa matrice
        // stampa available corners
    }

    public void showDecks(){
        
    }
    public void updateGame(Game game){ //ogni volta che c'è un place o draw

    }

}
