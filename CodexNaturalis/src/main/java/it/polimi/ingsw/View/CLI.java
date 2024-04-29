package it.polimi.ingsw.View;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.ScoreRules.CompositionRule;
import it.polimi.ingsw.Model.ScoreRules.FlatRule;
import it.polimi.ingsw.Model.ScoreRules.NSymbolsRule;
import it.polimi.ingsw.Model.ScoreRules.ScoreRule;
import it.polimi.ingsw.Controller.*;

import java.util.*;

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

    public void showStarterCard(StarterCard startcard){
        System.out.println("Your first card it's this:\n");
        showCard(startcard);
        // flipped or not?
        // updategame
        // show ground (only first)
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
                updateGame(game);
            }
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        }

        System.out.println("You placed one card. Now it's time to draw. What do you want to do? Select the number corresponding to your choice:\n1- Show hand\n2- Show decks and draw a card\n\n");
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
                int drawChoice = chooseFromDecks(game);
                Deck deck1 = game.getDecks()[0];
                Deck deck2 = game.getDecks()[1];
                if(drawChoice >= 0 && drawChoice <3){
                    deck1.drawCard(drawChoice);
                    if(drawChoice == 2){
                        System.out.println("You drew this card:\n");
                        showCard(deck1.getCards().get(2));
                    }
                }
                else{
                    deck2.drawCard(drawChoice);
                    if(drawChoice == 5){
                        System.out.println("You drew this card:\n");
                        showCard(deck1.getCards().get(5));
                    }
                }
                updateGame(game); // to do
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
        String z = "Front corners:\n";
        Corner[] cornersF = card.getCorners();
        Corner[] cornersB = card.getBackCorners();
        for(int i = 0; i<4; i++){
           z = z.concat("- ").concat(cornersF[i].getPos()).concat(" ").concat(cornersF[i].getCornerRes().name().toLowerCase()).concat(" ").concat(Boolean.toString(cornersF[i].getAvailability())).concat("\n");
        }
        z = z.concat("Back corners:\n");
        for(int i = 0; i<4; i++){
            z = z.concat("- ").concat(cornersB[i].getPos()).concat(" ").concat(cornersB[i].getCornerRes().name().toLowerCase()).concat(" ").concat(Boolean.toString(cornersB[i].getAvailability())).concat("\n");
        }
        return z;
    }

    public void showCard(Card card){
        String y,z;
        y = toStringRule(card);
        z = toStringCorner(card);
        System.out.println("Color: " + card.getColor() + ", rule: " + y + ", " + z + "\n");
    }
    public void showGround(){
        Card[][] matrix = player.getPlayerGround().getGround();    // scrittura di una matrice 0 e 1 con 1 dove c'è una carta
        int[][] matrixToPrint = new int[84][84];
        for (int i = 0; i < 84; i++) {
            for (int j = 0; j < 84; j++) {
                if(matrix[i][j]==null){
                    matrixToPrint[i][j] = 0;
                }
                else {
                    matrixToPrint[i][j] = 1;
                }
            }
        }
        //stampa della matrice
        System.out.println("Your play ground looks like this (1 = there is a card, 0 = there isn't):\n");
        for (int i = 0; i < 84; i++) {
            for (int j = 0; j < 84; j++) {
                System.out.print(matrixToPrint[i][j] + " ");
            }
            System.out.println();
        }
        // stampa available positions
        System.out.println("This is the list of positions where it is possible to place a card:\n");
        for (Position pos: player.getPlayerGround().getAvailablePositions()) {
            System.out.println("(" + pos.getX()+", " + pos.getY() + ") ");
        }
    }

    public int chooseFromDecks(Game game){
        Deck[] decks = game.getDecks();
        String deck1name = decks[0].getKindOfDeck();
        String deck2name = decks[1].getKindOfDeck();
        ArrayList<Card> deck1 = decks[0].getCards();
        ArrayList<Card> deck2 = decks[1].getCards();

        Card deck1card1 = deck1.getFirst();
        Card deck1card2 = deck1.get(1);
        Card deck1card3 = deck2.get(2);
        Card deck2card1 = deck2.getFirst();
        Card deck2card2 = deck2.get(1);
        Card deck2card3 = deck2.get(2);

        System.out.println("Here you are the game decks." +
                "\nThe first and the second card of each deck are facing up. You can choose one of them or pick the top of the remaining deck (face down)\n" +
                        "First card of the standard deck (0 to choose):\n");

        showCard(deck1card1);
        System.out.println("\nSecond card of the standard deck (1 to choose):\n");
        showCard(deck1card2);
        System.out.println("\nAlternatively, you can input 2 to choose the hidden card at the top of the standard deck.\n");
        System.out.println("First card of the golden deck (3 to choose):\n");
        showCard(deck2card1);
        System.out.println("\nSecond card of the golden deck (4 to choose):\n");
        showCard(deck2card2);
        System.out.println("\nAlternatively, you can input 5 to choose the hidden card at the top of the standard deck.\n");
        System.out.println("Which card do you choose? Input the number corresponding to your choice:\n");
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        do{
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice >=0 && choice<6));
        return choice;
    }
    public void updateGame(Game game){ //ogni volta che c'è un place o draw
        //
    }

}
