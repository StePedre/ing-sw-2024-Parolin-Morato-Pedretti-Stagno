package it.polimi.ingsw.Controller;
import it.polimi.ingsw.Model.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;




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

            // get the resources and create every front corner
            Resource TLF = (Resource) playableCardObj.get("TLFcorner");
            Corner TLFCorner = new Corner("top left front", TLF, true);
            Resource TRF = (Resource) playableCardObj.get("TRFcorner");
            Corner TRFCorner = new Corner("top right front", TRF, true);
            Resource BLF = (Resource) playableCardObj.get("BLFcorner");
            Corner BLFCorner = new Corner("bottom left front", BLF, true);
            Resource BRF = (Resource) playableCardObj.get("BRFcorner");
            Corner BRFCorner = new Corner("bottom right front", BRF, true);

            // create the array of front corners
            Corner[] frontCorners = {TLFCorner, TRFCorner, BLFCorner, BRFCorner};

            // set the availability of every corner that does not exist to false
            for (Corner c : frontCorners) {
                if (c.getCornerRes() == Resource.NOTVISIBLE)
                    c.setAvailable(false);
            }

            // get the resources and create every back corner
            Resource TLB = (Resource) playableCardObj.get("TLBcorner");
            Corner TLBCorner = new Corner("top left back", TLB, true);
            Resource TRB = (Resource) playableCardObj.get("TRBcorner");
            Corner TRBCorner = new Corner("top right back", TRB, true);
            Resource BLB = (Resource) playableCardObj.get("BLBcorner");
            Corner BLBCorner = new Corner("bottom left back", BLB, true);
            Resource BRB = (Resource) playableCardObj.get("BRBcorner");
            Corner BRBCorner = new Corner("bottom right back", BRB, true);

            // create the array of back corners
            Corner[] backCorners = {TLBCorner, TRBCorner, BLBCorner, BRBCorner};

            // set the availability of every corner that does not exist to false
            for (Corner c : backCorners) {
                if (c.getCornerRes() == Resource.NOTVISIBLE)
                    c.setAvailable(false);
            }

            if (id < 41) {
                // do req for resource cards (set keys in hashmap and all integers to 0)
            }
            else {
                // do req for gold cards
            }

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
}
