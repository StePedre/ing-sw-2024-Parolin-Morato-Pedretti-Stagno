package it.polimi.ingsw.View;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.ScoreRules.CompositionRule;
import it.polimi.ingsw.Model.ScoreRules.NSymbolsRule;
import java.util.*;

public class CLI {
    public CLI(){}

    public void Welcome(Player player) {
        System.out.println("Welcome to Codex Naturalis, " + player.getNickname() + "!\n");
    }

    public boolean showStarterCard(StarterCard startcard){
        System.out.println("Your first card it's this:\n");
        showCard(startcard);
        System.out.println(toStringBackCorners(startcard));
        System.out.println("\nDo you want to place it flipped or not? Input 0 for not flipped, 1 for flipped.\n");
        Scanner scanner = new Scanner(System.in);
        int choice;
        boolean flag = false;
        do {
            choice = scanner.nextInt();
            if(choice == 1){
                flag = true;
            }
            scanner.nextLine();
        } while(!(choice == 0 || choice == 1));
        return flag;
    }

    public int chooseObjective(ObjectiveCard obj1, ObjectiveCard obj2){
        int choice;
        System.out.println("Choose between two secret objectives:\n1- " + toStringRule(obj1) + "\n2- " + toStringRule(obj2));
        Scanner scanner = new Scanner(System.in);
        do {
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice==1 || choice == 2));
        return choice;
    }
 // aggiungere un while (nel client) che lo fa andare finchè non è il proprio turno
    public void notYourTurn(Game game, Player  player){
        System.out.println("\nIt's your turn!\nWhat do you want to do? Select the number corresponding to your choice:\n1- Show play ground (and common objectives)\n2- Show hand\n3- Show card on ground\n\n");
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice>0 && choice<4));
        switch (choice) {
            case 1 ->
                showGround(game, player);
            case 2 ->
                showHand(player);
            case 3 -> {
                System.out.println("Which card do you want to see? Insert coordinates (x first):\n");
                int coordX = scanner.nextInt();
                scanner.nextLine();
                int coordY = scanner.nextInt();
                scanner.nextLine();
                Card cardToShow = player.getPlayerGround().getGround()[coordX][coordY]; // prende carta (x,y) dal ground del player
                if(coordX == 42 && coordY ==42){
                    StarterCard starterCardToShow = (StarterCard) cardToShow;
                    showCard(starterCardToShow);
                }
                else{
                    PlayableCard CardToShow = (PlayableCard) cardToShow;
                    showCard(CardToShow);
                }
            }
        }
    }

    public boolean yourTurnPlay(Game game, Player player){
        System.out.println("\nWhat do you want to do? Select the number corresponding to your choice:\n1- Show play ground\n2- Show hand\n3- Show card on ground\n 4- Play card\n\n");
        int choice;
        Scanner scanner = new Scanner(System.in);
            do {
                choice = scanner.nextInt();
                scanner.nextLine();
            } while (!(choice > 0 && choice < 5));
            switch (choice) {
                case 1 -> {
                    showGround(game, player);
                    return false;
                }
                case 2 -> {
                    showHand(player);
                    return false;
                }
                case 3 -> {
                    System.out.println("Which card do you want to see? Insert coordinates (x first):\n");
                    int coordX = scanner.nextInt();
                    scanner.nextLine();
                    int coordY = scanner.nextInt();
                    scanner.nextLine();
                    Card cardToShow = player.getPlayerGround().getGround()[coordX][coordY]; // prende carta (x,y) dal ground del player
                    if (coordX == 42 && coordY == 42) {
                        StarterCard starterCardToShow = (StarterCard) cardToShow;
                        showCard(starterCardToShow);
                    } else {
                        PlayableCard CardToShow = (PlayableCard) cardToShow;
                        showCard(CardToShow);
                    }
                    return false;
                }
                case 4 -> {
                    return true;
                }
                default -> throw new IllegalStateException("Unexpected value: " + choice);
            }
    }
                    //client inputs coordinates where to place the card
    public Position inputCoordinates(){
        System.out.println("Where do you want to place the card? Insert coordinates between 0 and 83 (X first):\n");
        int coordX = -1;
        int coordY = -1;
        Scanner scanner = new Scanner(System.in);
        while(!(coordY>=0 && coordY<84 && coordX>=0 && coordX <84)){
            coordX = scanner.nextInt();
            scanner.nextLine();
            coordY = scanner.nextInt();
            scanner.nextLine();
        }
        return (new Position(coordX, coordY));
    }
                                    //client chooses which card (and if flipped) to place in the coordinates given before
    public PlayableCard inputCardToPlace(Player player){
        System.out.println("Which card do you want to play? 1, 2 or 3? This is your hand:\n");
        showHand(player);
        PlayableCard cardToPlay = null;
        int card = 0;
        Scanner scanner = new Scanner(System.in);
        while(card!=1 && card!=2 && card!=3) {
            card = scanner.nextInt();
            scanner.nextLine();
            if (card == 1) {
                cardToPlay = (PlayableCard) player.getHand().getCard(0);
            } else if (card == 2) {
                cardToPlay = (PlayableCard) player.getHand().getCard(1);
            } else if (card == 3) {
                cardToPlay = (PlayableCard) player.getHand().getCard(2);
            } else {
                System.out.println("Invalid choice. Choose between 1, 2 and 3:\n");
            }
        }
        System.out.println("Flipped (input 1) or not (input 0)?\n");
        int flip = -1;
        while(flip!=1 && flip!=0) {
            flip = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Invalid choice. Choose between 1, 2 and 3:\n");
        }
        if (flip ==1) {
            cardToPlay.flipCard();
        }
        return cardToPlay;
    }

        // draw section
    public PlayableCard yourTurnDraw(Game game, Player player){
        System.out.println("You placed one card. Now it's time to draw. What do you want to do? Select the number corresponding to your choice:\n1- Show hand\n2- Show decks and draw a card\n\n");
        int choice2 = 0;
        Scanner scanner = new Scanner(System.in);
        while(choice2!=2) {
            do {
                choice2 = scanner.nextInt();
                scanner.nextLine();
            } while (!(choice2 > 0 && choice2 < 3));
            switch ((choice2)) {
                case 1 -> showHand(player);
                case 2 -> {
                }
            }
        }
        return chooseFromDecks(game);
    }

    public void showHand(Player player){
       Hand hand = player.getHand();
       ObjectiveCard secretObj = hand.getObjCard();

       int i = 1;

       System.out.println("\nThis is your hand:\n");
       System.out.println("Secret objective: " + toStringRule(secretObj));

       for(Card card: hand.getCards()){
           System.out.println("Card " + i + "-> ");
           showCard((PlayableCard) card);
           i++;
       }

        System.out.println("""
                Score rules (legend):
                - FR 'Flat Rule'. Card gives the indicated amount of points each time it is played.
                - CCR 'Covered Corners Rule'. Card gives 2 points for each of its covered corners.
                - NSR 'N symbols Rule'. Card gives the indicated amount of points for each time the group of N indicated symbols appears on the ground.
                - CR 'Composition Rule'. Card gives the indicated amount of points for each time the indicated composition appears on the ground.
                - OER 'One of Each Rule'. Card gives 3 points each time there is a set of plume, potion and scroll.
                """);
    }

    public String toStringRule(Card card){  // stampa la rule di una carta giocabile
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
    public String toStringRule(ObjectiveCard card){  // stampa la rule di una carta obiettivo
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


    public String toStringFrontCorners(Card card) {
        String z = "Front corners:\n";
        Corner[] cornersF = card.getCorners();
        for (int i = 0; i < 4; i++) {
            z = z.concat("- ").concat(cornersF[i].getPos()).concat(" ").concat(cornersF[i].getCornerRes().name().toLowerCase()).concat(" ").concat(Boolean.toString(cornersF[i].getAvailability())).concat("\n");
        }
        return z;
    }
    public String toStringBackCorners(Card card){
        Corner[] cornersB = card.getBackCorners();
        String z = "Back corners:\n";
            for (int i = 0; i < 4; i++) {
                z = z.concat("- ").concat(cornersB[i].getPos()).concat(" ").concat(cornersB[i].getCornerRes().name().toLowerCase()).concat(" ").concat(Boolean.toString(cornersB[i].getAvailability())).concat("\n");
            }
            return z;
    }

    public String toStringReq(PlayableCard card){
        String w  = "Requirements: ";
        HashMap<Resource, Integer> reqMap = card.getRequirements();
        int fox = reqMap.get(Resource.FOX);
        int leaf = reqMap.get(Resource.LEAF);
        int mushroom = reqMap.get(Resource.MUSHROOM);
        int bug = reqMap.get(Resource.BUG);
        w = w.concat(fox + " foxes," + leaf + " leaves," + mushroom + " mushrooms," + bug + " bugs.");
        return w;
    }

    public void showCard(PlayableCard card){
        String y,z,w;
        y = toStringRule(card);
        if(card.getFlip()){
            z = toStringBackCorners(card);
        }
        else{
            z = toStringFrontCorners(card);
        }
        w = toStringReq(card);
        System.out.println("Color: " + card.getColor() + ", rule: " + y + "\n" + z + "\n" + w + "\n");
    }

    public void showCard(StarterCard card){
        String y,z;
        y = toStringRule(card);
        if(card.getFlip()){
            z = toStringBackCorners(card);
        }
        else{
            z = toStringFrontCorners(card);
        }
        System.out.println("Color: " + card.getColor() + ", rule: " + y + "\n" + z + "\n");
    }

    public void showGround(Game game, Player player){
        ObjectiveCard[] commonObjs = game.getCommonObj();
        System.out.println("\nThese are your common objectives:\n");
        for(ObjectiveCard obj: commonObjs){
            System.out.println(toStringRule(obj));
        }
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

    public PlayableCard chooseFromDecks(Game game){
        Deck[] decks = game.getDecks();
        String deck1name = decks[0].getKindOfDeck();
        String deck2name = decks[1].getKindOfDeck();
        ArrayList<Card> deck1 = decks[0].getCards();
        ArrayList<Card> deck2 = decks[1].getCards();

        Card deck1card1 = deck1.getFirst();
        Card deck1card2 = deck1.get(1);
        Card deck1card3 = deck1.get(2);
        Card deck2card1 = deck2.getFirst();
        Card deck2card2 = deck2.get(1);
        Card deck2card3 = deck2.get(2);

        System.out.println("Here you are the game decks." +
                "\nThe first and the second card of each deck are facing up. You can choose one of them or pick the top of the remaining deck (face down)\n" +
                        "First card of the " + deck1name + " (0 to choose):\n");

        showCard((PlayableCard) deck1card1);
        System.out.println("\nSecond card of the " + deck1name + " (1 to choose):\n");
        showCard((PlayableCard) deck1card2);
        System.out.println("\nAlternatively, you can input 2 to choose the hidden card at the top of the " + deck1name +".\n");
        System.out.println("First card of the " + deck2name + " (3 to choose):\n");
        showCard((PlayableCard) deck2card1);
        System.out.println("\nSecond card of the " + deck2name + " (4 to choose):\n");
        showCard((PlayableCard) deck2card2);
        System.out.println("\nAlternatively, you can input 5 to choose the hidden card at the top of the " + deck2name + "deck.\n");
        System.out.println("Which card do you choose? Input the number corresponding to your choice:\n");
        Scanner scanner = new Scanner(System.in);
        int choice;
        Card chosen = null;
        do{
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice >=0 && choice<6));
        switch (choice){
            case 0 ->
                chosen = deck1card1;
            case 1 ->
                chosen = deck1card2;
            case 2->
                chosen = deck1card3;
            case 3 ->
                chosen = deck2card1;
            case 4 ->
                chosen = deck2card2;
            case 5->
                chosen = deck2card3;
        }
        return (PlayableCard) chosen;
    }
}
