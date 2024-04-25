package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

// This controller does the following:
// - Initializes the board, decks and hands of each player (BOARD AND HAND DONE!)
// - Sets the first player to play (DONE!)
// - Places the starting card on the board (TO DO)
// - Sets the secret objective for each player (DONE!)
public class InitGameController {

    public void InitGame () {
        ArrayList<Player> players;
        int numberOfPlayers;
        Player firstPlayer;
        Deck[] decks;
        // decks are created in the parsing controller
        ObjectiveCard[] secretObjectives;
        ObjectiveCard[] commonObjectives;

        numberOfPlayers = players.size();

        firstPlayer = setFirstPlayer(players, numberOfPlayers);
        // select first player

        Game game = new Game(players, numberOfPlayers, decks, commonObjectives, firstPlayer);
        // create the game

        game.getDecks()[0].shuffle();
        game.getDecks()[1].shuffle();
        // shuffle R and G decks and reveal two cards


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

    private void selectSecretObj (Game game, ObjectiveCard[] secretObjectives) {
        ObjectiveCard chosenSecretObj;
        for (int i = 0; i < game.getPlayers().size(); i++) {
            // wait for decision made by the player through the GUI...
            // receives selected card
            // chosenSecretObj = secretObjectives[0] or secretObjectives[1]
            game.getPlayers().get(i).getHand().setSecretObj(chosenSecretObj);
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
