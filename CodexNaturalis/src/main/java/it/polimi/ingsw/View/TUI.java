package it.polimi.ingsw.View;
import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.*;

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
        System.out.println("\n                                                                                           Your first card is this:\n");
        System.out.println("                                                                                           1                        2");
        AdvancedTUI_temp.printStartingCard(startcard);
        System.out.println("\n                                                                           Do you want to place it to the front (1) or flipped (2)?\n");
        Scanner scanner = new Scanner(System.in);
        int choice;
        boolean flag = false;
        do {
            choice = scanner.nextInt();
            if(choice == 1){
                flag = true;
            }
            scanner.nextLine();
        } while(!(choice == 1 || choice == 2));
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
    public int chooseObjective(ObjectiveCard obj1, ObjectiveCard obj2) {
        int choice;
        System.out.println("\n                                                                                     Choose between two secret objectives:\n");
        System.out.println("                                                                                           1                        2");
        AdvancedTUI_temp.printObjectives(new ObjectiveCard[]{obj1, obj2});
        Scanner scanner = new Scanner(System.in);
        do {
            choice = scanner.nextInt();
            scanner.nextLine();
        } while(!(choice==1 || choice == 2));

        return choice;
    }

    public void printOtherGrounds(Game game, Player  player){
        for(Player player1 : game.getPlayers()){
            if(!Objects.equals(player1.getNickname(), player.getNickname())){
                System.out.println("\n                                                                               " + player1.getNickname() +" has " + player1.getPlayerGround().getPlayerScore() + " points. This is it's board:\n");
                AdvancedTUI_temp.printGround(player1.getPlayerGround());
            }
        }
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
     * @param game   is the instance of the game that is being played.
     * @param player is the instance of the player who's waiting.
     */
    public void notYourTurn(Game game, Player  player){
        Deck[] decks = game.getDecks();
        System.out.println("\n");
        System.out.println("                                                   RESOURCE DECK:                                                                                 GOLD DECK:");
        AdvancedTUI_temp.printDecks(decks[0], decks[1], "                        ");
        System.out.println("\n");
        AdvancedTUI_temp.printGround(player.getPlayerGround());
        System.out.println("\n");
        System.out.println("                                                                                                    SECRET                                                  ");
        System.out.println("                                                    YOUR HAND:                                     OBJECTIVE                                                         COMMON OBJECTIVES:");

        AdvancedTUI_temp.printHand(player.getHand(), game.getCommonObj());
        System.out.println("\n                                                                   It's not your turn. You have to wait until the other players finish to play.\n");
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

        Deck[] decks = game.getDecks();
        System.out.println("\n");
        System.out.println("                                                   RESOURCE DECK:                                                                                 GOLD DECK:");
        AdvancedTUI_temp.printDecks(decks[0], decks[1], "                        ");
        System.out.println("\n");
        System.out.println("\n                                                                                                YOU HAVE " + player.getPlayerGround().getPlayerScore() + " POINTS!\n");
        AdvancedTUI_temp.printGround(player.getPlayerGround());
        System.out.println("\n");
        return true;
    }

    /**
     * The method asks the player where to place the card. The client needs to input coordinates x and y to identify
     * a specific position on the play ground, seen as a matrix.
     *
     * @return desired position where to place the card.
     */

    public Position inputCoordinates(Player player) {
        AdvancedTUI_temp.printGround(player.getPlayerGround());
        System.out.println("\n                                                              Where do you want to place the card? Insert the number of the corresponding position:\n");
        Scanner scanner = new Scanner(System.in);
        int position = -1;

        while (true) {
            try {
                position = Integer.parseInt(scanner.nextLine().trim());

                if (position > 0 && position <= player.getPlayerGround().getAvailablePositions().size()) {
                    break; // Valid position entered, exit loop
                } else {
                    System.out.println("                                                                                   Please enter a valid position number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("                                                                                     Invalid input. Please enter a valid number.");
            }
        }

        // Retrieve and return the corresponding Position object
        Map<Integer, Position> availableNumbers = player.getPlayerGround().getAvailableNumbers();
        return new Position(availableNumbers.get(position).getX(), availableNumbers.get(position).getY());
    }

    /**
     * The method asks the player which of the three cards in their hand they want to play, and show them the hand.
     * The player inputs 1, 2 or 3 and then must choose between placing it flipped (1) or not (0). If it's 1, the Card
     * objected gets flipped. The Playable Card object corresponding to the choice is returned to the server.
     *
     * @param player is the instance of the player who's playing.
     * @return the Playable Card to be placed, which is chosen from the player's hand.
     */

    public PlayableCard inputCardToPlace(Game game, Player player) {
        showHand(game, player);
        PlayableCard cardToPlay = null;
        Scanner scanner = new Scanner(System.in);
        while (cardToPlay == null) {
            try {
                int card = Integer.parseInt(scanner.nextLine().trim());
                if (card >= 1 && card <= 3) {
                    cardToPlay = (PlayableCard) player.getHand().getCard(card - 1);
                }
            } catch (NumberFormatException e) {
                System.out.println("                                                                              Invalid input. Please enter a number between 1 and 3");
            }
        }

        System.out.println("                                                                                           1                      2");
        AdvancedTUI_temp.printCard(cardToPlay);
        System.out.println("\n                                                                                       To the front (1) or flipped (2)?");

        while (true) {
            try {
                int flip = Integer.parseInt(scanner.nextLine().trim());
                if (flip == 1 || flip == 2) {
                    if (flip == 2) {
                        cardToPlay.flipCard();
                    }else{
                        if(!player.getPlayerGround().checkRequirements(cardToPlay)){
                            System.out.println("\n                                                                You don't have enough resources, choose another card or play that card flipped\n");
                            return null;
                        }
                    }
                    break;
                } else {
                    System.out.println("\n                                                                            Invalid choice. Please enter 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n                                                                                Invalid input. Please enter 1 or 2.");
            }
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
        return chooseFromDecks(game, player);
    }

    /**
     * The method shows each playable card of the hand (there are three of them) plus the secret objective. It also
     * prints a legend with the acronym of each ScoreRule.
     *
     * @param player is the instance of the player who's waiting or playing.
     */
    public void showHand(Game game, Player player){
        System.out.println("                                                     YOUR HAND:                                     SECRET                                                  ");
        System.out.println("                                  1                      2                      3                  OBJECTIVE                                                         COMMON OBJECTIVES:");

        AdvancedTUI_temp.printHand(player.getHand(), game.getCommonObj());
        System.out.println("\n                                                                                     Choose a card in your hand to play (1 to 3):\n");
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
        System.out.println("                                                                                                       COMMON OBJECTIVES:");
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
        AdvancedTUI_temp.printGround(player.getPlayerGround());
        System.out.println("                                                                             SECRET");
        System.out.println("                                         YOUR HAND:                         OBJECTIVE                                                         COMMON OBJECTIVES:");
        AdvancedTUI_temp.printHand(player.getHand(), game.getCommonObj());
        System.out.println("\n");
        System.out.println("                                                   RESOURCE DECK:                                                                                 GOLD DECK:");
        System.out.println("                                  1                      2                      3                                              4                      5                      6");
        AdvancedTUI_temp.printDecks(decks[0], decks[1], "                        ");
        System.out.println("\n                                                              Choose a card to draw from the decks (1 to 6) or check other player's boards (7)\n");
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        boolean valid = false;

        do {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 6) {
                    valid = true;
                } else if(choice == 7){
                    printOtherGrounds(game, player);
                    chooseFromDecks(game, player);
                } else {
                    System.out.println("                                                                            Invalid input. Please enter a number between 1 and 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("                                                                                Invalid input. Please enter a number between 1 and 6.");
            }
        } while (!valid);

        return choice;
    }


    public void showRoom(ArrayList<Room> rooms){
        if(rooms.isEmpty()){
            System.out.println("\nNo room is available!:\n");
        }
        else{
            System.out.println("These are the available rooms to play in:\n");
            AdvancedTUI_temp.printRoomsTable(rooms);
        }
    }
    public boolean chooseRoom() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Create a room");
        System.out.println("2. Join an existing room");
        System.out.print("Enter your choice (1 or 2): ");

        int choice = 0;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice == 1 || choice == 2) {
                    break; // Valid choice entered, exit loop
                } else {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                    System.out.print("Enter your choice (1 or 2): ");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                System.out.print("Enter your choice (1 or 2): ");
            }
        }

        return choice == 1;
    }
    public String getRoomName(boolean b, ArrayList<Room> rooms){
        Scanner s = new Scanner(System.in);
        boolean nameOk = true;
        String name;
        if(b){
            do{
                System.out.println("What is the name of the new room?");
                name = s.nextLine();
                for(Room r : rooms){
                    if(name.equals(r.getName())){
                        System.out.println("Please, enter a name that is not already taken");
                        nameOk = false;
                        break;
                    }
                }
            } while(!nameOk);
        }
        else{
            boolean check = false;
            do{
                System.out.println("Which room?");
                name = s.nextLine();
                for(Room r : rooms){
                    if(name.equals(r.getName())){
                        check = true;
                    }
                }
                if(!check){
                    System.out.println("Please enter a valid room name");
                    nameOk = false;
                }
            } while(!nameOk);
        }
        return name;
    }

    public void playerJoined(Player player){
        System.out.println("Player " + player.getNickname() + " has joined the room");
    }

    public String chooseColor(Set<String> colors){
        System.out.println("Choose a color: ");
        for(String s : colors){
            System.out.println(s);
        }
        Scanner s = new Scanner(System.in);
        String color = s.nextLine();
        while (!colors.contains(color)){
            System.out.println("Wrong color. Type a valid color");
            color = s.nextLine();
        }
        return color;
    }
}
