package it.polimi.ingsw.CS;

import it.polimi.ingsw.Controller.InitGameController;
import it.polimi.ingsw.Controller.ParsingController;
import it.polimi.ingsw.Model.*;
import org.json.simple.parser.ParseException;

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
    public static void parse(Game game) {
        ParsingController pc = new ParsingController();
        try {
            game.setStarterCards(pc.createStarterCardsArray());
            ArrayList<ObjectiveCard> list = pc.createObjectiveCardsArray();
            //altro parsing, chiedi a stefano
            Random rand = new Random();
            ObjectiveCard[] objs = {list.remove(rand.nextInt(list.size())),list.remove(rand.nextInt(list.size()))};
            game.setCommonObj(objs);
            game.setOtherObjs(list);
            //parsing deck
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
