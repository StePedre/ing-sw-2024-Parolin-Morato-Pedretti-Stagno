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


public class ParsingController {
    ArrayList<PlayableCard> playableCards;
    ArrayList<StarterCard> starterCards;
    ArrayList<ObjectiveCard> objectiveCards;

    public ArrayList<PlayableCard> parsingPlayableCards() throws IOException, ParseException {
        int id;
        JSONParser jsonParser = new JSONParser();
        FileReader fileReader = new FileReader("CodexNaturalis/src/main/java/Resources/carte.json");
        Object obj = jsonParser.parse(fileReader);
        JSONObject jsonObject = (JSONObject) obj;
        JSONObject gameCards = (JSONObject) jsonObject.get("gamecards");
        JSONObject cards = (JSONObject) gameCards.get("cards");
        JSONArray playableCard = (JSONArray) cards.get("playablecards");

        id = 1;
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
            Corner[] frontCorners = {TLFCorner, TRFCorner, BLFCorner, BRFCorner};

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
            Corner[] backCorners = {TLBCorner, TRBCorner, BLBCorner, BRBCorner};

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
            ScoreRule rule = null;

            for (Object r : ruleArray) {
                JSONObject ruleObj = (JSONObject) r;

                String typeRule = (String) ruleObj.get("type");
                if (Objects.equals(typeRule, "FlatRule")) {
                    int pointsFlatRule = (int) ruleObj.get("points");
                    rule = new FlatRule(pointsFlatRule);
                }
                if (Objects.equals(typeRule, "NSymbolsRule")) {
                    String resTypeString = (String) ruleObj.get("restype");
                    Resource resTypeRes = assignResource(resTypeString);
                    int resNum = (int) ruleObj.get("resnum");
                    int pointsNSymbolsRule = (int) ruleObj.get("points");
                    rule = new NSymbolsRule(resTypeRes, resNum, pointsNSymbolsRule);
                }

            }

            Card card = new PlayableCard(id, rule, frontCorners, backCorners, colorCard, requirements);
            id ++;
        }

        return playableCards;

    }

    public ArrayList<StarterCard> parsingStarterCards() {

        return starterCards;

    }
    public ArrayList<ObjectiveCard> parsingObjectiveCards() {

        return objectiveCards;

    }

    private HashMap<Resource, Integer> initHashMap () {
        HashMap<Resource, Integer> map = new HashMap<Resource, Integer>();
        map.put(Resource.BUG, 0);
        map.put(Resource.FOX, 0);
        map.put(Resource.MUSHROOM, 0);
        map.put(Resource.LEAF, 0);
        return map;
    }

    // map json string to the correct resource
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
            case null, default -> null;
        };
    }

    private Resource mapResourceToColor (String color) {
        return switch (color) {
            case "red" -> Resource.MUSHROOM;
            case "violet" -> Resource.BUG;
            case "green" -> Resource.LEAF;
            case "blue" -> Resource.FOX;
            case null, default -> null;
        };
    }

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

    private ScoreRule getScoreRule(JSONArray ruleArray) {
        ScoreRule rule = null;

        for (Object r : ruleArray) {
            JSONObject ruleObj = (JSONObject) r;

            String typeRule = (String) ruleObj.get("type");

            // FlatRule
            if (Objects.equals(typeRule, "FlatRule")) {
                int pointsFlatRule = (int) ruleObj.get("points");
                rule = new FlatRule(pointsFlatRule);
            }

            // NSymbolsRule
            if (Objects.equals(typeRule, "NSymbolsRule")) {
                String resTypeString = (String) ruleObj.get("restype");
                Resource resTypeRes = assignResource(resTypeString);
                int resNum = (int) ruleObj.get("resnum");
                int pointsNSymbolsRule = (int) ruleObj.get("points");
                rule = new NSymbolsRule(resTypeRes, resNum, pointsNSymbolsRule);
            }

            // CoveredCornersRule
            if (Objects.equals(typeRule, "CoveredCornersRule"))
                rule = new CoveredCornersRule();

            // CompositionRule
            if (Objects.equals(typeRule, "CompositionRule")) {
                Position[] offset;

                String color1String = (String) ruleObj.get("color1");
                Resource color1Res = mapResourceToColor(color1String);
                String color2String = (String) ruleObj.get("color2");
                Resource color2Res = mapResourceToColor(color2String);
                String color3String = (String) ruleObj.get("color3");
                Resource color3Res = mapResourceToColor(color3String);

                Resource[] colors = {color1Res, color2Res, color3Res};

            }

            // OneOfEachRule
            if (Objects.equals(typeRule, "OneOfEachRule")) {
                rule = new OneOfEachRule();
            }

        }
        return rule;
    }
}
