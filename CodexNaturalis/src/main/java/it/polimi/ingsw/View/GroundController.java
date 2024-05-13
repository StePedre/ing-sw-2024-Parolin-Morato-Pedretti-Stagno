package it.polimi.ingsw.View;

import it.polimi.ingsw.Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class GroundController {
        @FXML
        private HBox hboxHand;
        @FXML
        private GridPane gridPaneGround;
        @FXML
        private ScrollPane scrollPaneGround;
        @FXML
        private AnchorPane stdAnchorPane;
        @FXML
        private Label labelNick, labelPoints;
        @FXML
        private Label mushroomNum, bugNum, leafNum, foxNum, potionNum, scrollNum, plumeNum;
        @FXML
        private ImageView handCardLeft;
        @FXML
        private ImageView handCardCenter;
        @FXML
        private ImageView handCardRight;
        @FXML
        private ImageView secretObj;
        @FXML
        private static ImageView commonObj1;
        @FXML
        private static ImageView commonObj2;
        private int converter = 39; // 42 - 3 (coord iniziali matrice e gridPane)
        @FXML
        private Button yourTurnButton;
        @FXML
        private Label yourTurnLabel1, yourTurnLabel2;
        private ArrayList<Position> availablePos = new ArrayList<>();
        private final Image voidImage = new Image("url immagine vuota");
        private final String imagesFrontPath = "src/main/resources/CODEX_cards_gold_front/";
        private final String imagesBackPath = "src/main/resources/CODEX_cards_gold_back/";

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


        public void close(ActionEvent e) throws IOException {    // chiusura finestra yourturn
                Stage stage = (Stage) yourTurnButton.getScene().getWindow();
                stage.close();
                playCard(stage);
        }

        public void playCard(Stage stage) throws IOException {
                setDragDetected(handCardLeft);
                setDragDetected(handCardCenter);
                setDragDetected(handCardRight);
                setDropZones();
                setDropCompleted(handCardLeft);
                setDropCompleted(handCardCenter);
                setDropCompleted(handCardRight);
                // receive updated ground
                // showAvailablePos(updated ground);

                DrawController dc = new DrawController();
                // chiedi game al server
                // dc.addCards(game.getDecks());
                dc.yourTurnDraw(stage);

        }

        // chiamato dal client
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



       public void placeFirstCard(StarterCard sc){
               ImageView iv = new ImageView(new Image(imagesFrontPath + sc.getId() + ".png"));
               gridPaneGround.add(iv, 3, 3);
       }
       // send to server and receive updated ground (with available positions)

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

        public void InitializePlayerGround(Player player, StarterCard startercard, Stage stage) throws IOException {
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