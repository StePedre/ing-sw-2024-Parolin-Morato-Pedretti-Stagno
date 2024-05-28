package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
    @FXML
    private AnchorPane playGroundAnchor;
    @FXML
    private HBox playGroundHBox;
    @FXML
    private GridPane gridPaneGround;
    @FXML
    private AnchorPane anchor2, anchor3, anchor4, anchor5, anchor6, anchor7, anchor8, anchor9,nickAnchorPane;
    @FXML
    private HBox hBoxStart;
    @FXML
    private RadioButton buttonR,buttonL;
    @FXML
    private ImageView frontStarterCard, backStarterCard, secretObjLeft, secretObjRight, backgroundIV, sadFace, sadFace2;
    @FXML
    private Button finishButton, finishButton2, buttonStart, confirmRoom;
    @FXML
    private TextField nickTextField, numberPlayersTF, textFieldRoom;
    @FXML
    private Label nickLabel, label20p, labelRoom, validLabel, winnerName, winnersNames, startLabel, startLabel2;
    @FXML
    private ProgressBar waitingBar;
    @FXML
    private ComboBox<Label> menu;
    @FXML
    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();
    @FXML
    private ImageView goldFaceDown, resFaceDown, goldFaceUp1, goldFaceUp2, resFaceUp1, resFaceUp2;
    private GUIClientSocket client;
    @FXML
    private Label labelPoints, mushroomNum, bugNum, leafNum, foxNum, potionNum, scrollNum, plumeNum;
    @FXML
    private ImageView handCardLeft, handCardCenter, handCardRight, secretObj, logo;
    @FXML
    private ImageView commonObj1, commonObj2;
    private int converter = 39; // 42 - 3 (coord iniziali matrice e gridPane)
    @FXML
    private Button nickButton, requestButton, yourTurnButton;
    private ArrayList<Position> availablePos = new ArrayList<>();
    private final String emptyImagePath = "C:\\Users\\Ste\\Desktop\\Stefano\\UNI\\ANNO III\\INGEGNERIA DEL SOFTWARE\\PROGETTO_IDS\\ing-sw-2024-Parolin-Morato-Pedretti-Stagno\\CodexNaturalis\\src\\main\\resources\\border_image.png";
    private final String imagesFrontPath = "C:\\Users\\Ste\\Desktop\\Stefano\\UNI\\ANNO III\\INGEGNERIA DEL SOFTWARE\\PROGETTO_IDS\\ing-sw-2024-Parolin-Morato-Pedretti-Stagno\\CodexNaturalis\\src\\main\\resources\\CODEX_cards_gold_front\\";
    private final String imagesBackPath = "C:\\Users\\Ste\\Desktop\\Stefano\\UNI\\ANNO III\\INGEGNERIA DEL SOFTWARE\\PROGETTO_IDS\\ing-sw-2024-Parolin-Morato-Pedretti-Stagno\\CodexNaturalis\\src\\main\\resources\\CODEX_cards_gold_back\\";

    public AnchorPane getAnchor2() {return anchor2;}
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
    public RadioButton getButtonL() {return buttonL;}
    public RadioButton getButtonR() {return buttonR;}
    public Button getButtonStart() {return buttonStart;}
    public HBox getHboxStart() {return hBoxStart;}
    public Label getWinnerName() {return winnerName;}
    public Label getWinnersNames() {return winnersNames;}
    public Button getFinishButton() {return finishButton;}
    public Button getFinishButton2() {return finishButton2;}
    public Button getConfirmRoom() {return confirmRoom;}
    public TextField getTfRoom(){return textFieldRoom;}
    public Label getLabelRoom(){return labelRoom;}
    public ComboBox<Label> getMenu() {return menu;}
    public Label getStartLabel(){return startLabel;}
    public Label getStartLabel2(){return startLabel2;}
    public Label getNickLabel(){return nickLabel;}
    public Label getLabel20p(){return label20p;}
    public TextField getNickTextField() {return nickTextField;}
    public ProgressBar getWaitingBar() {return waitingBar;}
    public ImageView getLogo() {
        return logo;
    }
    public ImageView getBackgroundIV(){ return backgroundIV;}
    public ImageView getSadFace(){ return sadFace;}
    public ImageView getSadFace2(){ return sadFace2;}
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
    public AnchorPane getNickAnchor(){
        return nickAnchorPane;
    }
    public AnchorPane getPlayGroundAnchor(){
        return playGroundAnchor;
    }
    public HBox getPlayGroundHBox(){return playGroundHBox;}
    public void addRoomsMenu() throws IOException, ClassNotFoundException {
        confirmRoom.setVisible(false);
        labelRoom.setVisible(false);
        textFieldRoom.setVisible(false);
        textFieldRoom.setDisable(true);
        menu.setPromptText("Available rooms:");
        menu.setStyle("-fx-text-background-color: black;");
        menu.setVisible(true);
        menu.setDisable(false);
        menu.setOnAction(e->{
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

    public boolean getNumberPlayers() {
        int numberOfPlayers = 0;
        String inputText = numberPlayersTF.getText();
        if (!inputText.isEmpty()) {
            try {
                numberOfPlayers = Integer.parseInt(inputText);
                if (numberOfPlayers < 2 || numberOfPlayers > 4) {
                    validLabel.setVisible(true);
                    numberPlayersTF.clear();
                } else {
                    client.sendToServer(numberOfPlayers);
                    return true;
                }
            } catch (NumberFormatException e) {
                validLabel.setVisible(true);
                numberPlayersTF.clear();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public void getLeftSecretObj() {
        ObjectiveCard secretObj;
        System.out.println("Chosen left secret objective");
    }

    public void getRightSecretObj() {
        ObjectiveCard secretObj;
        System.out.println("Chosen right secret objective");
    }
    public void addSecretObjImages() throws IOException, ClassNotFoundException {
        ObjectiveCard[] objs = client.receiveSecretObjsFromServer();
        secretObjLeft.setImage(new Image("file:" + imagesFrontPath + objs[0].getId() + ".png"));
        secretObjRight.setImage(new Image("file:" + imagesFrontPath + objs[1].getId() + ".png"));
    }
    public StarterCard addStarterImages() throws IOException, ClassNotFoundException {
        StarterCard card = client.receiveStarterCardFromServer();
        frontStarterCard.setImage(new Image("file:" + imagesFrontPath + card.getId() + ".png"));
        backStarterCard.setImage(new Image("file:" + imagesBackPath + card.getId() + ".png"));
        return card;
    }
    public void sendIfStarterFlipped(boolean flip) throws IOException {
        client.sendToServer(flip);
    }

    public boolean addGround(Player p, Game g) throws IOException, ClassNotFoundException {
        // client.reset();
        // Game g = client.receiveGameFromServer();
        // Player p = client.receivePlayerFromServer();
        labelPoints.setText("Points: "+ p.getPlayerGround().getPlayerScore());
        addImages(p.getHand());
        addSecretObj(p.getHand().getObjCard());
        addCommonObj(g.getCommonObj());
        setTotalResource(p.getPlayerGround().getTotalResources());
        // show actual ground
        return client.receiveBooleanFromServer();
    }

    public void addImages(Hand hand) {
        handCardLeft.setImage(new Image("file:" + imagesFrontPath + hand.getCard(0).getId() + ".png"));
        handCardCenter.setImage(new Image("file:" + imagesFrontPath + hand.getCard(1).getId() + ".png"));
        handCardRight.setImage(new Image("file:" + imagesFrontPath + hand.getCard(2).getId() + ".png"));
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

    public boolean checkIfOver() throws IOException, ClassNotFoundException {
        return client.receiveBooleanFromServer();
    }

    public void placeFirstCard(StarterCard sc, Player updatedP) throws IOException, ClassNotFoundException {
        ImageView iv = new ImageView(new Image("file:" + imagesFrontPath + sc.getId() + ".png"));
        gridPaneGround.add(iv, 3, 4);
        showAvailablePos(updatedP.getPlayerGround());
    }

    public boolean playCard() throws IOException, ClassNotFoundException {
        client.reset();
        Player player = client.receivePlayerFromServer();
        Game game = client.receiveGameFromServer();
        showAvailablePos(player.getPlayerGround());
        setDragDetected(handCardLeft);
        setDragDetected(handCardCenter);
        setDragDetected(handCardRight);
        setDropZones();
        setDropCompleted(handCardLeft);
        setDropCompleted(handCardCenter);
        setDropCompleted(handCardRight);
        // client.sendToServer(cardToPlay);
        // client.sendToServer(cardPosition);
        client.receivePlayerFromServer();  // riceve player con mano aggiornata -> vedi placeCardController per capire com'è la nuova mano e aggiorna il playground
        client.reset();
        if(client.receiveBooleanFromServer()){
            // to do: cosa succede se ha vinto
            return false;
        }
        else{
            addCards(game.getDecks());
            return true;
        }
    }



    public void addCards(Deck[] decks) {
      //  buttonSubDraw.setVisible(false);
        resFaceUp1.setImage(new Image("file:" + imagesFrontPath + decks[0].getCards().get(0).getId()));
        resFaceUp1.setImage(new Image("file:" + imagesFrontPath + decks[0].getCards().get(1).getId()));
        resFaceDown.setImage(new Image("file:" + imagesBackPath + decks[0].getCards().get(2).getId()));
        goldFaceUp1.setImage(new Image("file:" + imagesFrontPath + decks[1].getCards().get(0).getId()));
        goldFaceUp2.setImage(new Image("file:" + imagesFrontPath + decks[1].getCards().get(1).getId()));
        goldFaceDown.setImage(new Image("file:" + imagesBackPath + decks[1].getCards().get(2).getId()));
    }


    public boolean yourTurnDraw() throws IOException {
        int[] choice = new int[] { -1 };
    /*    DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5);
        dropShadow.setColor(javafx.scene.paint.Color.BLUE);

        resFaceDown.setOnMouseClicked(event -> {
            choice[0] = 0;
            resFaceDown.setEffect(dropShadow);
        });
        resFaceUp1.setOnMouseClicked(event -> {
            choice[0] = 1;
            resFaceUp1.setEffect(dropShadow);
            buttonSubDraw.setVisible(true);
        });
        resFaceUp2.setOnMouseClicked(event ->{
            choice[0] = 2;
            resFaceUp2.setEffect(dropShadow);
            buttonSubDraw.setVisible(true);
        });
        goldFaceDown.setOnMouseClicked(event ->{
            choice[0] = 3;
            goldFaceDown.setEffect(dropShadow);
            buttonSubDraw.setVisible(true);
        });
        goldFaceUp1.setOnMouseClicked(event -> {
            choice[0] = 4;
            goldFaceUp1.setEffect(dropShadow);
            buttonSubDraw.setVisible(true);
        });
        goldFaceUp2.setOnMouseClicked(event-> {
            choice[0] = 5;
            goldFaceUp2.setEffect(dropShadow);
            buttonSubDraw.setVisible(true);
        });

        buttonSubDraw.setOnAction(event -> {
            Stage stage = (Stage) (buttonSubDraw.getScene().getWindow());
            stage.close();
        });
        client.sendToServer(choice[0]);*/
        return true;
    }

    public void putDrawnInHand(PlayableCard card){
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

    public ArrayList<Player> getWinners() throws IOException, ClassNotFoundException {
        return client.receiveWinnersFromServer();
    }

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

    public void setDropZones() {
        for (Position pos : availablePos) {
            ImageView zone = new ImageView(new Image("file:" + emptyImagePath + "border_image.png", 150, 100, false, false));
            gridPaneGround.add(zone, pos.getX() - converter, pos.getY() - converter);
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
                    if(GridPane.getColumnIndex(zone) == 0 || GridPane.getRowIndex(zone) == 0) {
                        System.out.println("Top border");
                        addColumnRowTop();
                    }
                    if(GridPane.getColumnIndex(zone) == (gridPaneGround.getColumnCount() - 1) || GridPane.getRowIndex(zone) ==(gridPaneGround.getRowCount() - 1)){
                        System.out.println("Bottom border");
                        addColumnRowBottom();
                    }
                    success = true;
                }
                e.setDropCompleted(success);
                e.consume();
            });
        }
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

        for (int i = 0; i < gridPaneGround.getColumnCount(); i ++) {
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

        for (int i = 0; i < gridPaneGround.getColumnCount(); i ++) {
            for (int j = 0; j < gridPaneGround.getRowCount(); j++) {
                Node node = getNodeFromGridPane(gridPaneGround, i, j);
                GridPane.setColumnIndex(node, i + 1);
                GridPane.setRowIndex(node, j + 1);
            }
        }
        gridPaneGround.getColumnConstraints().addFirst(new ColumnConstraints(117.14));
        gridPaneGround.getRowConstraints().addFirst(new RowConstraints(59.29));
    }

    public void setDropCompleted(ImageView iv){
        iv.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                iv.setImage(new Image("file:" + imagesBackPath + "95.png"));
            }
            event.consume();
        });
    }

    public void showAvailablePos(PlayerGround pg){
        int x, y;
        Card[][] matrix = pg.getGround();
        for(int i = 0; i < 84; i++) {
            for (int j = 0; j < 84; j++) {
                x = i-converter;
                y = j-converter;
                Position pos = new Position(x, y);
                Card c = matrix[i][j];
                if (c != null && (c.getId() == -1 && !isFound(availablePos, pos))) {
                    //gridPaneGround.add(new ImageView(voidImage), x, y);
                    availablePos.add(new Position(x, y));
                }
            }
        }
    }

    public boolean isFound(ArrayList<Position> list, Position pos){
        for(Position p: list){
            if(p.getX() == pos.getX() && p.getY() == pos.getY()){
                return true;
            }
        }
        return false;
    }
    public void setStreams(GUIClientSocket client){
        this.client = client;
    }
}


