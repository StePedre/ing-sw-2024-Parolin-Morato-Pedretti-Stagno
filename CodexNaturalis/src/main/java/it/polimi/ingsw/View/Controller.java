package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
    private GridPane gridPaneGround;
    @FXML
    private AnchorPane anchor2, anchor3, anchor4, anchor5, anchor6, anchor7, anchor8, anchor9, nickAnchorPane, anchor10;
    @FXML
    private HBox hBoxStart;
    @FXML
    private RadioButton buttonR, buttonL;
    @FXML
    private ImageView frontStarterCard, backStarterCard, secretObjLeft, secretObjRight, backgroundIV, sadFace, sadFace2;
    @FXML
    private Button finishButton, finishButton2, buttonStart, confirmRoom, confirmColor, IpButton;
    @FXML
    private TextField nickTextField, numberPlayersTF, textFieldRoom, IpField;
    @FXML
    private Label nickLabel, labelNick, label20p, labelRoom, validLabel, winnerName, winnersNames, startLabel, startLabel2, colorValidLabel;
    @FXML
    private ProgressBar waitingBar;
    @FXML
    private ComboBox<Label> menu;
    @FXML
    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();
    @FXML
    private ImageView resUp1, resUp2, resDeck, goldUp1, goldUp2, goldDeck;
    @FXML
    private Label labelPoints, labelPoints2, labelPoints3, labelPoints4, mushroomNum, bugNum, leafNum, foxNum, potionNum, scrollNum, plumeNum;
    @FXML
    private ImageView handCardLeft, handCardCenter, handCardRight, secretObj, logo;
    @FXML
    private ImageView commonObj1, commonObj2;
    private int converter = 38; // 42 - 4 (coord iniziali matrice e gridPane)
    @FXML
    private Button nickButton, requestButton, yourTurnButton;
    private ArrayList<Position> availablePos = new ArrayList<>();
    private final String emptyImagePath = "src/main/resources/border_image.png";
    private final String imagesFrontPath = "src/main/resources/CODEX_cards_gold_front/";
    private final String imagesBackPath = "src/main/resources/CODEX_cards_gold_back/";
    private PlayableCard currentHandLeft, currentHandCenter, currentHandRight;
    private String nick2, nick3, nick4;
    private boolean isDragged = false;
    private PlayableCard cardPlayed;
    private Position posPlayed;

    public PlayableCard getCurrentHandLeft() {
        return currentHandLeft;
    }

    public PlayableCard getCurrentHandCenter() {
        return currentHandCenter;
    }

    public PlayableCard getCurrentHandRight() {
        return currentHandRight;
    }

    public Position getPosPlayed() {
        return posPlayed;
    }

    public ImageView getResUp1() {
        return resUp1;
    }

    public ImageView getResUp2() {
        return resUp2;
    }

    public ImageView getResDeck() {
        return resDeck;
    }

    public ImageView getGoldUp1() {
        return goldUp1;
    }

    public ImageView getGoldUp2() {
        return goldUp2;
    }

    public ImageView getGoldDeck() {
        return goldDeck;
    }

    public PlayableCard getCardPlayed() {
        return cardPlayed;
    }

    public void setCardPlayed(PlayableCard card) {
        cardPlayed = card;
    }

    public ArrayList<Position> getAvailablePos() {
        return availablePos;
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

    public boolean getIsDragged() {
        return isDragged;
    }

    public AnchorPane getAnchor2() {
        return anchor2;
    }

    public Button getNickButton() {
        return nickButton;
    }

    public Button getYourTurnButton() {
        return yourTurnButton;
    }

    public Button getRequestButton() {
        return requestButton;
    }

    public ImageView getFrontStarterCard() {
        return frontStarterCard;
    }

    public ImageView getBackStarterCard() {
        return backStarterCard;
    }

    public ImageView getSecretObjLeft() {
        return secretObjLeft;
    }

    public ImageView getSecretObjRight() {
        return secretObjRight;
    }

    public RadioButton getButtonL() {
        return buttonL;
    }

    public RadioButton getButtonR() {
        return buttonR;
    }

    public Button getButtonStart() {
        return buttonStart;
    }

    public Button getIpButton() {
        return IpButton;
    }

    public HBox getHboxStart() {
        return hBoxStart;
    }

    public TextField getIpField() {
        return IpField;
    }

    public Label getWinnerName() {
        return winnerName;
    }

    public Label getWinnersNames() {
        return winnersNames;
    }

    public Button getFinishButton() {
        return finishButton;
    }

    public Button getFinishButton2() {
        return finishButton2;
    }

    public Button getConfirmRoom() {
        return confirmRoom;
    }

    public Button getConfirmColor() {
        return confirmColor;
    }

    public TextField getTfRoom() {
        return textFieldRoom;
    }

    public Label getLabelRoom() {
        return labelRoom;
    }

    public ComboBox<Label> getMenu() {
        return menu;
    }

    public Label getStartLabel() {
        return startLabel;
    }

    public Label getStartLabel2() {
        return startLabel2;
    }

    public Label getNickLabel() {
        return nickLabel;
    }

    public Label getLabel20p() {
        return label20p;
    }

    public Label getColorValidLabel() {
        return colorValidLabel;
    }

    public TextField getNickTextField() {
        return nickTextField;
    }

    public ProgressBar getWaitingBar() {
        return waitingBar;
    }

    public ImageView getLogo() {
        return logo;
    }

    public ImageView getBackgroundIV() {
        return backgroundIV;
    }

    public ImageView getSadFace() {
        return sadFace;
    }

    public ImageView getSadFace2() {
        return sadFace2;
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

    public AnchorPane getAnchor3() {
        return anchor3;
    }

    public AnchorPane getAnchor10() {
        return anchor10;
    }

    public AnchorPane getNickAnchor() {
        return nickAnchorPane;
    }

    public AnchorPane getPlayGroundAnchor() {
        return playGroundAnchor;
    }

    public HBox getPlayGroundHBox() {
        return playGroundHBox;
    }

    public ToggleButton getRed() {
        return red;
    }

    public ToggleButton getBlue() {
        return blue;
    }

    public ToggleButton getGreen() {
        return green;
    }

    public ToggleButton getYellow() {
        return yellow;
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

    public Position reconvertPosition(Position pos) {
        Position toReturn = new Position(0, 0);
        toReturn.setX(pos.getX() + converter);
        toReturn.setY(pos.getY() + converter);
        return toReturn;
    }

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

    public void addRoomCreationInput(ArrayList<Room> rooms) throws IOException, ClassNotFoundException {
        menu.setDisable(true);
        menu.setVisible(false);
        confirmRoom.setVisible(false);
        labelRoom.setVisible(true);
        textFieldRoom.setVisible(true);
        labelRoom.setDisable(false);
        textFieldRoom.setDisable(false);
        textFieldRoom.setOnKeyTyped(e -> {
            confirmRoom.setVisible(true);
            confirmRoom.setDisable(false);
        });
    }

    public String getNickname() throws IOException, ClassNotFoundException {
        String nickname;
        nickname = nickTextField.getText();
        if (!nickname.isEmpty()) {
            nickTextField.clear();
        }
        return nickname;
    }

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

    public void getLeftSecretObj() {
        ObjectiveCard secretObj;
        System.out.println("Chosen left secret objective");
    }

    public void getRightSecretObj() {
        ObjectiveCard secretObj;
        System.out.println("Chosen right secret objective");
    }

    public void addStarterImages(StarterCard card) {
        frontStarterCard.setImage(new Image("file:" + imagesFrontPath + card.getId() + ".png"));
        backStarterCard.setImage(new Image("file:" + imagesBackPath + card.getId() + ".png"));
    }

    public void addSecretObjImages(ObjectiveCard[] objs) {
        secretObjLeft.setImage(new Image("file:" + imagesFrontPath + objs[0].getId() + ".png"));
        secretObjRight.setImage(new Image("file:" + imagesFrontPath + objs[1].getId() + ".png"));
    }

    public void addNames(Game g, Player p) {
        labelNick.setText("Player: " + p.getNickname());
        String color = colorToString(p.getColor());
        labelNick.setStyle("-fx-text-fill: " + color);
        for (Player player : g.getPlayers()) {
            if (!player.getNickname().equals(p.getNickname())) {
                if (!labelPoints2.isVisible()) {
                    labelPoints2.setText(player.getNickname());
                    nick2 = player.getNickname();
                    labelPoints2.setVisible(true);
                } else if (!labelPoints3.isVisible()) {
                    labelPoints3.setText(player.getNickname());
                    nick3 = player.getNickname();
                    labelPoints3.setVisible(true);
                } else if (!labelPoints4.isVisible()) {
                    labelPoints4.setText(player.getNickname());
                    nick4 = player.getNickname();
                    labelPoints4.setVisible(true);
                }
            }
        }
    }

    public void addGround(Game g, Player p) {
        labelPoints.setText("Points: " + p.getPlayerGround().getPlayerScore());
        for (Player player : g.getPlayers()) {
            if (!player.getNickname().equals(p.getNickname())) {
                if (player.getNickname().equals(nick2)) {
                    labelPoints2.setText(nick2 + ": " + player.getPlayerGround().getPlayerScore());
                    String color = colorToString(player.getColor());
                    labelPoints2.setStyle("-fx-text-fill: " + color);
                } else if (player.getNickname().equals(nick3)) {
                    labelPoints3.setText(nick3 + ": " + player.getPlayerGround().getPlayerScore());
                    String color = colorToString(player.getColor());
                    labelPoints2.setStyle("-fx-text-fill: " + color);
                } else if (player.getNickname().equals(nick4)) {
                    labelPoints4.setText(nick4 + ": " + player.getPlayerGround().getPlayerScore());
                    String color = colorToString(player.getColor());
                    labelPoints2.setStyle("-fx-text-fill: " + color);
                }
            }
        }
        addImages(p.getHand());
        addSecretObj(p.getHand().getObjCard());
        addCommonObj(g.getCommonObj());
        setTotalResource(p.getPlayerGround().getTotalResources());
    }

    public String colorToString(String color) {
        String c = "";
        if (color.equals("red")) {
            c = "#a90303";
        }
        if (color.equals("blue")) {
            c = "#1472d2";
        }
        if (color.equals("green")) {
            c = "#338d25";
        }
        if (color.equals("yellow")) {
            c = "#9117b0";
        }
        return c;
    }

    public void updateAfterPlay(Player p) {
        addImages(p.getHand());
        setTotalResource(p.getPlayerGround().getTotalResources());
        labelPoints.setText("Points: " + p.getPlayerGround().getPlayerScore());
    }

    public void setLinkToPlayerGrounds(Game g, Player p) {
        // to do
    }

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

    public void addSecretObj(ObjectiveCard secretObjective) {
        secretObj.setImage(new Image("file:" + imagesFrontPath + secretObjective.getId() + ".png"));
    }

    // questo potrebbe andare bene statico perchè tutti condividono gli stessi commonObj
    public void addCommonObj(ObjectiveCard[] commonObj) {
        commonObj1.setImage(new Image("file:" + imagesFrontPath + commonObj[0].getId() + ".png"));
        commonObj2.setImage(new Image("file:" + imagesFrontPath + commonObj[1].getId() + ".png"));
    }

    public void setTotalResource(HashMap<Resource, Integer> map) {
        mushroomNum.setText(String.valueOf(map.get(Resource.MUSHROOM)));
        bugNum.setText(String.valueOf(map.get(Resource.BUG)));
        foxNum.setText(String.valueOf(map.get(Resource.FOX)));
        leafNum.setText(String.valueOf(map.get(Resource.LEAF)));
        potionNum.setText(String.valueOf(map.get(Resource.POTION)));
        scrollNum.setText(String.valueOf(map.get(Resource.SCROLL)));
        plumeNum.setText(String.valueOf(map.get(Resource.PLUME)));
    }

    public void placeFirstCard(Player updatedP) {
        Card sc = updatedP.getPlayerGround().getGround()[42][42];
        ImageView iv;
        if (sc.getFlip()) {
            iv = new ImageView(new Image("file:" + imagesBackPath + sc.getId() + ".png", 150, 100, true, true));
        } else {
            iv = new ImageView(new Image("file:" + imagesFrontPath + sc.getId() + ".png", 150, 100, true, true));
        }
        gridPaneGround.add(iv, 4, 4);
    }

    /*public Position playCard(Player p, Game g) throws IOException, ClassNotFoundException {
        showAvailablePos(p.getPlayerGround().getAvailablePositions());
        setDragDetected(handCardLeft);
        setDragDetected(handCardCenter);
        setDragDetected(handCardRight);
        Position droppedP = new Position(-1, -1);
        for(Position pos: availablePos){
            ImageView zone = (ImageView) getImageViewFromPos(pos.getX(), pos.getY());
            droppedP = setDropZones(zone);
        }
        return droppedP;
       // p = client.receivePlayerFromServer();  // riceve player con mano aggiornata -> vedi placeCardController per capire com'è la nuova mano e aggiorna il playground

     /*   if(client.receiveBooleanFromServer()){
            // to do: cosa succede se ha vinto
            return false;
        }
        else{
            addCards(g.getDecks());
            return true;
        }
    }*/

    public PlayableCard returnCardPlayed() {
        PlayableCard toReturn = null;
        if (setDropCompleted(handCardLeft)) {
            toReturn = currentHandLeft;
        }
        if (setDropCompleted(handCardCenter)) {
            toReturn = currentHandCenter;
        }
        if (setDropCompleted(handCardRight)) {
            toReturn = currentHandRight;
        }
        return toReturn;
    }


    public void addCards(Deck[] decks) {
        resUp1.setImage(new Image("file:" + imagesFrontPath + decks[0].getCards().get(0).getId() + ".png"));
        resUp2.setImage(new Image("file:" + imagesFrontPath + decks[0].getCards().get(1).getId() + ".png"));
        resDeck.setImage(new Image("file:" + imagesBackPath + decks[0].getCards().get(2).getId() + ".png"));
        goldUp1.setImage(new Image("file:" + imagesFrontPath + decks[1].getCards().get(0).getId() + ".png"));
        goldUp2.setImage(new Image("file:" + imagesFrontPath + decks[1].getCards().get(1).getId() + ".png"));
        goldDeck.setImage(new Image("file:" + imagesBackPath + decks[1].getCards().get(2).getId() + ".png"));
    }

    public void putDrawnInHand(PlayableCard card) {
        Image toPut = new Image("file:" + imagesFrontPath + card.getId() + ".png");
        //       if(handCardRight.getImage() == voidImage){
        handCardRight.setImage(toPut);
        //      }
        //      if(handCardCenter.getImage()==voidImage){
        handCardCenter.setImage(toPut);
        //       }
        //      if(handCardLeft.getImage()==voidImage){
        handCardLeft.setImage(toPut);
        //     }
    }

  /*  public boolean waitForTurn(Stage stage) throws IOException, ClassNotFoundException {
        // aspetta turno (scritta che indica turno corrente?)
        // può vedere pg altri giocatori
        return client.receiveBooleanFromServer();
    }*/

    /*public ArrayList<Player> getWinners() throws IOException, ClassNotFoundException {
        return client.receiveWinnersFromServer();
    }*/

    public void setDragDetected(ImageView iv) {  //iv da dove parto (hand) iv2 dove arrivo)
        iv.setOnDragDetected(event -> {
            Dragboard db = iv.startDragAndDrop(TransferMode.MOVE);
            Image dragMiniature = new Image(iv.getImage().getUrl(), 150, 100, true, true);
            ClipboardContent content = new ClipboardContent();
            content.putImage(dragMiniature);
            db.setContent(content);
            event.consume();
        });
    }

    public Node getImageViewFromPos(int x, int y) {
        for (Node node : gridPaneGround.getChildren()) {
            if ((node != null) && (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y)) {
                return node;
            }
        }
        return null;
    }

    public void setDropZones(ImageView zone) {
        isDragged = false;
        zone.setOnDragOver(e -> {
            if (e.getDragboard().hasImage()) {
                e.acceptTransferModes(TransferMode.MOVE);
                e.consume();
            }
        });
        zone.setOnDragEntered(e -> {
            ColorAdjust ca = new ColorAdjust();
            ca.setBrightness(0.5);
            zone.setEffect(ca);
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
                if (GridPane.getColumnIndex(zone) == 0 || GridPane.getRowIndex(zone) == 0) {
                    System.out.println("Top border");
                    addColumnRowTop();
                }
                if (GridPane.getColumnIndex(zone) == (gridPaneGround.getColumnCount() - 1) || GridPane.getRowIndex(zone) == (gridPaneGround.getRowCount() - 1)) {
                    System.out.println("Bottom border");
                    addColumnRowBottom();
                }
                success = true;
            }
            posPlayed = new Position(GridPane.getColumnIndex(zone), GridPane.getRowIndex(zone));
            e.setDropCompleted(success);
            isDragged = true;
            e.consume();
        });
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public void addColumnRowTop() {
        double widthGridPane = gridPaneGround.getWidth();
        double heightGridPane = gridPaneGround.getHeight();
        gridPaneGround.setPrefWidth(widthGridPane + 117.14);
        gridPaneGround.setPrefHeight(heightGridPane + 59.29);

        for (int i = 0; i < gridPaneGround.getColumnCount(); i++) {
            for (int j = 0; j < gridPaneGround.getRowCount(); j++) {
                Node node = getNodeFromGridPane(gridPaneGround, i, j);
                GridPane.setColumnIndex(node, i + 1);
                GridPane.setRowIndex(node, j + 1);
            }
        }
        gridPaneGround.getColumnConstraints().addFirst(new ColumnConstraints(117.14));
        gridPaneGround.getRowConstraints().addFirst(new RowConstraints(59.29));
    }

    public void addColumnRowBottom() {
        double widthGridPane = gridPaneGround.getWidth();
        double heightGridPane = gridPaneGround.getHeight();
        gridPaneGround.setPrefWidth(widthGridPane + 117.14);
        gridPaneGround.setPrefHeight(heightGridPane + 59.29);

        for (int i = 0; i < gridPaneGround.getColumnCount(); i++) {
            for (int j = 0; j < gridPaneGround.getRowCount(); j++) {
                Node node = getNodeFromGridPane(gridPaneGround, i, j);
                GridPane.setColumnIndex(node, i + 1);
                GridPane.setRowIndex(node, j + 1);
            }
        }
        gridPaneGround.getColumnConstraints().addFirst(new ColumnConstraints(117.14));
        gridPaneGround.getRowConstraints().addFirst(new RowConstraints(59.29));
    }

    public Boolean setDropCompleted(ImageView iv) {
        Boolean[] toReturn = new Boolean[1];
        iv.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                iv.setImage(new Image("file:" + imagesBackPath + "95.png"));
            }
            toReturn[0] = true;
            event.consume();
        });
        return toReturn[0];
    }

    public ArrayList<ImageView> showAvailablePos(Set<Position> pos) {
        int x, y;
        ArrayList<ImageView> imageViewArrayList = new ArrayList<ImageView>();
        for (Position p : pos) {
            y = p.getX() - converter;   // maybe
            x = p.getY() - converter;
            ImageView image = new ImageView(new Image("file:" + emptyImagePath, 150, 100, false, false));
            gridPaneGround.add(image, x, y);
            imageViewArrayList.add(image);
            availablePos.add(new Position(x, y));
        }
        return imageViewArrayList;
    }

    public boolean isFound(ArrayList<Position> list, Position pos) {
        for (Position p : list) {
            if (p.getX() == pos.getX() && p.getY() == pos.getY()) {
                return true;
            }
        }
        return false;
    }
}


