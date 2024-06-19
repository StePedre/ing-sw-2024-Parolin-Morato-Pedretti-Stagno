package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.*;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ParsingControllerTest {

    ParsingController pc = new ParsingController();
    private void printCard(String s, Corner[] fc, Corner[] bc, HashMap<Resource, Integer> req){
        System.out.println(s);
        for(Corner c: fc){
            System.out.println(c.getPos() + " " + c.getAvailability() + " " + c.getCornerRes().name());
        }
        for(Corner c: bc){
            System.out.println(c.getPos() + " " + c.getAvailability() + " " + c.getCornerRes().name());
        }
        System.out.println(req);
    }
    @Test
    void parsingPlayableCards() throws IOException, ParseException {
        ArrayList<PlayableCard> cards = pc.parsingPlayableCards();
        Random r = new Random();
        int id = r.nextInt(79);
        assertEquals(cards.get(id).getId(), id+1);
        printCard(cards.get(id).getRule().getName(), cards.get(id).getCorners(), cards.get(id).getBackCorners(), cards.get(id).getRequirements());
    }

    @Test
    void createGoldDeck() throws IOException, ParseException {
        Deck deck = pc.createGoldDeck();
        Random r = new Random();
        int id = r.nextInt(39);
        PlayableCard card = deck.drawCard(id);
        if(card.getId() == 40+id+1){
            System.out.println(true);
        }
        printCard(card.getRule().getName(), card.getCorners(), card.getCorners(), card.getRequirements());
    }

    @Test
    void createObjectiveCardsArray() throws IOException, ParseException {
        ArrayList<ObjectiveCard> cards = pc.createObjectiveCardsArray();
        Random r = new Random();
        int id = r.nextInt(15);
        assertEquals(cards.get(id).getId(), 86+id+1);
        System.out.println((cards.get(id).getRule().getName()));
    }

    @Test
    void createResDeck() throws IOException, ParseException {
        Deck deck = pc.createResDeck();
        Random r = new Random();
        int id = r.nextInt(39);
        PlayableCard card = deck.drawCard(id);
        if(card.getId() == id+1){
            System.out.println(true);
        }
        printCard(card.getRule().getName(), card.getCorners(), card.getCorners(), card.getRequirements());
    }

    @Test
    void createStarterCardsArray() throws IOException, ParseException {
        ArrayList<StarterCard> cards = pc.createStarterCardsArray();
        Random r = new Random();
        int id = r.nextInt(5);
        assertEquals(cards.get(id).getId(), 80+id+1);
        System.out.println(cards.get(id).getRule().getName());
        for(Corner c: cards.get(id).getCorners()){
            System.out.println(c.getPos() + " " + c.getAvailability() + " " + c.getCornerRes().name());
        }
        for(Corner c: cards.get(id).getBackCorners()){
            System.out.println(c.getPos() + " " + c.getAvailability() + " " + c.getCornerRes().name());
        }
        System.out.println(cards.get(id).getBackRes());
    }
}