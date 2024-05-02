package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.InitGameController;
import it.polimi.ingsw.Model.*;

import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        Game game = new Game();
        //creare deck
        Deck[] decks = new Deck[2];
        game.setDecks(decks);
        //seleziononare carta obj comune
        ObjectiveCard[] obj = new ObjectiveCard[2];
        game.setCommonObj(obj);
        InitGameController c = new InitGameController(game);
        //c.InitializeGame();
        try {
            MyServerSocket ss = new MyServerSocket(59090, game,c);
            ss.runServer();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
