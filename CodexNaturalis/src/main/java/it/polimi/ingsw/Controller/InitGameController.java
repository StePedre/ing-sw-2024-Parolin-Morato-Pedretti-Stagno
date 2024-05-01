package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.View.CLI;

import java.util.ArrayList;
import java.util.Random;

// This controller does the following:
// - Initializes the board, decks and hands of each player (BOARD AND HAND DONE!)
// - Sets the first player to play (DONE!)
// - Places the starting card on the board (DONE!)
// - Sets the secret objective for each player (DONE!)
public class InitGameController {
    private Game game = null;
    private Player firstPlayer;
    // decks are created in the parsing controller
    private ObjectiveCard[] secretObjectives;
    private ObjectiveCard[] commonObjectives;
    private StarterCard[] startingCards;
    private CLI cliPlayer1;
    private CLI cliPlayer2;
    private CLI cliPlayer3;
    private CLI cliPlayer4;

    public InitGameController(Game game){
        this.game=game;
    }
    public void InitializeGame () {
        firstPlayer = setFirstPlayer(game.getPlayers(), game.getNumPlayer());
        // select first player

        game.getDecks()[0].shuffle();
        game.getDecks()[1].shuffle();
        // shuffle R and G decks and reveal two cards

        placeStartingCard(game, startingCards);
        // place initial card

        game.getDecks()[2].shuffle();
        // shuffle O decks and reveal two cards

        selectSecretObj(game, secretObjectives);
        // select secret objective

        populateHand(game);
        // create hand for each player
    }

    private Player setFirstPlayer (ArrayList<Player> players, int numberOfPlayers) {
        Random rand = new Random();
        int indexFirstPlayer = rand.nextInt(numberOfPlayers);
        Player firstPlayer = players.get(indexFirstPlayer);
        return firstPlayer;
    }

    // selects the secret objective for each player
    private void selectSecretObj (Game game, ObjectiveCard[] secretObjectives) {
        ObjectiveCard chosenSecretObj;
        for (int i = 0; i < game.getPlayers().size(); i++) {
            // wait for decision made by the player through the GUI...
            // receives selected card
            // chosenSecretObj = secretObjectives[0] or secretObjectives[1]
            game.getPlayers().get(i).getHand().setSecretObj(chosenSecretObj);
        }
    }

    // place the starting card for each player on their player ground
    private void placeStartingCard (Game game, StarterCard[] startingCards) {
        ArrayList<Player> players = game.getPlayers();
        StarterCard startingCard;
        Position startingPosition = new Position(42,42);
        int randomIndex;

        for ( int i = 0; i < players.size(); i++) {
            Random rand = new Random();
            randomIndex = rand.nextInt(startingCards.length);
            startingCard = startingCards[randomIndex];

            // add ability to choose the side of starter card
            // remove starer card from their deck
            try {
                players.get(i).getPlayerGround().placeCard(startingCard, startingPosition);
                players.get(i).getCLI().showStarterCard (startingCard);
            } catch (InvalidPositionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // populate the hand of each player
    private void populateHand (Game game) {
        Deck deckResource;
        Deck deckGold;
        ArrayList<Player> players;
        int indexRandCard1, indexRandCard2, indexRandCard3;
        // these are the index of the random cards to add to each hand

        deckResource = game.getDecks()[0];
        deckGold = game.getDecks()[1];
        players = game.getPlayers();

        for (int i = 0; i < players.size(); i++) {
            Random rand = new Random();
            Card[] cards = new Card[3];

            indexRandCard1 = rand.nextInt(deckResource.getNumberOfCards()) + 2;
            indexRandCard2 = rand.nextInt(deckResource.getNumberOfCards()) + 2;
            indexRandCard3 = rand.nextInt(deckGold.getNumberOfCards()) + 2;
            // +2 avoids the selection of cards that are revealed on the ground
            cards[0] = deckResource.drawCard(indexRandCard1);
            cards[1] = deckResource.drawCard(indexRandCard2);
            cards[2] = deckGold.drawCard(indexRandCard3);
            // cards in positions 0 and 1 are resources
            // card in position 2 is gold
            Hand hand = new Hand(cards);
            players.get(i).setHand(hand);
        }
    }

}
