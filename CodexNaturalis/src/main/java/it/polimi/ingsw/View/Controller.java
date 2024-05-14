package it.polimi.ingsw.View;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.ScoreRules.FlatRule;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller  implements  Initializable{

    @FXML
    private ImageView frontStarterCard, backStarterCard, secretObjLeft, secretObjRight;
    @FXML
    private ProgressBar loadingBar;
    @FXML
    private Button buttonSubDraw;
    @FXML
    private TextField nickTextField, numberPlayersTF;
    @FXML
    private Label validLabel, loadingLabel;
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
    private Button yourTurnButton;
    private ArrayList<Position> availablePos = new ArrayList<>();
    private final Image voidImage = new Image("url immagine vuota");
    private final String imagesFrontPath = "src/main/resources/CODEX_cards_gold_front/";
    private final String imagesBackPath = "src/main/resources/CODEX_cards_gold_back/";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Socket socket = new Socket("127.0.0.1",59090);
            client = new GUIClientSocket(socket.getInputStream(),socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public ProgressBar getLoadingBar() {
        return loadingBar;
    }

    public Label getLoadingLabel() {
        return loadingLabel;
    }

    public void switchToLogin(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/insertNick.fxml")));
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
    }

    public void animationLoadingBar() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(loadingBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(3), e -> {
                }, new KeyValue(loadingBar.progressProperty(), 1))
        );
        timeline.play();

    }

    public void getNickname(ActionEvent event) throws IOException, ClassNotFoundException {
        String nickname;
        do {
            nickname = nickTextField.getText();
            if (!nickname.isEmpty()) {
                nickTextField.clear();
            }
            client.sendToServer(nickname);
        }while(!client.receiveBooleanFromServer());
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        switchToNumberPlayers(stage);
    }



    public void switchToNumberPlayers(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/requestNoPlayers.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void getNumberPlayers(ActionEvent event) throws IOException {
        int numberOfPlayers;
        String inputText = numberPlayersTF.getText();
        if (!inputText.isEmpty()) {
            try {
                numberOfPlayers = Integer.parseInt(inputText);
                if (numberOfPlayers < 2 || numberOfPlayers > 4) {
                    validLabel.setVisible(true);
                    numberPlayersTF.clear();
                } else {
                    System.out.println(numberOfPlayers);
                    Scene scene = ((Node) event.getSource()).getScene();
                    Stage stage = (Stage) scene.getWindow();
                    switchToStarterChoice(stage);
                }
            } catch (NumberFormatException e) {
                validLabel.setVisible(true);
                numberPlayersTF.clear();
            }
        }

    }

    public void getSideStarterCard(ActionEvent event) throws IOException {

    }

    public void getLeftSecretObj(MouseEvent event) throws IOException {
        ObjectiveCard secretObj;
        //server.sendSecretObj (secretObj);
        System.out.println("Chosen left secret objective");
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        switchToWaitingStart(stage);
    }

    public void getRightSecretObj(MouseEvent event) throws IOException {
        ObjectiveCard secretObj;
        // associazione immagine-carta
        //server.sendSecretObj (secretObj);
        System.out.println("Chosen right secret objective");
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        switchToWaitingStart(stage);
    }

    public void switchToWaitingStart(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/waitingStart.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToStarterChoice(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/startingCardChoice.fxml")));
       // StarterCard card = client.receiveStarterCardFromServer();
        StarterCard card = new StarterCard(86, new FlatRule(0), new Corner[4], new Corner[4], Resource.BLANK, null);
        addStarterImages(card);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSelectSecretObj(MouseEvent event) throws IOException {
        // add method that returns the chosen side of the card!!!
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/selectSecretObjs.fxml")));
        // client manda secret objectives
        ObjectiveCard card1 = new ObjectiveCard(93, new FlatRule(0));
        ObjectiveCard card2 = new ObjectiveCard(91, new FlatRule(0));
        ObjectiveCard[] objs = {card1, card2};
        addSecretObjImages(objs);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void addStarterImages(StarterCard card) throws IOException {
        Image image1 = new Image("file:" + imagesFrontPath + card.getId() + ".png");
        Image image2 = new Image("file:" + imagesBackPath + card.getId() + ".png");
        frontStarterCard.setImage(image1);
        backStarterCard.setImage(image2);
        // TO DO: metodo che gestisce la scelta e la manda al server
    }

    public void addSecretObjImages(ObjectiveCard[] secretObjs) {
        secretObjLeft.setImage(new Image(imagesFrontPath + secretObjs[0].getId() + ".png"));
        secretObjRight.setImage(new Image(imagesFrontPath + secretObjs[1].getId() + ".png"));
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

    public void setNickname(String name) {
        labelNick.setText(name);
    }

    public void setScore(int score) {
        labelPoints.setText("Score: " + score);
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

    public void placeFirstCard(StarterCard sc) throws IOException, ClassNotFoundException {
        ImageView iv = new ImageView(new Image(imagesFrontPath + sc.getId() + ".png"));
        gridPaneGround.add(iv, 3, 3);
        Player updated = client.receivePlayerFromServer();
        showAvailablePos(updated.getPlayerGround());
    }


    public void close(ActionEvent e) throws IOException, ClassNotFoundException {    // chiusura finestra yourturn
        Stage stage = (Stage) yourTurnButton.getScene().getWindow();
        stage.close();
        playCard(stage);
    }

    public void playCard(Stage stage) throws IOException, ClassNotFoundException {
        setDragDetected(handCardLeft);
        setDragDetected(handCardCenter);
        setDragDetected(handCardRight);
        setDropZones();
        setDropCompleted(handCardLeft);
        setDropCompleted(handCardCenter);
        setDropCompleted(handCardRight);
        Player player = client.receivePlayerFromServer();
        showAvailablePos(player.getPlayerGround());
        Game game = client.receiveGameFromServer();
        addCards(game.getDecks());
        yourTurnDraw(stage);

    }


    public void addCards(Deck[] decks) {
        buttonSubDraw.setDisable(true);
        resFaceUp1.setImage(new Image("src/main/resources/CODEX_cards_gold_front/" + decks[0].getCards().get(0).getId()));
        resFaceUp1.setImage(new Image("src/main/resources/CODEX_cards_gold_front/" + decks[0].getCards().get(1).getId()));
        resFaceDown.setImage(new Image("src/main/resources/CODEX_cards_gold_back/" + decks[0].getCards().get(2).getId()));
        goldFaceUp1.setImage(new Image("src/main/resources/CODEX_cards_gold_back/" + decks[1].getCards().get(0).getId()));
        goldFaceUp2.setImage(new Image("src/main/resources/CODEX_cards_gold_back/" + decks[1].getCards().get(1).getId()));
        goldFaceDown.setImage(new Image("src/main/resources/CODEX_cards_gold_back/" + decks[1].getCards().get(2).getId()));
    }

    public void yourTurnDraw(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/drawpanel.fxml")));
        stage.setScene(new Scene(root, 350, 300));
        stage.show();
        int chosen = chooseCard();
        client.sendToServer(chosen);
    }
    public int chooseCard(){
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
            buttonSubDraw.setDisable(false);
        });
        resFaceUp2.setOnMouseClicked(event ->{
            choice[0] = 2;
            resFaceUp2.setEffect(dropShadow);
            buttonSubDraw.setDisable(false);
        });
        goldFaceDown.setOnMouseClicked(event ->{
            choice[0] = 3;
            goldFaceDown.setEffect(dropShadow);
            buttonSubDraw.setDisable(false);
        });
        goldFaceUp1.setOnMouseClicked(event -> {
            choice[0] = 4;
            goldFaceUp1.setEffect(dropShadow);
            buttonSubDraw.setDisable(false);
        });
        goldFaceUp2.setOnMouseClicked(event-> {
            choice[0] = 5;
            goldFaceUp2.setEffect(dropShadow);
            buttonSubDraw.setDisable(false);
        });

        buttonSubDraw.setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
        return choice[0];
    }

    public void putDrawnInHand(PlayableCard card){
        Image toPut = new Image(imagesFrontPath + card.getId() + ".png");
        if(handCardRight.getImage() == voidImage){
            handCardRight.setImage(toPut);
        }
        if(handCardCenter.getImage()==voidImage){
            handCardCenter.setImage(toPut);
        }
        if(handCardLeft.getImage()==voidImage){
            handCardLeft.setImage(toPut);
        }
    }

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
                if (c.getId() == -1 && !isFound(availablePos, pos)) {
                    gridPaneGround.add(new ImageView(voidImage), x, y);
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

    public void yourTurnBanner(Player player, Game game, Stage stage) throws IOException {
        Stage yourTurnStage = new Stage();
        yourTurnStage.initModality(Modality.WINDOW_MODAL);
        yourTurnStage.initOwner(stage);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/yourTurn.fxml")));
        stage.setScene(new Scene(root, 600, 200));
        stage.show();
    }

    public void InitializePlayerGround(Player player, StarterCard startercard, Stage stage) throws IOException, ClassNotFoundException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/playground.fxml")));
        setNickname(player.getNickname());
        addImages(player.getHand());
        addSecretObj(player.getHand().getObjCard());
        addCommonObj(player.getGame().getCommonObj());
        setScore(player.getPlayerGround().getPlayerScore());
        setTotalResource(player.getPlayerGround().getTotalResources());
        placeFirstCard(startercard);
        showAvailablePos(player.getPlayerGround());
        stage.setScene(new Scene(root, 350, 300));
        stage.show();  // finchè non è il suo turno
    }

    public void showUpdatedPlayerGround(Player player, Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/playground.fxml")));
        setNickname(player.getNickname());
        addImages(player.getHand());
        addSecretObj(player.getHand().getObjCard());
        addCommonObj(player.getGame().getCommonObj());
        setScore(player.getPlayerGround().getPlayerScore());
        setTotalResource(player.getPlayerGround().getTotalResources());
        stage.setScene(new Scene(root, 350, 300));
        stage.show();
    }
}


