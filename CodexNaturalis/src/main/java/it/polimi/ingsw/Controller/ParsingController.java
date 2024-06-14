package it.polimi.ingsw.Controller;
import it.polimi.ingsw.Model.*;

import it.polimi.ingsw.Model.ScoreRules.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * ParsingController class implements the controller in charge of managing parsing from json file to java objects.
 * The json file contains all the cards from Codex Naturalis and all their features. Methods are documented below.
 */
public class ParsingController {
    private final String filePath = "C:\\Users\\Ste\\Desktop\\Stefano\\UNI\\ANNO III\\INGEGNERIA DEL SOFTWARE\\PROGETTO_IDS\\ing-sw-2024-Parolin-Morato-Pedretti-Stagno\\CodexNaturalis\\src\\main\\java\\it\\polimi\\ingsw\\Resources\\carte.json";

    /**
     * The method adds a requirement (of a specific Resource) to the map that stores all the requirements for a card.
     * Since there are four types possible of each requirement (and there are up to five requirements) this method
     * increments by one only the type passed as a parameter.
     *
     *
     * @param map stores the requirements (initialized to 0 in parsingPlayableCards()).
     * @param res is the resource to be added to the requirements.
     */
    private void addRequirements (HashMap<Resource, Integer> map, Resource res) {
        int countMushroom, countBug, countFox, countLeaf;
        countMushroom = map.get(Resource.MUSHROOM);
        countBug = map.get(Resource.BUG);
        countFox = map.get(Resource.FOX);
        countLeaf = map.get(Resource.LEAF);

        if (res == Resource.MUSHROOM) {
            countMushroom++;
            map.put(Resource.MUSHROOM, countMushroom);
        }
        if (res == Resource.BUG) {
            countBug++;
            map.put(Resource.BUG, countBug);
        }
        if (res == Resource.FOX) {
            countFox++;
            map.put(Resource.FOX, countFox);
        }
        if (res == Resource.LEAF) {
            countLeaf++;
            map.put(Resource.LEAF, countLeaf);
        }
    }

    /**
     * The method maps (converts) a specific json word into its corresponding and same-meaning Resource enumeration type.
     * There are 9 possible cases.
     *
     *
     * @param resString is the word got from json file.
     * @return the assigned resource to the json word passed as a parameter.
     */
    private Resource assignResource (String resString) {
        return switch (resString) {
            case "plume" -> Resource.PLUME;
            case "potion" -> Resource.POTION;
            case "scroll" -> Resource.SCROLL;
            case "fox" -> Resource.FOX;
            case "mushroom" -> Resource.MUSHROOM;
            case "leaf" -> Resource.LEAF;
            case "bug" -> Resource.BUG;
            case "blank" -> Resource.BLANK;
            case "notavail" -> Resource.NOTVISIBLE;
            case null, default -> Resource.NOTVISIBLE;
        };
    }

    /**
     * The method builds the deck of golden cards, which are the cards from 41 to 80 presented in json file.
     * Object from class Deck is, in fact, filled with 40 PlayableCard objects, that are extracted from the list of
     * Playable Cards returned by parsingPlayableCards() method. See such method for information.
     *
     * @return deck of golden cards.
     */
    public Deck createGoldDeck () throws IOException, ParseException {
        Deck goldDeck;
        int numberOfCards = 40;
        String typeOfDeck = "Gold";
        ArrayList<PlayableCard> playableCards = parsingPlayableCards();
        ArrayList<PlayableCard> goldCards = new ArrayList<>();

        for (PlayableCard pc : playableCards) {
            if (pc.getId() > 40 && pc.getId() < 81)
                goldCards.add(pc);
        }

        goldDeck = new Deck(numberOfCards, typeOfDeck, goldCards);
        return goldDeck;
    }

    /**
     * See method parsingObjectiveCards() for further details.
     *
     * @exception IOException is thrown where there is a problem with input or output.
     * @exception ParseException is thrown where there is a problem in parsing.
     * @return return value of parsingObjectiveCards.
     */
    public ArrayList<ObjectiveCard> createObjectiveCardsArray () throws IOException, ParseException {
        return parsingObjectiveCards();
    }

    /**
     * The method builds the deck of resource cards, which are the first 40 cards presented in json file.
     * Object from class Deck is, in fact, filled with 40 PlayableCard objects.
     *
     * @return deck of resource cards.
     */
    public Deck createResDeck () throws IOException, ParseException {
        Deck resDeck;
        int numberOfCards = 40;
        String typeOfDeck = "Resource";
        ArrayList<PlayableCard> playableCards = parsingPlayableCards();
        ArrayList<PlayableCard> resCards = new ArrayList<>();
        for (PlayableCard pc : playableCards) {
            if (pc.getId() > 0 && pc.getId() < 41)
                resCards.add(pc);
        }

        resDeck = new Deck(numberOfCards, typeOfDeck, resCards);
        return resDeck;
    }

    /**
     * See method parsingStarterCards() for further details.
     *
     * @exception IOException is thrown where there is a problem with input or output.
     * @exception ParseException is thrown where there is a problem in parsing.
     * @return return value of parsingStarterCards.
     */
    public ArrayList<StarterCard> createStarterCardsArray () throws IOException, ParseException {
        return parsingStarterCards();
    }

    /**
     * The method uses information found in a json file to set the resources displayed on the back of a starter card.
     * There may be up to three resources. For each one of them, if not present, it's marked as blank.
     * If present, the method assignResource() converts json word into java Resource enumeration.
     *
     *
     * @param backResArray is an ordered sequence of values coming from a json file (in this case, all the information
     *                     coming from the back resource section of a card).
     * @return list of Resource objects obtained from json information.
     */
    private ArrayList<Resource> getBackRes (JSONArray backResArray) {
        ArrayList<Resource> backRes = new ArrayList<>();

        for (Object b : backResArray) {
            JSONObject backResObj = (JSONObject) b;

            String res1String = (String) backResObj.get("res1");
            Resource res1;
            if (Objects.equals(res1String, "none"))
                res1 = Resource.BLANK;
            else
                res1 = assignResource(res1String);
            backRes.add(res1);

            String res2String = (String) backResObj.get("res2");
            Resource res2;
            if (Objects.equals(res2String, "none"))
                res2 = Resource.BLANK;
            else
                res2 = assignResource(res2String);
            backRes.add(res2);

            String res3String = (String) backResObj.get("res3");
            Resource res3;
            if (Objects.equals(res3String, "none"))
                res3 = Resource.BLANK;
            else
                res3 = assignResource(res3String);
            backRes.add(res3);
        }

        return backRes;
    }

    /**
     * The method converts information found in a json file into attributes of a specific ScoreRule. The type of rule
     * is the first parameter findable in the section "rule" of a json card. On the base of its type, a rule may have
     * different types of other relevant information.
     *
     *
     * @param ruleArray is an ordered sequence of values coming from a json file (in this case, all the information
     *                  coming from the rule section of a card).
     * @return object of class ScoreRule obtained from json information.
     */
    private ScoreRule getScoreRule(JSONArray ruleArray) {
        ScoreRule rule = null;

        for (Object r : ruleArray) {
            JSONObject ruleObj = (JSONObject) r;

            String typeRule = (String) ruleObj.get("type");

            // FlatRule
            if (Objects.equals(typeRule, "FlatRule")) {
                int pointsFlatRule = Integer.parseInt((String) ruleObj.get("points"));
                rule = new FlatRule(pointsFlatRule);
            }

            // NSymbolsRule
            if (Objects.equals(typeRule, "NSymbolsRule")) {
                String resTypeString = (String) ruleObj.get("restype");
                Resource resTypeRes = assignResource(resTypeString);
                int resNum = Integer.parseInt((String) ruleObj.get("resnum"));
                int pointsNSymbolsRule = Integer.parseInt((String) ruleObj.get("points"));
                rule = new NSymbolsRule(resTypeRes, resNum, pointsNSymbolsRule);
            }

            // CoveredCornersRule
            if (Objects.equals(typeRule, "CoveredCornersRule"))
                rule = new CoveredCornersRule();

            // CompositionRule
            if (Objects.equals(typeRule, "CompositionRule")) {
                int points = Integer.parseInt((String) ruleObj.get("points"));

                String color1String = (String) ruleObj.get("color1");
                Resource color1Res = mapResourceToColor(color1String);
                String color2String = (String) ruleObj.get("color2");
                Resource color2Res = mapResourceToColor(color2String);
                String color3String = (String) ruleObj.get("color3");
                Resource color3Res = mapResourceToColor(color3String);

                Resource[] colors = {color1Res, color2Res, color3Res};

                int offsetx2 = Integer.parseInt((String)  ruleObj.get("offsetx2"));
                int offsety2 = Integer.parseInt((String) ruleObj.get("offsety2"));
                int offsetx3 = Integer.parseInt((String) ruleObj.get("offsetx3"));
                int offsety3 = Integer.parseInt((String) ruleObj.get("offsety3"));

                Position offset1 = new Position(offsetx2, offsety2);
                Position offset2 = new Position(offsetx3, offsety3);

                Position[] offset = {offset1, offset2};

                rule = new CompositionRule(offset, colors, points);
            }

            // OneOfEachRule
            if (Objects.equals(typeRule, "OneOfEachRule")) {
                rule = new OneOfEachRule();
            }

        }
        return rule;
    }

    /**
     * The method initializes a hash map used for storing cards' requirements: the key is of Resource type and the value
     * is an integer. Since there are no requirements in cards from Resource deck, their map stays initialized to 0 for
     * each key.
     *
     * @return initialized hash map.
     */
    private HashMap<Resource, Integer> initHashMap () {
        HashMap<Resource, Integer> map = new HashMap<>();
        map.put(Resource.BUG, 0);
        map.put(Resource.FOX, 0);
        map.put(Resource.MUSHROOM, 0);
        map.put(Resource.LEAF, 0);
        return map;
    }

    /**
     * The method maps (converts) a specific json word (representing a color) into a specific Resource enumeration type.
     * There are 4 possible cases.
     *
     *
     * @param color is the word got from json file.
     * @return the assigned resource to the json color word passed as a parameter.
     */
    private Resource mapResourceToColor (String color) {
        return switch (color) {
            case "red" -> Resource.MUSHROOM;
            case "violet" -> Resource.BUG;
            case "green" -> Resource.LEAF;
            case "blue" -> Resource.FOX;
            case null, default -> null;
        };
    }

    /**
     * The method converts information found in a json file into attributes of java objects of ObjectiveCard class.
     * Based on the json result, through the method getScoreRule, it sets the ScoreRule that fully describes the objective.
     *
     * @exception IOException is thrown where there is a problem with input or output.
     * @exception ParseException is thrown where there is a problem in parsing.
     * @return full list of Objective Cards.
     */
    private ArrayList<ObjectiveCard> parsingObjectiveCards() throws IOException, ParseException {
        int id;
        JSONParser jsonParser = new JSONParser();
        FileReader fileReader = new FileReader(filePath);
        Object obj = jsonParser.parse(fileReader);
        JSONObject jsonObject = (JSONObject) obj;
        JSONObject gameCards = (JSONObject) jsonObject.get("gamecards");
        JSONArray objectiveCardsArray = (JSONArray) gameCards.get("objectivecards");

        id = 87;
        ArrayList<ObjectiveCard> objectiveCards = new ArrayList<>();
        for (Object o : objectiveCardsArray) {
            JSONObject objectiveCardObj = (JSONObject) o;
            JSONArray ruleArray = (JSONArray) objectiveCardObj.get("rule");
            ScoreRule rule = getScoreRule(ruleArray);

            ObjectiveCard card = new ObjectiveCard(id, rule);
            objectiveCards.add(card);
            id ++;
        }

        return objectiveCards;
    }

    /**
     * The method converts information found in a json file into attributes of java objects of PlayableCard class.
     * Based on the json result, it sets card color, it creates two arrays of corners (front and back) and sets their
     * resource and availability, and it sets the requirements of each card. Resource cards (from 1 to 40 in json) have
     * no requirements, whereas Golden Cards (from 41 to 80) may have up to 5 requirements.
     *
     * @exception IOException is thrown where there is a problem with input or output.
     * @exception ParseException is thrown where there is a problem in parsing.
     * @return full list of Playable Cards.
     */
    public ArrayList<PlayableCard> parsingPlayableCards() throws IOException, ParseException {
        int id;
        JSONParser jsonParser = new JSONParser();
        FileReader fileReader = new FileReader(filePath);
        Object obj = jsonParser.parse(fileReader);
        JSONObject jsonObject = (JSONObject) obj;
        JSONObject gameCards = (JSONObject) jsonObject.get("gamecards");
        JSONObject cards = (JSONObject) gameCards.get("cards");
        JSONArray playableCard = (JSONArray) cards.get("playablecards");

        id = 1;
        ArrayList<PlayableCard> playableCards = new ArrayList<>();
        for (Object o : playableCard) {
            JSONObject playableCardObj = (JSONObject) o;

            // get the color of the card
            String color = (String) playableCardObj.get("color");
            Resource colorCard = mapResourceToColor(color);

            // get the resources and create every front corner
            String TLFString = (String) playableCardObj.get("TLFcorner");
            Resource TLFRes = assignResource(TLFString);
            Corner TLFCorner = new Corner("top left front", TLFRes, true);
            String TRFString = (String) playableCardObj.get("TRFcorner");
            Resource TRFRes = assignResource(TRFString);
            Corner TRFCorner = new Corner("top right front", TRFRes, true);
            String BLFString = (String) playableCardObj.get("BLFcorner");
            Resource BLFRes = assignResource(BLFString);
            Corner BLFCorner = new Corner("bottom left front", BLFRes, true);
            String BRFString = (String) playableCardObj.get("BRFcorner");
            Resource BRFRes = assignResource(BRFString);
            Corner BRFCorner = new Corner("bottom right front", BRFRes, true);

            // create the array of front corners
            Corner[] frontCorners = {TLFCorner, BLFCorner, TRFCorner,  BRFCorner};

            // set the availability of every corner that does not exist to false
            for (Corner c : frontCorners) {
                if (c.getCornerRes() == Resource.NOTVISIBLE)
                    c.setAvailable(false);
            }

            // get the resources and create every back corner
            String TLBString = (String) playableCardObj.get("TLBcorner");
            Resource TLBRes = assignResource(TLBString);
            Corner TLBCorner = new Corner("top left back", TLBRes, true);
            String TRBString = (String) playableCardObj.get("TRBcorner");
            Resource TRBRes = assignResource(TRBString);
            Corner TRBCorner = new Corner("top right back", TRBRes, true);
            String BLBString = (String) playableCardObj.get("BLBcorner");
            Resource BLBRes = assignResource(BLBString);
            Corner BLBCorner = new Corner("bottom left back", BLBRes, true);
            String BRBString = (String) playableCardObj.get("BRBcorner");
            Resource BRBRes = assignResource(BRBString);
            Corner BRBCorner = new Corner("bottom right back", BRBRes, true);

            // create the array of back corners
            Corner[] backCorners = {TLBCorner, BLBCorner, TRBCorner,  BRBCorner};

            // set the availability of every corner that does not exist to false
            for (Corner c : backCorners) {
                if (c.getCornerRes() == Resource.NOTVISIBLE)
                    c.setAvailable(false);
            }

            HashMap<Resource, Integer> requirements = initHashMap();
            // Resource cards have no requirements

            if (id >= 41) {
                // for gold cards only
                JSONArray requirementsArray = (JSONArray) playableCardObj.get("req");

                for (Object ob : requirementsArray) {
                    JSONObject requirementsObj = (JSONObject) ob;
                    Resource r1Res;
                    Resource r2Res;
                    Resource r3Res;
                    Resource r4Res;
                    Resource r5Res;

                    String r1String = (String) requirementsObj.get("r1");
                    String r2String = (String) requirementsObj.get("r2");
                    String r3String = (String) requirementsObj.get("r3");
                    String r4String = (String) requirementsObj.get("r4");
                    String r5String = (String) requirementsObj.get("r5");

                    if (!r1String.isEmpty()) {
                        r1Res = assignResource(r1String);
                        addRequirements(requirements, r1Res);
                    }
                    if (!r2String.isEmpty()) {
                        r2Res = assignResource(r2String);
                        addRequirements(requirements, r2Res);
                    }
                    if (!r3String.isEmpty()) {
                        r3Res = assignResource(r3String);
                        addRequirements(requirements, r3Res);
                    }
                    if (!r4String.isEmpty()) {
                        r4Res = assignResource(r4String);
                        addRequirements(requirements, r4Res);
                    }
                    if (!r5String.isEmpty()) {
                        r5Res = assignResource(r5String);
                        addRequirements(requirements, r5Res);
                    }
                }
            }

            JSONArray ruleArray = (JSONArray) playableCardObj.get("rule");
            ScoreRule rule = getScoreRule(ruleArray);

            PlayableCard card = new PlayableCard(id, rule, frontCorners, backCorners, colorCard, requirements);
            playableCards.add(card);
            id ++;
        }

        return playableCards;
    }

    /**
     * The method converts information found in a json file into attributes of java objects of StarterCard class.
     * Based on the json result, it creates two arrays of corners (front and back) and sets their resource and availability,
     * and it sets the back resources (visible in the center of the starter cards only) through the method getBackRes().
     * Starter cards don't have a unique color, so they are all set as blank.
     *
     * @exception IOException is thrown where there is a problem with input or output.
     * @exception ParseException is thrown where there is a problem in parsing.
     * @return full list of Starter Cards.
     */
    private ArrayList<StarterCard> parsingStarterCards() throws IOException, ParseException {
        int id;
        JSONParser jsonParser = new JSONParser();
        FileReader fileReader = new FileReader(filePath);
        Object obj = jsonParser.parse(fileReader);
        JSONObject jsonObject = (JSONObject) obj;
        JSONObject gameCards = (JSONObject) jsonObject.get("gamecards");
        JSONObject cards = (JSONObject) gameCards.get("cards");
        JSONArray starterCard = (JSONArray) cards.get("startercards");

        id = 81;
        ArrayList<StarterCard> starterCards = new ArrayList<>();
        for (Object o : starterCard) {
            JSONObject starterCardObj = (JSONObject) o;

            // get the resources and create every front corner
            String TLFString = (String) starterCardObj.get("TLFcorner");
            Resource TLFRes = assignResource(TLFString);
            Corner TLFCorner = new Corner("top left front", TLFRes, true);
            String TRFString = (String) starterCardObj.get("TRFcorner");
            Resource TRFRes = assignResource(TRFString);
            Corner TRFCorner = new Corner("top right front", TRFRes, true);
            String BLFString = (String) starterCardObj.get("BLFcorner");
            Resource BLFRes = assignResource(BLFString);
            Corner BLFCorner = new Corner("bottom left front", BLFRes, true);
            String BRFString = (String) starterCardObj.get("BRFcorner");
            Resource BRFRes = assignResource(BRFString);
            Corner BRFCorner = new Corner("bottom right front", BRFRes, true);

            // create the array of front corners
            Corner[] frontCorners = {TLFCorner, BLFCorner, TRFCorner, BRFCorner};

            // set the availability of every corner that does not exist to false
            for (Corner c : frontCorners) {
                if (c.getCornerRes() == Resource.NOTVISIBLE)
                    c.setAvailable(false);
            }

            // get the resources and create every back corner
            String TLBString = (String) starterCardObj.get("TLBcorner");
            Resource TLBRes = assignResource(TLBString);
            Corner TLBCorner = new Corner("top left back", TLBRes, true);
            String TRBString = (String) starterCardObj.get("TRBcorner");
            Resource TRBRes = assignResource(TRBString);
            Corner TRBCorner = new Corner("top right back", TRBRes, true);
            String BLBString = (String) starterCardObj.get("BLBcorner");
            Resource BLBRes = assignResource(BLBString);
            Corner BLBCorner = new Corner("bottom left back", BLBRes, true);
            String BRBString = (String) starterCardObj.get("BRBcorner");
            Resource BRBRes = assignResource(BRBString);
            Corner BRBCorner = new Corner("bottom right back", BRBRes, true);

            // create the array of back corners
            Corner[] backCorners = {TLBCorner, BLBCorner, TRBCorner,  BRBCorner};

            // set the availability of every corner that does not exist to false
            for (Corner c : backCorners) {
                if (c.getCornerRes() == Resource.NOTVISIBLE)
                    c.setAvailable(false);
            }

            JSONArray backResArray = (JSONArray) starterCardObj.get("backres");
            ArrayList<Resource> backRes = getBackRes(backResArray);

            StarterCard card = new StarterCard(id, new FlatRule(0), frontCorners, backCorners, Resource.BLANK, backRes);
            starterCards.add(card);
            id ++;
        }

        return starterCards;
    }

}
