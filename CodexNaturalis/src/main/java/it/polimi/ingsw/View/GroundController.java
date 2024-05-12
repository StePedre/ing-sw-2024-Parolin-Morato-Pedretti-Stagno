package it.polimi.ingsw.View;
import it.polimi.ingsw.Model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


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
        private Image voidImage = new Image("url immagine vuota");

        public void addImages(Hand hand) {
                handCardLeft.setImage(new Image("src/main/resources/CODEX_cards_gold_front/" + String.valueOf(hand.getCard(0).getId()) + ".png"));
                handCardCenter.setImage(new Image("src/main/resources/CODEX_cards_gold_front/" + String.valueOf(hand.getCard(1).getId()) + ".png"));
                handCardRight.setImage(new Image("src/main/resources/CODEX_cards_gold_front/" + String.valueOf(hand.getCard(2).getId()) + ".png"));
        }

        public void addSecretObj(ObjectiveCard secretObjective) {
                secretObj.setImage(new Image("src/main/resources/CODEX_cards_gold_front/" + String.valueOf(secretObjective.getId()) + ".png"));
        }

        // questo potrebbe andare bene statico perchè tutti condividono gli stessi commonObj
        public void addCommonObj(ObjectiveCard[] commonObj) {
                commonObj1.setImage(new Image("src/main/resources/CODEX_cards_gold_front/" + String.valueOf(commonObj[0].getId()) + ".png"));
                commonObj2.setImage(new Image("src/main/resources/CODEX_cards_gold_front/" + String.valueOf(commonObj[1].getId()) + ".png"));
        }

        public void setNickname(String name) {
                labelNick.setText(name);
        }

        public void setScore(int score) {
                labelPoints.setText("Score: " + String.valueOf(score));
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
                Image toPut = new Image("src/main/resources/CODEX_cards_gold_front/" + String.valueOf(card.getId()) + ".png");
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
               ImageView iv = new ImageView(new Image("src/main/resources/CODEX_cards_gold_front/"+String.valueOf(sc.getId()) + ".png"));
               gridPaneGround.add(iv, 3, 3);
       }
       // send to server and receive updated ground (with available positions)

        public void showAvailablePos(PlayerGround pg){
                int x, y;
                Card[][] matrix = pg.getGround();
                for(int i = 0; i<84; i++) {
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

       /* public void placeFirstCard(StarterCard starterCard, Player player){
                ImageView ivVoid = new ImageView("url image blank");
                Card[][] matrix = player.getPlayerGround().getGround();
                for(int i = 0; i<84; i++){
                        for(int j = 0; j<84; j++){
                                c = matrix[i][j];
                                if(c==cardToPlace){
                                        ImageView iv = new ImageView(new Image("src/main/resources/CODEX_cards_gold_front/"+String.valueOf(c.getId()) + ".png"));
                                        gridPaneGround.add(iv, i-converter, j-converter);


                                }
                                if(c.getId()==-1){
                                        gridPaneGround.add(ivVoid, i-converter, j-converter);
                                        availablePos.add(new Position(i-converter, j-converter));
                                }
                        }
                }
        }*/


     /*   public void initialize() {
            Screen screen = Screen.getPrimary();
            double screenHeight = screen.getBounds().getHeight();
            double screenWidth = screen.getBounds().getWidth();
            adjustLabel(labelNick, screenHeight, screenWidth);
            adjustLabel(labelPoints, screenHeight, screenWidth);
            adjustLabel(labelSecret, screenHeight, screenWidth);
            adjustLabel(labelCommon, screenHeight, screenWidth);
            adjustLabel(labelRes, screenHeight, screenWidth);
            adjustLabel(mushroomNum, screenHeight, screenWidth);
            adjustLabel(bugNum, screenHeight, screenWidth);
            adjustLabel(leafNum, screenHeight, screenWidth);
            adjustLabel(foxNum, screenHeight, screenWidth);
            adjustLabel(potionNum, screenHeight, screenWidth);
            adjustLabel(scrollNum, screenHeight, screenWidth);
            adjustLabel(plumeNum, screenHeight, screenWidth);
        }

        public void adjustLayout (Node node, double screenHeight, double screenWidth, double nodeHeight, double nodeWidth) {
            double newAnchor = AnchorPane.getTopAnchor(node) * screenHeight / height;
            AnchorPane.setTopAnchor(node, newAnchor);
            newAnchor = AnchorPane.getBottomAnchor(node) * screenHeight / height;
            AnchorPane.setBottomAnchor(node, newAnchor);
            newAnchor = AnchorPane.getLeftAnchor(node) * screenWidth / width;
            AnchorPane.setLeftAnchor(node, newAnchor);
            newAnchor = AnchorPane.getRightAnchor(node) * screenWidth / width;
            AnchorPane.setRightAnchor(node, newAnchor);

            double newSize = nodeHeight * screenHeight / height;
            node.prefHeight(newSize);
            newSize = nodeWidth * screenWidth / width;
            node.prefHeight(newSize);
        }

        public void adjustLabel(Label label, double screenHeight, double screenWidth){
            double nodeHeight = label.getPrefHeight();
            double nodeWidth = label.getPrefWidth();
            adjustLayout(label, screenHeight, screenWidth, nodeHeight, nodeWidth);
            double newFontSize = calculateSizeFont(screenHeight, screenWidth, label.getFont().getSize());
            setFontSizeLabel(label, newFontSize);
        }


        public double calculateSizeFont (double screenHeight, double screenWidth, double fontSize) {
            return 0.5 * fontSize * (screenHeight * screenWidth) / (height * width);
        }

        public void setFontSizeLabel (Label label, double size) {
            Font originalFont = label.getFont();
            Font newFont = new Font(originalFont.getFamily(), size);
            label.setFont(newFont);
        }

        public void setFontSizeButton (Button button, double size) {
            Font originalFont = button.getFont();
            Font newFont = new Font(originalFont.getFamily(), size);
            button.setFont(newFont);
        }*/


}