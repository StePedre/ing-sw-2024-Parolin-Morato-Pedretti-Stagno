package it.polimi.ingsw.View;
import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.ScoreRules.CompositionRule;
import it.polimi.ingsw.Model.ScoreRules.NSymbolsRule;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Each object of TUI class (Text-based User Interface) is associated with one Player object.
 * The class implements input and output methods to support players' decisions during the game.
 * Interactions are made through standard output (print on screen) and standard input (keyboard).
 * Methods from TUI class are invoked by clients both in RMI and socket implementation.
 */
public class TUI {


    private static final String LOG_FILE_PATH = "debug.log";

    /**
     * Constructor of the class with no parameters.
     */
    public TUI(){}

    /**
     * The method asks for the client's nickname and returns it as a string.
     *
     * @return input nickname.
     */
    public String insertNickname(boolean choice){
        if(choice) {
            System.out.println("Insert your nickname, please: ");
        }else{
            System.out.println("Insert a valid nickname , please: ");
        }
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * The method asks for the number of players that will be in the game. It should only be invoked
     * when the client is the first player joining the game.
     *
     * @return desired number of players.
     */
    public int askPlayersNo(){
        System.out.println("You're the first player, insert how many players you'd like, please: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    /**
     * The method prints a welcome message to the player passed as a parameter.
     *
     * @param player gets the message.
     */
    public void Welcome(Player player) {
        System.out.println("Welcome to Codex Naturalis, " + player.getNickname() + "!\n"+"Please wait for others player");
    }

    /**
     * When the game is over, this method prints the name(s) of the winner(s).
     * To better understand the possibility of multiple winners, see Game.finish().
     *
     * @param winners is passed by the controller, which gets it from invoking game.finish().
     *                It is the list of game winners. It may be only one.
     */
    public void winnersPrint(ArrayList<Player> winners){
        System.out.println("The game is over!");
        if(winners.size()==1){
            System.out.println("The winner is: " + winners.getFirst().getNickname());
        }
        else{
            System.out.println("It's a draw! The winners are:\n");
            for(Player p: winners){
                System.out.println(p.getNickname()+"\n");
            }
        }

    }

    /**
     * The method shows to a player its starting card, which is random, through the method showCard and
     * toStringBackCorners (see such methods). The method also asks the player if they want to place the starter card
     * flipped or not, and returns the boolean value of the answer.
     *
     * @param startcard is the player's StarterCard randomly chosen by the controller.
     * @return true if the player wants to place the starter card face down, false otherwise.
     */
    public boolean showStarterCard(StarterCard startcard){
        System.out.println("Your first card it's this:\n");
        /*showCard(startcard);
        System.out.println(toStringBackCorners(startcard));*/
        AdvancedTUI_temp.printStartingCard(startcard);
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

    /**
     * The method shows to a player two possible secret objectives through the method toStringRule, since an Objective
     * Card is fully described only by its ScoreRule. See such method.
     * The player chooses between those two.
     *
     * @param obj1 is the first out of two secret objective proposed to the player.
     * @param obj2 is the second proposed secret objective.
     * @return the number corresponding to the player's choice (1 for the first objective, 2 for the second).
     */
    public int chooseObjective(ObjectiveCard obj1, ObjectiveCard obj2) throws IOException {
        int choice;
        /*System.out.println("Choose between two secret objectives:\n1- " + toStringRule(obj1) + "\n2- " + toStringRule(obj2));*/
        System.out.println("Choose between two secret objectives: ");
        AdvancedTUI_temp.printObjectives(new ObjectiveCard[]{obj1, obj2});
        Scanner scanner = new Scanner(System.in);
        do {
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice==1 || choice == 2));

        return choice;
    }


 // aggiungere un while (nel client) che lo fa andare finchè non è il proprio turno

    /**
     * The method allows a player (who is waiting for others to finish their turns) to check and see their hands,
     * their playground, which contains the secret objective, and every card on the ground. This can happen through
     * a menu with numeric options. Other methods are invoked to:
     * - see player's hand: showHand();
     * - see playground: showGround(game, player);
     * - see a card on the ground: showCard(card).
     *
     * @param game is the instance of the game that is being played.
     * @param player is the instance of the player who's waiting.
     */
    public boolean notYourTurn(Game game, Player  player){
        System.out.println("\nYou have to wait other players.\nWhat do you want to do? Select the number corresponding to your choice:\n1- Show play ground (and common objectives)\n2- Show hand\n3- Show card on ground\n4- wait your turn\n\n");
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice>0 && choice<4));
        switch (choice) {
            case 1 -> {
                showGround(game, player);
                return true;
            }
            case 2 -> {
                showHand(player);
                return true;
            }
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
                return true;
            }
            case 4 -> {
                return false;
            }
        }
        return false;
    }

    /**
     * The method implements the first part of a player's turn. As in notYourTurn, this method allows to check multiple
     * times the hand, the playground, which contains the secret objective, and every card on the ground.
     * When the choice is 4, which is "play card", there are no more choices available: the method ends, and it should
     * be followed by invocation of inputCoordinates() and inputCardToPlace();
     *
     * @param game is the instance of the game that is being played.
     * @param player is the instance of the player whose turn it is.
     * @exception IllegalStateException arises when an input error occurs.
     */
    public boolean yourTurnPlay(Game game, Player player){
        System.out.println("\n");
        AdvancedTUI_temp.printObjectives(game.getCommonObj());
        System.out.println("\n");
        AdvancedTUI_temp.printDeck(game.getDecks()[0]);
        AdvancedTUI_temp.printDeck(game.getDecks()[1]);
        System.out.println("\n");
        AdvancedTUI_temp.printGround(player.getPlayerGround());
        AdvancedTUI_temp.printResources(player.getPlayerGround().getTotalResources());
        System.out.println("\n");
        System.out.println("This is the list of positions where it is possible to place a card:\n");
        for (Position pos: player.getPlayerGround().getAvailablePositions()) {
            System.out.println("(" + pos.getX()+", " + pos.getY() + ") ");
        }
        System.out.println("\n");
        return true;
        /*
        System.out.println("It's your turn!\nWhat do you want to do? Select the number corresponding to your choice:\n1- Show play ground\n2- Show hand\n3- Show card on ground\n 4- Play card\n\n");
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

         */
    }

    /**
     * The method asks the player where to place the card. The client needs to input coordinates x and y to identify
     * a specific position on the play ground, seen as a matrix.
     *
     * @return desired position where to place the card.
     */
    public Position inputCoordinates(Player player){
        System.out.println("Where do you want to place the card? Insert the number of the corresponding position:\n");
        int position = -1;
        Scanner scanner = new Scanner(System.in);
        position = scanner.nextInt();
        while(position > player.getPlayerGround().getAvailablePositions().size() || position <= 0){
            System.out.println("Please insert a valid number:\n");
            position = scanner.nextInt();
        }
        Map<Integer, Position> numbers = player.getPlayerGround().getAvailableNumbers();
        Position position1 = numbers.get(position);
        return (new Position(position1.getX(), position1.getY()));
    }


    /**
     * The method asks the player which of the three cards in their hand they want to play, and show them the hand.
     * The player inputs 1, 2 or 3 and then must choose between placing it flipped (1) or not (0). If it's 1, the Card
     * objected gets flipped. The Playable Card object corresponding to the choice is returned to the server.
     *
     * @param player is the instance of the player who's playing.
     * @return the Playable Card to be placed, which is chosen from the player's hand.
     */
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

    /**
     * The method implements the second part of a player's turn: the drawing section. The player can see the hand how
     * many times he/she wants. When they finally choose to draw (input 2) the method chooseFromDecks() is invoked.
     * See such method for further details.
     * The return value of chooseFromDecks(), which is an integer, it's the number corresponding to the drawn card.
     *
     * @param game is the instance of the game that is being played.
     * @param player is the instance of the player who's playing.
     * @return numeric choice corresponding to Playable Card drawn from Resource deck or Golden deck.
     */
    public int yourTurnDraw(Game game, Player player){
        /*System.out.println("You placed one card. Now it's time to draw. What do you want to do? Select the number corresponding to your choice:\n1- Show hand\n2- Show decks and draw a card\n\n");
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
        }*/
        return chooseFromDecks(game, player);
    }

    /**
     * The method shows each playable card of the hand (there are three of them) plus the secret objective. It also
     * prints a legend with the acronym of each ScoreRule.
     *
     * @param player is the instance of the player who's waiting or playing.
     */
    public void showHand(Player player){
        AdvancedTUI_temp.printHand(player.getHand());
       /*Hand hand = player.getHand();
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
                */

    }

    /**
     * The method gets all the information about the scoring rule of a Card object. There is a complementary method
     * called in the same way that only accepts ObjectiveCard objects. See such method.
     * From the type of rule, the method is able to know how many and which other parameters are related to the rule.
     * All the information is gathered into a single string.
     *
     * @param card is (typically) a playable card.
     * @return a single string describing the scoring rule of the card passed as a parameter.
     */
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

    /**
     * The function of this method has been described in the homonym one that accepts Card objects as parameter.
     *
     * @param card is an Objective Card.
     * @return a single string describing the scoring rule of the objective card passed as a parameter.
     */
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


    /**
     * The method gets all the information (position, resource, availability) about each of the four corners
     * on the front of the card. The information is gathered in a single string.
     *
     * @param card is the Card object whose front corners need to be displayed.
     * @return a single string describing all the four corners of the card passed as a parameter.
     */
    public String toStringFrontCorners(Card card) {
        String z = "Front corners:\n";
        Corner[] cornersF = card.getCorners();
        for (int i = 0; i < 4; i++) {
            z = z.concat("- ").concat(cornersF[i].getPos()).concat(" ").concat(cornersF[i].getCornerRes().name().toLowerCase()).concat(" ").concat(Boolean.toString(cornersF[i].getAvailability())).concat("\n");
        }
        return z;
    }

    /**
     * The method gets all the information (position, resource, availability) about each of the four corners
     * on the back of the card. The information is gathered in a single string.
     *
     * @param card is the Card object whose back corners need to be displayed.
     * @return a single string describing all the four back corners of the card passed as a parameter.
     */
    public String toStringBackCorners(Card card){
        Corner[] cornersB = card.getBackCorners();
        String z = "Back corners:\n";
            for (int i = 0; i < 4; i++) {
                z = z.concat("- ").concat(cornersB[i].getPos()).concat(" ").concat(cornersB[i].getCornerRes().name().toLowerCase()).concat(" ").concat(Boolean.toString(cornersB[i].getAvailability())).concat("\n");
            }
            return z;
    }

    /**
     * The method gather in a single string the information about the requirements to play a card passed as parameter.
     *
     * @param card is the PlayableCard object whose requirements are being examined.
     * @return a single string describing how many occurrences are required of each type of resource.
     */
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

    /**
     * The method shows the card (of class PlayableCard) passed as a parameter. To do that, the method invokes other
     * methods to convert card's features into strings: toStringRule, toStringCorners, toStringBackCorners, toStringReq.
     * The combination of their results is printed to describe the whole card.
     *
     * @param card is the Playable Card to be displayed.
     */
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

    /**
     * The function of this method has been described in the homonym one that accepts StarterCard objects as parameter.
     * The only difference is that starter cards don't have any requirements.
     *
     * @param card is the StarterCard to be displayed.
     */
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

    /**
     * The method builds and shows the player's ground as a matrix of 1 and 0: 1 if there is a card in the position
     * identified by matrix coordinates, 0 otherwise. The method also shows the list of the current available positions.
     *
     * @param game is the instance of the game that is being played.
     * @param player is the instance of the player whose ground needs to be displayed.
     */
    public void showGround(Game game, Player player){
        ObjectiveCard[] commonObjs = game.getCommonObj();
        System.out.println("\nThese are all players' common objectives:\n");
        /* for(ObjectiveCard obj: commonObjs){
            System.out.println(toStringRule(obj));
        } */
        AdvancedTUI_temp.printObjectives(commonObjs);

        /*
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
        */
        //stampa della matrice
        System.out.println("Your play ground looks like this:\n");
        AdvancedTUI_temp.printGround(player.getPlayerGround());
        /*
        for (int i = 0; i < 84; i++) {
            for (int j = 0; j < 84; j++) {
                System.out.print(matrixToPrint[i][j] + " ");
            }
            System.out.println();
        } */

        // stampa available positions
        System.out.println("This is the list of positions where it is possible to place a card:\n");
        for (Position pos: player.getPlayerGround().getAvailablePositions()) {
            System.out.println("(" + pos.getX()+", " + pos.getY() + ") ");
        }
    }

    /**
     * The method implements the choice from decks when it's time to draw a card. There are 6 possibilities, since
     * there are two decks (Resource deck and Golden deck) and both of them have two cards face up and one card face
     * down (which is the top of the remaining deck).
     *
     * @param game is the instance of the game that is being played.
     * @return numeric choice corresponding to Playable Card drawn from Resource deck or Golden deck:
     * - 0 is one out of two cards from Resource deck that are on the ground and facing up
     * - 1 is the other card from Resource deck which is on the ground, facing up
     * - 2 is the first face down card at the top of Resource deck
     * - 3 same as 0 but from Golden deck
     * - 4 same as 1 but from Golden deck
     * - 5 same as 2 but from Golden deck
     */
    public int chooseFromDecks(Game game, Player player){
        Deck[] decks = game.getDecks();
        String deck1name = decks[0].getKindOfDeck();
        String deck2name = decks[1].getKindOfDeck();

        System.out.println("This is your hand: ");
        AdvancedTUI_temp.printHand(player.getHand());
/*
        Card deck1card1 = deck1.getFirst();
        Card deck1card2 = deck1.get(1);
        Card deck2card1 = deck2.getFirst();
        Card deck2card2 = deck2.get(1); */

        System.out.println("Here you are the game decks." +
               "" + deck1name + " deck (0, 1, 2 to choose):\n");
        /*
        showCard((PlayableCard) deck1card1);
        System.out.println("\nSecond card of the " + deck1name + " (1 to choose):\n");
        showCard((PlayableCard) deck1card2);
        System.out.println("\nAlternatively, you can input 2 to choose the hidden card at the top of the " + deck1name +".\n");
        System.out.println("First card of the " + deck2name + " (3 to choose):\n");
        showCard((PlayableCard) deck2card1);
        System.out.println("\nSecond card of the " + deck2name + " (4 to choose):\n");
        showCard((PlayableCard) deck2card2);
        System.out.println("\nAlternatively, you can input 5 to choose the hidden card at the top of the " + deck2name + "deck.\n");
        System.out.println("Which card do you choose? Input the number corresponding to your choice:\n"); */
        AdvancedTUI_temp.printDeck(decks[0]);
        System.out.println( "" + deck2name + " deck (3, 4, 5 to choose):\n");
        AdvancedTUI_temp.printDeck(decks[1]);
        Scanner scanner = new Scanner(System.in);
        int choice;
        do{
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice >=0 && choice<6));
        return choice;
    }

    // methods equivalent to yourturndraw + choosefromdeck
    public int yourTurnDraw2(Game game, Player player){
        System.out.println("You placed one card. Now it's time to draw." +
                "These are the card facing up from " + game.getDecks()[0].getKindOfDeck() +":");
        showCard((PlayableCard) game.getDecks()[0].getCards().getFirst());
        showCard((PlayableCard) game.getDecks()[0].getCards().get(1));
        System.out.println("And these are the cards facing up from " + game.getDecks()[1].getKindOfDeck()+":");
        showCard((PlayableCard) game.getDecks()[1].getCards().getFirst());
        showCard((PlayableCard) game.getDecks()[1].getCards().get(1));
        System.out.println("Otherwise you can draw from the top of one deck, where the card are facing down. " +
                "Which deck do you want to draw a card from? 0 for " + game.getDecks()[0].getKindOfDeck() +
                ", 1 for " + game.getDecks()[1].getKindOfDeck());
        Scanner scanner = new Scanner(System.in);
        int choice;
        do{
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice >=0 && choice<2));
        return choice;
    }
    public int drawnCard(Deck deck){
        System.out.println("You chose "+deck.getKindOfDeck()+". Which card do you want to draw? 0 for first card facing up, " +
                "1 for second card facing up, 2 for card at the top of the facing down deck.");
        Scanner scanner = new Scanner(System.in);
        int choice;
        do{
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice >=0 && choice<=2));
        return choice;
    }
    public void showRoom(ArrayList<Room> rooms){
        String s ="These are the available room to play:";
        if(rooms.isEmpty()){
            s+=" no room available";
        }
        else{
            for(Room r: rooms){
                s+=r.getName()+"\n";
            }
        }
        System.out.println(s);
    }
    public boolean chooseRoom(){
        System.out.println("Do you want to create a room: 1 or to join one : other");
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        s.nextLine();
        return n == 1;
    }
    public String getRoomName(boolean b, boolean choice){
        Scanner s = new Scanner(System.in);
        if(b){
            if(choice) {
                System.out.println("What is the name of the new room?");
            }else{
                System.out.println("Please, enter a name that is not already taken");
            }
        }
        else{
            if(choice){
                System.out.println("Which room?");
            }else{
                System.out.println("Please enter the name of a room that exist");
            }
        }
        return s.nextLine();
    }

    public void playerJoined(Player player){
        System.out.println("Player " + player.getNickname() + " has joined the room");
    }
    public boolean refreshRoom(){
        System.out.println("Do you want to update rooms list or not?\n1-yes\nother-no");
        Scanner s = new Scanner(System.in);
        return (s.nextInt()==1) ? true : false;
    }

    public static void cleanDebugLog() throws IOException {
        File file = new File(LOG_FILE_PATH);
        if (file.exists()) {
            file.delete();
            file.createNewFile();
        }
    }
}
