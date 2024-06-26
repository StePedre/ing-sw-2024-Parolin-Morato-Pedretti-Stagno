package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * The class Controller is the controller of the Application.
 * The needed Nodes from every scene in the fxml file have an id, this controller matches them with their correct id in
 * order to have their references in the class.
 * The class also has a Screen object to get an instance of the screen of the user, and three Strings which contain the
 * path of the needed resources.
 * There are a lot of getter methods that are used to get a specific Node: the return Object depends on the type of Node.
 *
 */

public class Controller {
    @FXML
    private ToggleButton red, blue, green, yellow;
    @FXML
    private ToggleGroup colors;
    @FXML
    private AnchorPane playGroundAnchor;
    @FXML
    private HBox playGroundHBox;
    @FXML
    private GridPane gridPaneGroundOther, gridPaneGround;
    @FXML
    private AnchorPane anchor2, anchor3, anchor4, anchor5, anchor6, anchor7, anchor8, anchor9, nickAnchorPane, anchor10;
    @FXML
    private HBox hBoxStart;
    @FXML
    private RadioButton buttonR, buttonL;
    @FXML
    private ImageView frontStarterCard, backStarterCard, secretObjLeft, secretObjRight, backgroundIV, sadFace, sadFace2;
    @FXML
    private Button finishButton, errorButton, finishButton2, buttonStart, confirmRoom, confirmColor, IpButton, flipButton, yourTurnButton, notYourTurnButton;
    @FXML
    private TextField nickTextField, numberPlayersTF, textFieldRoom, IpField;
    @FXML
    private Label score,name, nickLabel, labelNick, label20p, labelRoom, validLabel, winnerName, winnersNames, startLabel, startLabel2, colorValidLabel;
    @FXML
    private ProgressBar waitingBar;
    @FXML
    private ComboBox<Label> menu;
    @FXML
    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();
    @FXML
    private ImageView resUp1, resUp2, resDeck, goldUp1, goldUp2, goldDeck, yourPawn, pawn2, pawn3, pawn4;
    @FXML
    private Label labelPoints, labelPoints2, labelPoints3, labelPoints4, mushroomNum, bugNum, leafNum, foxNum, potionNum, scrollNum, plumeNum;
    @FXML
    private ImageView handCardLeft, handCardCenter, handCardRight, secretObj, logo;
    @FXML
    private ImageView commonObj1, commonObj2;
    @FXML
    private Button nickButton, requestButton;
    private final ArrayList<Position> availablePos = new ArrayList<>();
    private final String emptyImagePath = String.valueOf(getClass().getResource("border_image.png"));
    private final String imagesFrontPath = String.valueOf(getClass().getResource( "CODEX_cards_gold_front"))+"/";
    private final String imagesBackPath = String.valueOf(getClass().getResource("CODEX_cards_gold_back"))+"/";
    private PlayableCard currentHandLeft, currentHandCenter, currentHandRight;
    private String nick2, nick3, nick4;
    private PlayableCard cardPlayed;
    private Position posPlayed;
    ArrayList<ImageView> imageViewArrayList = new ArrayList<>();

    /**
     * This method sets the Images of the cards to be drawn in the draw panel.
     * If a card is NULL, then the empty image is set.
     *
     * @param decks is the array that contains the two decks (resource and gold).
     */
    public void addCards(Deck[] decks) {
        if (decks[0].getCards().getFirst() == null) {
            resUp1.setImage(new Image("file:" + emptyImagePath));
        } else {
            resUp1.setImage(new Image("file:" + imagesFrontPath + decks[0].getCards().getFirst().getId() + ".png"));
        }
        if (decks[0].getCards().get(1) == null) {
            resUp2.setImage(new Image("file:" + emptyImagePath));
        } else {
            resUp2.setImage(new Image("file:" + imagesFrontPath + decks[0].getCards().get(1).getId() + ".png"));
        }
        if (decks[0].getCards().get(2) == null) {
            resDeck.setImage(new Image("file:" + emptyImagePath));
        } else {
            resDeck.setImage(new Image("file:" + imagesBackPath + decks[0].getCards().get(2).getId() + ".png"));
        }
        if (decks[1].getCards().getFirst() == null) {
            goldUp1.setImage(new Image("file:" + emptyImagePath));
        } else {
            goldUp1.setImage(new Image("file:" + imagesFrontPath + decks[1].getCards().getFirst().getId() + ".png"));
        }
        if (decks[1].getCards().get(1) == null) {
            goldUp2.setImage(new Image("file:" + emptyImagePath));
        } else {
            goldUp2.setImage(new Image("file:" + imagesFrontPath + decks[1].getCards().get(1).getId() + ".png"));
        }
        if (decks[1].getCards().get(2) == null) {
            goldDeck.setImage(new Image("file:" + emptyImagePath));
        } else {
            goldDeck.setImage(new Image("file:" + imagesBackPath + decks[1].getCards().get(2).getId() + ".png"));
        }
    }

    /**
     * This method sets the Images of the common objective cards.
     *
     * @param commonObj is the array that contains the two common objective cards.
     */
    public void addCommonObj(ObjectiveCard[] commonObj) {
        commonObj1.setImage(new Image("file:" + imagesFrontPath + commonObj[0].getId() + ".png"));
        commonObj2.setImage(new Image("file:" + imagesFrontPath + commonObj[1].getId() + ".png"));
    }

    /**
     * This method displays the hand, the secret objective, the correct score and the total resources of the player and
     * the common objectives.
     *
     * @param g is the instance of the Game.
     * @param p is the instance of the Player.
     */
    public void addGround(Game g, Player p) {
        labelPoints.setText("Points: " + p.getPlayerGround().getPlayerScore());
        for (Player player : g.getPlayers()) {
            if (!player.getNickname().equals(p.getNickname())) {
                if (player.getNickname().equals(nick2)) {
                    labelPoints2.setText(nick2 + ": " + player.getPlayerGround().getPlayerScore());
                    colorToPawn(player.getColor(), pawn2);
                } else if (player.getNickname().equals(nick3)) {
                    labelPoints3.setText(nick3 + ": " + player.getPlayerGround().getPlayerScore());
                    colorToPawn(player.getColor(), pawn3);
                } else if (player.getNickname().equals(nick4)) {
                    labelPoints4.setText(nick4 + ": " + player.getPlayerGround().getPlayerScore());
                    colorToPawn(player.getColor(), pawn4);
                }
            }
        }
        addImages(p.getHand());
        addSecretObj(p.getHand().getObjCard());
        addCommonObj(g.getCommonObj());
        setTotalResource(p.getPlayerGround().getTotalResources());
    }

    /**
     * This method sets the Images to the hand of a player.
     * If the card is null, the empty image is set. Otherwise, the correct image of the hand passed as a parameter is set.
     *
     * @param hand is the hand of the player.
     */
    public void addImages(Hand hand) {
        if (hand.getCard(0) == null) {
            handCardLeft.setImage(new Image("file:" + emptyImagePath));
            currentHandLeft = null;
        } else {
            handCardLeft.setImage(new Image("file:" + imagesFrontPath + hand.getCard(0).getId() + ".png"));
            currentHandLeft = hand.getCard(0);
        }
        if (hand.getCard(1) == null) {
            handCardCenter.setImage(new Image("file:" + emptyImagePath));
            currentHandCenter = null;
        } else {
            handCardCenter.setImage(new Image("file:" + imagesFrontPath + hand.getCard(1).getId() + ".png"));
            currentHandCenter = hand.getCard(1);
        }
        if (hand.getCard(2) == null) {
            handCardRight.setImage(new Image("file:" + emptyImagePath));
            currentHandRight = null;
        } else {
            handCardRight.setImage(new Image("file:" + imagesFrontPath + hand.getCard(2).getId() + ".png"));
            currentHandRight = hand.getCard(2);
        }
    }

    /**
     * This method displays the name of the other players with their correct color.
     *
     * @param g is the instance of the Game.
     * @param p is the instance of the Player.
     */
    public void addNames(Game g, Player p) {
        labelNick.setText("Player: " + p.getNickname());
        colorToPawn(p.getColor(), yourPawn);
        for (Player player : g.getPlayers()) {
            if (!player.getNickname().equals(p.getNickname())) {
                if (!labelPoints2.isVisible()) {
                    labelPoints2.setText(player.getNickname()+ ": 0");
                    nick2 = player.getNickname();
                    labelPoints2.setVisible(true);
                } else if (!labelPoints3.isVisible()) {
                    labelPoints3.setText(player.getNickname()+ ": 0");
                    nick3 = player.getNickname();
                    labelPoints3.setVisible(true);
                } else if (!labelPoints4.isVisible()) {
                    labelPoints4.setText(player.getNickname()+ ": 0");
                    nick4 = player.getNickname();
                    labelPoints4.setVisible(true);
                }
            }
        }
    }

    /**
     * This method shows the other players' player ground.
     * Their player ground is built from each previous move done by each player.
     *
     * @param pg is the player ground to display.
     */
    public void addOtherPlayerground(Player pg){
        name.setText("Nickname : \n"+ pg.getNickname());
        score.setText("Points : " + pg.getPlayerGround().getPlayerScore());
        ImageView iv;
        for(Move move : pg.getPlayerGround().getMoves()){
            if (pg.getPlayerGround().getGround()[move.getPos().getX()][move.getPos().getY()].getFlip()) {
                iv = new ImageView(new Image("file:" + imagesBackPath + pg.getPlayerGround().getGround()[move.getPos().getX()][move.getPos().getY()].getId() + ".png", 150, 100, false, false));
            } else {
                iv = new ImageView(new Image("file:" + imagesFrontPath + pg.getPlayerGround().getGround()[move.getPos().getX()][move.getPos().getY()].getId() + ".png", 150, 100, false, false));
            }
            gridPaneGroundOther.add(iv,move.getPos().getY(),move.getPos().getX());
        }
    }

    /**
     * This method sets visible the creation of the room.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public void addRoomCreationInput() throws IOException, ClassNotFoundException {
        menu.setDisable(true);
        menu.setVisible(false);
        confirmRoom.setVisible(!textFieldRoom.getText().isEmpty());
        labelRoom.setVisible(true);
        textFieldRoom.setVisible(true);
        labelRoom.setDisable(false);
        textFieldRoom.setDisable(false);
        textFieldRoom.setOnKeyTyped(e -> {
            confirmRoom.setVisible(true);
            confirmRoom.setDisable(false);
        });
    }

    /**
     * This method sets visible the choice of available rooms.
     *
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public void addRoomsMenu() throws IOException, ClassNotFoundException {
        confirmRoom.setVisible(false);
        labelRoom.setVisible(false);
        textFieldRoom.setVisible(false);
        textFieldRoom.setDisable(true);
        menu.setPromptText("Available rooms:");
        menu.setStyle("-fx-text-background-color: black;");
        menu.setVisible(true);
        menu.setDisable(false);
        menu.setOnAction(e -> {
            confirmRoom.setVisible(true);
            confirmRoom.setDisable(false);
        });

    }

    /**
     * This method sets the Image of the secret objective card chosen by the player.
     *
     * @param secretObjective is the secret objective card chosen by the player.
     */
    public void addSecretObj(ObjectiveCard secretObjective) {
        secretObj.setImage(new Image("file:" + imagesFrontPath + secretObjective.getId() + ".png"));
    }

    /**
     * This method sets the Images of the two secret objective to display in the correct ImageViews.
     *
     * @param objs is the starter card to displayed.
     */
    public void addSecretObjImages(ObjectiveCard[] objs) {
        secretObjLeft.setImage(new Image("file:" + imagesFrontPath + objs[0].getId() + ".png"));
        secretObjRight.setImage(new Image("file:" + imagesFrontPath + objs[1].getId() + ".png"));
    }

    /**
     * This method sets the Image of the starter card to display in the correct ImageViews (front and back).
     *
     * @param card is the starter card to displayed.
     */
    public void addStarterImages(StarterCard card) {
        frontStarterCard.setImage(new Image("file:" + imagesFrontPath + card.getId() + ".png"));
        backStarterCard.setImage(new Image("file:" + imagesBackPath + card.getId() + ".png"));
    }

    /**
     * This method adds the images of the colors, depending on the color of the player.
     *
     * @param color is the color to set.
     * @param iv is the ImageView in which to set the Image.
     */
    public void colorToPawn(String color, ImageView iv) {
        if (color.equals("red")) {
            iv.setImage(new Image("CODEX_pion_rouge.png", 20, 20, false, false));
        }
        if (color.equals("blue")) {
            iv.setImage(new Image("CODEX_pion_bleu.png", 20, 20, false, false));
        }
        if (color.equals("green")) {
            iv.setImage(new Image("CODEX_pion_vert.png", 20, 20, false, false));
        }
        if (color.equals("yellow")) {
            iv.setImage(new Image("CODEX_pion_jaune.png", 20, 20, false, false));
        }
    }

    /**
     * This method sets the correct Image to the card in hand of a player, depending on whether the card is flipped or not.
     *
     * @param hand is the hand of the player.
     */
    public void flipCard(Hand hand){
        String face;
        if(hand.getCard(0).getFlip()){
            face = imagesFrontPath;
        }else{
            face = imagesBackPath;
        }
        if (hand.getCard(0) == null) {
            handCardLeft.setImage(new Image("file:" + emptyImagePath));
            currentHandLeft = null;
        } else {
            handCardLeft.setImage(new Image("file:" + face + hand.getCard(0).getId() + ".png"));
            currentHandLeft = hand.getCard(0);
            currentHandLeft.flipCard();
        }
        if (hand.getCard(1) == null) {
            handCardCenter.setImage(new Image("file:" + emptyImagePath));
            currentHandCenter = null;
        } else {
            handCardCenter.setImage(new Image("file:" + face + hand.getCard(1).getId() + ".png"));
            currentHandCenter = hand.getCard(1);
            currentHandCenter.flipCard();
        }
        if (hand.getCard(2) == null) {
            handCardRight.setImage(new Image("file:" + emptyImagePath));
            currentHandRight = null;
        } else {
            handCardRight.setImage(new Image("file:" + face + hand.getCard(2).getId() + ".png"));
            currentHandRight = hand.getCard(2);
            currentHandRight.flipCard();
        }
    }

    public AnchorPane getAnchor2() {
        return anchor2;
    }

    public AnchorPane getAnchor3() {
        return anchor3;
    }

    public AnchorPane getAnchor4() {
        return anchor4;
    }

    public AnchorPane getAnchor5() {
        return anchor5;
    }

    public AnchorPane getAnchor6() {
        return anchor6;
    }

    public AnchorPane getAnchor7() {
        return anchor7;
    }

    public AnchorPane getAnchor8() {
        return anchor8;
    }

    public AnchorPane getAnchor9() {
        return anchor9;
    }

    public AnchorPane getAnchor10() {
        return anchor10;
    }

    public ImageView getBackgroundIV() {
        return backgroundIV;
    }

    public ImageView getBackStarterCard() {
        return backStarterCard;
    }

    public ToggleButton getBlue() {
        return blue;
    }

    public RadioButton getButtonL() {
        return buttonL;
    }

    public RadioButton getButtonR() {
        return buttonR;
    }

    public PlayableCard getCardPlayed() {
        return cardPlayed;
    }

    public Label getColorValidLabel() {
        return colorValidLabel;
    }

    public Button getConfirmColor() {
        return confirmColor;
    }

    public Button getConfirmRoom() {
        return confirmRoom;
    }

    public PlayableCard getCurrentHandCenter() {
        return currentHandCenter;
    }

    public PlayableCard getCurrentHandLeft() {
        return currentHandLeft;
    }

    public PlayableCard getCurrentHandRight() {
        return currentHandRight;
    }
    public Button getErrorButton() {return errorButton;}

    public Button getFinishButton2() {
        return finishButton2;
    }

    public Button getFlipButton() {
        return flipButton;
    }

    public ImageView getFrontStarterCard() {
        return frontStarterCard;
    }

    public ImageView getGoldDeck() {
        return goldDeck;
    }

    public ImageView getGoldUp1() {
        return goldUp1;
    }

    public ImageView getGoldUp2() {
        return goldUp2;
    }

    public ToggleButton getGreen() {
        return green;
    }

    public ImageView getHandCardCenter() {
        return handCardCenter;
    }

    public ImageView getHandCardLeft() {
        return handCardLeft;
    }

    public ImageView getHandCardRight() {
        return handCardRight;
    }

    public HBox getHboxStart() {
        return hBoxStart;
    }

    public ArrayList<ImageView> getImageViewList(){return imageViewArrayList;}

    public Button getIpButton() {
        return IpButton;
    }

    public TextField getIpField() {
        return IpField;
    }

    public Label getLabelPoints2() {
        return labelPoints2;
    }

    public Label getLabelPoints3() {
        return labelPoints3;
    }

    public Label getLabelPoints4() {
        return labelPoints4;
    }

    public Label getLabelRoom() {
        return labelRoom;
    }

    public ComboBox<Label> getMenu() {
        return menu;
    }

    public String getNick2() {
        return nick2;
    }

    public String getNick3() {
        return nick3;
    }

    public String getNick4() {
        return nick4;
    }

    public AnchorPane getNickAnchor() {
        return nickAnchorPane;
    }

    public Button getNickButton() {
        return nickButton;
    }

    public Label getNickLabel() {
        return nickLabel;
    }

    /**
     * This method gets the nickname typed by the user.
     *
     * @return such nickname.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public String getNickname() throws IOException, ClassNotFoundException {
        String nickname;
        nickname = nickTextField.getText();
        if (!nickname.isEmpty()) {
            nickTextField.clear();
        }
        return nickname;
    }

    public TextField getNickTextField() {
        return nickTextField;
    }

    public Button getNotYourTurnButton() {
        return notYourTurnButton;
    }

    /**
     * This method gets the number of players typed by the user.
     *
     * @return such number.
     */
    public int getNumberPlayers() {
        int numberOfPlayers = 0;
        String inputText = numberPlayersTF.getText();
        if (!inputText.isEmpty()) {
            try {
                numberOfPlayers = Integer.parseInt(inputText);
                if (numberOfPlayers < 2 || numberOfPlayers > 4) {
                    validLabel.setVisible(true);
                    numberPlayersTF.clear();
                }
            } catch (NumberFormatException e) {
                validLabel.setVisible(true);
                numberPlayersTF.clear();
            }
        }
        return numberOfPlayers;
    }

    public Position getPosPlayed() {
        return posPlayed;
    }

    public ToggleButton getRed() {
        return red;
    }

    public ImageView getResDeck() {
        return resDeck;
    }

    public ImageView getResUp1() {
        return resUp1;
    }

    public ImageView getResUp2() {
        return resUp2;
    }

    public Button getRequestButton() {
        return requestButton;
    }

    public ImageView getSadFace() {
        return sadFace;
    }

    public ImageView getSadFace2() {
        return sadFace2;
    }

    public ImageView getSecretObjLeft() {
        return secretObjLeft;
    }

    public ImageView getSecretObjRight() {
        return secretObjRight;
    }

    public Label getStartLabel() {
        return startLabel;
    }

    public TextField getTfRoom() {
        return textFieldRoom;
    }

    public Label getWinnerName() {
        return winnerName;
    }

    public Label getWinnersNames() {
        return winnersNames;
    }

    public ToggleButton getYellow() {
        return yellow;
    }

    public Button getYourTurnButton() {
        return yourTurnButton;
    }

    /**
     * This method sets the Image of the starter card chosen by the player.
     *
     * @param updatedP is the updated instance of the player.
     */
    public void placeFirstCard(Player updatedP) {
        Card sc = updatedP.getPlayerGround().getGround()[42][42];
        ImageView iv;
        if (sc.getFlip()) {
            iv = new ImageView(new Image("file:" + imagesBackPath + sc.getId() + ".png", 150, 100, true, true));
        } else {
            iv = new ImageView(new Image("file:" + imagesFrontPath + sc.getId() + ".png", 150, 100, true, true));
        }
        gridPaneGround.add(iv, 42, 42);
    }

    /**
     * This method switch the x and the y in a position due to the convention that: x = number of rows; y = number of columns.
     *
     * @param pos is the position whose x and y needs to be reconverted.
     * @return such updated Position.
     */
    public Position reconvertPosition(Position pos) {
        Position toReturn = new Position(0, 0);
        toReturn.setX(pos.getY());
        toReturn.setY(pos.getX());
        return toReturn;
    }

    /**
     * This method removes a cell corresponding to a non-available position from the grid pane.
     */
    public void removeAvailablePos(){
        ArrayList<Node> toRemove = new ArrayList<>();
        ImageView iv;
        for(Node n : gridPaneGround.getChildren()){
            if(n!=null) {
                iv = (ImageView) n;
                if (iv.getImage().getUrl()!=null && iv.getImage().getUrl().equals("file:" + emptyImagePath)) {
                    toRemove.add(n);
                }
            }
        }
        for(Node n : toRemove){
            if(n!=null) {
                gridPaneGround.getChildren().remove(n);
            }
        }
    }

    /**
     * This method removes the played position from the list of the available positions.
     */
    public void removePlayedPos() {
        availablePos.remove(posPlayed);
    }

    /**
     * This method sets the card played.
     *
     * @param card the card to be set.
     */
    public void setCardPlayed(PlayableCard card) {
        cardPlayed = card;
    }

    /**
     * This method sets the correct Image to the drag zone selected.
     * It also sets all the effects applied to the dragged card when entering and exiting the zone.
     *
     * @param zone is the zone selected
     */
    public void setDropZones(ImageView zone) {
        zone.setOnDragOver(e -> {
            if(zone.getImage().getUrl()!=null && zone.getImage().getUrl().equals("file:" + emptyImagePath)) {
                if (e.getDragboard().hasImage()) {
                    e.acceptTransferModes(TransferMode.MOVE);
                    e.consume();
                }
            }
        });
        zone.setOnDragEntered(e -> {
            if(zone.getImage().getUrl()!=null && zone.getImage().getUrl().equals("file:" + emptyImagePath)) {
                ColorAdjust ca = new ColorAdjust();
                ca.setBrightness(0.5);
                zone.setEffect(ca);
            }
        });
        zone.setOnDragExited(e -> {
            ColorAdjust ca = new ColorAdjust();
            ca.setBrightness(0);
            zone.setEffect(ca);
        });
        zone.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;
            if (db.hasImage()) {
                zone.setImage(db.getImage());
                zone.toFront();
                success = true;
            }
            posPlayed = new Position(GridPane.getColumnIndex(zone), GridPane.getRowIndex(zone));
            e.setDropCompleted(success);
            e.consume();
        });
    }

    /**
     * This method sets the played position to null.
     */
    public void setPosPlayedNull(){
        posPlayed = null;
    }

    /**
     * This method sets the correct number of every resource of a player.
     *
     * @param map is the HashMap that contains the correct values of each resource.
     */
    public void setTotalResource(HashMap<Resource, Integer> map) {
        mushroomNum.setText(String.valueOf(map.get(Resource.MUSHROOM)));
        bugNum.setText(String.valueOf(map.get(Resource.BUG)));
        foxNum.setText(String.valueOf(map.get(Resource.FOX)));
        leafNum.setText(String.valueOf(map.get(Resource.LEAF)));
        potionNum.setText(String.valueOf(map.get(Resource.POTION)));
        scrollNum.setText(String.valueOf(map.get(Resource.SCROLL)));
        plumeNum.setText(String.valueOf(map.get(Resource.PLUME)));
    }

    /**
     * This method sets the empty image in every ImageView corresponding to an available position.
     * This is done to highlight the available position in order to help the player understand where they can place a card.
     *
     * @param pos is the set of available positions.
     */
    public void showAvailablePos(Set<Position> pos) {
        imageViewArrayList.clear();
        int x, y;
        for (Position p : pos) {
            y = p.getX();
            x = p.getY();
            ImageView image = new ImageView(new Image("file:" + emptyImagePath, 150, 100, false, false));
            imageViewArrayList.add(image);
            gridPaneGround.add(image, x, y);
            availablePos.add(new Position(x, y));
        }
    }

    /**
     * This method updates the hand, total resources and the score of the player after their turn.
     *
     * @param p is the instance of the player.
     */
    public void updateAfterPlay(Player p) {
        addImages(p.getHand());
        setTotalResource(p.getPlayerGround().getTotalResources());
        labelPoints.setText("Points: " + p.getPlayerGround().getPlayerScore());
    }

}


