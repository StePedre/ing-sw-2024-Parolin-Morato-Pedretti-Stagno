package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.effect.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Controller {
    @FXML
    private VBox vboxWinners;
    @FXML
    private VBox vboxWinner;
    @FXML
    private VBox vboxWaitStart;
    @FXML
    private VBox vboxStartCard;
    @FXML
    private VBox vboxSecretObjs;
    @FXML
    private VBox vboxNoPlayers;
    @FXML
    private VBox vboxNickname;
    @FXML
    private AnchorPane paneRoom;
    @FXML
    private HBox hboxRoom;
    @FXML
    private RadioButton buttonR;
    @FXML
    private RadioButton buttonL;
    @FXML
    private VBox vboxRoom;
    @FXML
    private ImageView frontStarterCard, backStarterCard, secretObjLeft, secretObjRight;
    @FXML
    private Button buttonSubDraw;
    @FXML
    private TextField nickTextField, numberPlayersTF;
    @FXML
    private Label nickLabel, validLabel, loadingLabel;
    Button confirmRoom = new Button("Submit");
    TextField tfRoom = new TextField();
    Label labelRoom = new Label("Insert new room's name:");
    VBox vbox = new VBox(labelRoom, tfRoom);
    ComboBox<Label> menu = new ComboBox<>();
    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();
    @FXML
    private ImageView goldFaceDown, resFaceDown, goldFaceUp1, goldFaceUp2, resFaceUp1, resFaceUp2;
    private GUIClientSocket client;
    @FXML
    private GridPane gridPaneGround;
    @FXML
    private Label labelNick, labelPoints, mushroomNum, bugNum, leafNum, foxNum, potionNum, scrollNum, plumeNum;
    @FXML
    private ImageView handCardLeft, handCardCenter, handCardRight, secretObj;
    @FXML
    private static ImageView commonObj1, commonObj2;
    private int converter = 39; // 42 - 3 (coord iniziali matrice e gridPane)
    @FXML
    private Button nickButton, requestButton, yourTurnButton;
    private ArrayList<Position> availablePos = new ArrayList<>();
//    private final Image voidImage = new Image("url immagine vuota");
    private final String imagesFrontPath = "C:\\Users\\Ste\\Desktop\\Stefano\\UNI\\ANNO III\\INGEGNERIA DEL SOFTWARE\\PROGETTO_IDS\\ing-sw-2024-Parolin-Morato-Pedretti-Stagno\\CodexNaturalis\\src\\main\\resources\\CODEX_cards_gold_front\\";
    private final String imagesBackPath = "C:\\Users\\Ste\\Desktop\\Stefano\\UNI\\ANNO III\\INGEGNERIA DEL SOFTWARE\\PROGETTO_IDS\\ing-sw-2024-Parolin-Morato-Pedretti-Stagno\\CodexNaturalis\\src\\main\\resources\\CODEX_cards_gold_back\\";

/*
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Socket socket = new Socket("127.0.0.1",59090);
            client = new GUIClientSocket(socket.getInputStream(),socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

 */
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
    public VBox getVboxNick() {return vboxNickname;}
    public VBox getVboxRoom() {return vboxRoom;}
    public VBox getVboxNoPlayers() {return vboxNoPlayers;}
    public VBox getVboxSecretObjs() {return vboxSecretObjs;}
    public VBox getVboxStartCard() {return vboxStartCard;}
    public VBox getVboxWaitStart() {return vboxWaitStart;}
    public VBox getVboxWinners() {return vboxWinners;}
    public VBox getVboxWinner() {return vboxWinner;}

    public boolean addRoomsMenu(ArrayList<Room> rooms) throws IOException, ClassNotFoundException {
        final boolean[] flag = {false};
        confirmRoom.setVisible(false);
        confirmRoom.setPrefWidth(100);
        menu.setPrefWidth(200);
        menu.setPromptText("Available rooms:");
        HBox.setMargin(menu, new Insets(25, 0, 0, 350));
        menu.setStyle("-fx-text-background-color: black;");
        for (Room r: rooms){
            Label elem = new Label(r.getName());
            menu.getItems().add(elem);
        }
        hboxRoom.getChildren().clear();
        if(vboxRoom.getChildren().getLast()==confirmRoom){
            vboxRoom.getChildren().removeLast();
        }
        confirmRoom.setVisible(false);
        hboxRoom.getChildren().add(menu);
        menu.setOnAction(e ->{
            vboxRoom.getChildren().add(confirmRoom);
            VBox.setMargin(confirmRoom, new Insets(0, 0, 50, 400));
            confirmRoom.setVisible(true);
            confirmRoom.setOnAction(e2->{
                try {
                    client.sendToServer(false); // comunica al server che vuole joinare una stanza
                    client.sendToServer(menu.getSelectionModel().getSelectedItem().getText()); // comunica al server nome stanza
                    flag[0]  = true;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });
        return flag[0];
    }
    public boolean addRoomCreationInput(ArrayList<Room> rooms) throws IOException, ClassNotFoundException {
        final boolean[] flag = {false};
        labelRoom.setPrefWidth(200);
        tfRoom.setPrefWidth(200);
        vbox.setPrefWidth(200);
        confirmRoom.setPrefWidth(100);
        confirmRoom.setVisible(false);
        HBox.setMargin(vbox, new Insets(25, 0, 0, 350));
        hboxRoom.getChildren().clear();
        if (vboxRoom.getChildren().getLast() == confirmRoom) {
            vboxRoom.getChildren().removeLast();
        }
        confirmRoom.setVisible(false);
        hboxRoom.getChildren().add(vbox);
        vbox.getChildren().get(1).setOnKeyTyped(e -> {
            vboxRoom.getChildren().add(confirmRoom);
            VBox.setMargin(confirmRoom, new Insets(0, 0, 50, 400));
            confirmRoom.setVisible(true);
            confirmRoom.setOnAction(e2 -> {
                boolean found = false;
                try {
                    for (Room r : rooms) {
                        if (r.getName().equals(tfRoom.getText())) {
                            found = true;
                            tfRoom.setText("");
                            labelRoom.setPrefWidth(250);
                            labelRoom.setStyle("-fx-text-fill: #b31010");
                            labelRoom.setText("This name is already taken, choose a new one:");
                        }
                    }
                    if (!found) {
                        client.sendToServer(true);
                        client.sendToServer(tfRoom.getText());
                    }
                    if (!client.receiveBooleanFromServer()) {// controolo nome stanze contemporanee
                        flag[0] = true;
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });
        return flag[0];
    }

    public boolean sendNickname() throws IOException, ClassNotFoundException {
        String nickname;
        boolean flag = false;
        do {
            nickname = nickTextField.getText();
            if (!nickname.isEmpty()) {
                nickTextField.clear();
            }
            client.sendToServer(nickname);
            if(client.receiveBooleanFromServer()){
                flag = true;
            }
            else{
                nickLabel.setPrefWidth(800);    // eventualmente aggiungere un'altra label
                nickLabel.setStyle("-fx-text-fill: #b20b0b");
                nickLabel.setText("This name is already taken, choose another one:");
            }
        } while (!flag);
        return client.receiveBooleanFromServer();
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
                } else {
                    System.out.println(numberOfPlayers);
                    return  numberOfPlayers;
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
    public void addSecretObjImages() throws IOException, ClassNotFoundException {
        Player player = client.receivePlayerFromServer();   // server manda istanza player, setto già il nick per il ground
        labelNick.setText(player.getNickname());
        ObjectiveCard[] objs = client.receiveSecretObjsFromServer();
        secretObjLeft.setImage(new Image("file:" + imagesFrontPath + objs[0].getId() + ".png"));
        secretObjRight.setImage(new Image("file:" + imagesFrontPath + objs[1].getId() + ".png"));
    }
    public void sendSecret(int i) throws IOException {
        client.sendToServer(i);
    }
    public void addStarterImages() throws IOException, ClassNotFoundException {
        StarterCard card = client.receiveStarterCardFromServer();
        frontStarterCard.setImage(new Image("file:" + imagesFrontPath + card.getId() + ".png"));
        backStarterCard.setImage(new Image("file:" + imagesBackPath + card.getId() + ".png"));
    }
    public void sendIfStarterFlipped(boolean flip) throws IOException {
        client.sendToServer(flip);
    }

    public ArrayList<Room> getRooms() throws IOException, ClassNotFoundException {
        return client.receiveRoomsFromServer();
    }

    public boolean addGround() throws IOException, ClassNotFoundException {
        client.reset();
        Player p = client.receivePlayerFromServer();
        Game g = client.receiveGameFromServer();
        labelPoints.setText("Points: "+ p.getPlayerGround().getPlayerScore());
        addImages(p.getHand());
        addSecretObj(p.getHand().getObjCard());
        addCommonObj(g.getCommonObj());
        setTotalResource(p.getPlayerGround().getTotalResources());
        // show actual ground
        return client.receiveBooleanFromServer();
    }

    public void addImages(Hand hand) {
        handCardLeft.setImage(new Image(imagesFrontPath + hand.getCard(0).getId() + ".png"));
        handCardCenter.setImage(new Image(imagesFrontPath + hand.getCard(1).getId() + ".png"));
        handCardRight.setImage(new Image(imagesFrontPath + hand.getCard(2).getId() + ".png"));
    }

    public void addSecretObj(ObjectiveCard secretObjective) {
        secretObj.setImage(new Image(imagesFrontPath + secretObjective.getId() + ".png"));
    }

    // questo potrebbe andare bene statico perchè tutti condividono gli stessi commonObj
    public void addCommonObj(ObjectiveCard[] commonObj) {
        commonObj1.setImage(new Image(imagesFrontPath + commonObj[0].getId() + ".png"));
        commonObj2.setImage(new Image(imagesFrontPath + commonObj[1].getId() + ".png"));
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

    public boolean checkIfLast() throws IOException, ClassNotFoundException {
        return client.receiveBooleanFromServer();
    }
    /*
    public void placeFirstCard(StarterCard sc) throws IOException, ClassNotFoundException {
        ImageView iv = new ImageView(new Image(imagesFrontPath + sc.getId() + ".png"));
        gridPaneGround.add(iv, 3, 3);
        Player updated = client.receivePlayerFromServer();
        showAvailablePos(updated.getPlayerGround());
    }*/

    public boolean playCard() throws IOException, ClassNotFoundException {
        client.reset();
        Player player = client.receivePlayerFromServer();
        Game game = client.receiveGameFromServer();
        showAvailablePos(player.getPlayerGround());
        //  setDragDetected(handCardLeft);                  to do: setting drag and drop, depending on graphic structure of ground
        //  setDragDetected(handCardCenter);
        //  setDragDetected(handCardRight);
        //  setDropZones();
        //  setDropCompleted(handCardLeft);
        //  setDropCompleted(handCardCenter);
        //  setDropCompleted(handCardRight);
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
        buttonSubDraw.setVisible(false);
        resFaceUp1.setImage(new Image("src/main/resources/CODEX_cards_gold_front/" + decks[0].getCards().get(0).getId()));
        resFaceUp1.setImage(new Image("src/main/resources/CODEX_cards_gold_front/" + decks[0].getCards().get(1).getId()));
        resFaceDown.setImage(new Image("src/main/resources/CODEX_cards_gold_back/" + decks[0].getCards().get(2).getId()));
        goldFaceUp1.setImage(new Image("src/main/resources/CODEX_cards_gold_back/" + decks[1].getCards().get(0).getId()));
        goldFaceUp2.setImage(new Image("src/main/resources/CODEX_cards_gold_back/" + decks[1].getCards().get(1).getId()));
        goldFaceDown.setImage(new Image("src/main/resources/CODEX_cards_gold_back/" + decks[1].getCards().get(2).getId()));
    }


    public boolean yourTurnDraw() throws IOException {
        int[] choice = new int[] { -1 };
        DropShadow dropShadow = new DropShadow();
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
        client.sendToServer(choice[0]);
        return true;
    }

    public void putDrawnInHand(PlayableCard card){
        Image toPut = new Image(imagesFrontPath + card.getId() + ".png");
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

    public boolean waitForTurn(Stage stage) throws IOException, ClassNotFoundException {
        // aspetta turno (scritta che indica turno corrente?)
        // può vedere pg altri giocatori
        return client.receiveBooleanFromServer();
    }

    public ArrayList<Player> getWinners() throws IOException, ClassNotFoundException {
        return client.receiveWinnersFromServer();
    }

/*
    public void setDragDetected(ImageView iv) {  //iv da dove parto (hand) iv2 dove arrivo)
        iv.setOnDragDetected(event -> {
            Dragboard db = iv.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(iv.getImage());
            event.consume();
        });
    }

    public void setDropZones() {
        for (Position pos : availablePos) {
            ImageView zone = (ImageView) gridPaneGround.getChildren().get(pos.getY() * gridPaneGround.getColumnCount() + pos.getX());
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
            zone.setOnDragDropped(e -> {
                Dragboard db = e.getDragboard();
                boolean success = false;
                if (db.hasImage()) {
                    zone.setImage(db.getImage());
                    if(zone.getX() == 0 || zone.getY()==0 ){
                        gridPaneGround.addColumn(0);
                        gridPaneGround.addRow(0);
                        converter--;
                    }
                    if(zone.getX() == (gridPaneGround.getColumnCount()-1) || zone.getY() ==(gridPaneGround.getRowCount()-1)){
                        gridPaneGround.addColumn(gridPaneGround.getColumnCount());
                        gridPaneGround.addRow(gridPaneGround.getRowCount());
                    }
                    success = true;
                }
                e.setDropCompleted(success);
                e.consume();
            });
        }
    }

    public void setDropCompleted(ImageView iv){
        iv.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                iv.setImage(voidImage);

            }
            event.consume();
        });
    }*/

    public void showAvailablePos(PlayerGround pg){
        int x, y;
        Card[][] matrix = pg.getGround();
        for(int i = 0; i < 84; i++) {
            for (int j = 0; j < 84; j++) {
                x = i-converter;
                y = j-converter;
                Position pos = new Position(x, y);
                Card c = matrix[i][j];
                if (c.getId() == -1 && !isFound(availablePos, pos)) {
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


