package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.InitGameController;
import it.polimi.ingsw.Controller.ParsingController;
import it.polimi.ingsw.Model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Server {
    public static void main(String[] args) {
        Game game = new Game();
        parse(game);
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
    public static void parse(Game game){
        ParsingController pc = new ParsingController();
        game.setStarterCards(pc.parsingStarterCards());
        //altro parsing, chiedi a stefano
        ArrayList<ObjectiveCard> list = pc.parsingObjectiveCards();
        Random rand = new Random();
        ObjectiveCard[] objs = {list.remove(rand.nextInt(list.size())),list.remove(rand.nextInt(list.size()))};
        game.setCommonObj(objs);
        game.setOtherObjs(list);
        //parsing deck
    }
}
