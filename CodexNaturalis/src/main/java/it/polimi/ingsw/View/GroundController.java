package it.polimi.ingsw.View;
import it.polimi.ingsw.Model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Set;


public class GroundController {

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

        public void addImages(Hand hand){
            handCardLeft.setImage(new Image("src/main/resources/CODEX_cards_gold_front/"+String.valueOf(hand.getCard(0).getId()) + ".png"));
            handCardCenter.setImage(new Image("src/main/resources/CODEX_cards_gold_front/"+String.valueOf(hand.getCard(1).getId()) + ".png"));
            handCardRight.setImage(new Image("src/main/resources/CODEX_cards_gold_front/"+String.valueOf(hand.getCard(2).getId()) + ".png"));
        }

        public void addSecretObj(ObjectiveCard secretObjective){
            secretObj.setImage(new Image("src/main/resources/CODEX_cards_gold_front/"+String.valueOf(secretObjective.getId()) + ".png"));
        }
        // questo potrebbe andare bene statico perchè tutti condividono gli stessi commonObj
        public void addCommonObj(ObjectiveCard[] commonObj){
            commonObj1.setImage(new Image("src/main/resources/CODEX_cards_gold_front/"+String.valueOf(commonObj[0].getId()) + ".png"));
            commonObj2.setImage(new Image("src/main/resources/CODEX_cards_gold_front/"+String.valueOf(commonObj[1].getId()) + ".png"));
        }

        public void setNickname(String name){
                labelNick.setText(name);
        }

        public void setScore(int score){
                labelPoints.setText("Score: " + String.valueOf(score));
        }

        public void setTotalResource(HashMap<Resource, Integer> map){
                mushroomNum.setText(String.valueOf(map.get(Resource.MUSHROOM)));
                bugNum.setText(String.valueOf(map.get(Resource.BUG)));
                foxNum.setText(String.valueOf(map.get(Resource.FOX)));
                leafNum.setText(String.valueOf(map.get(Resource.LEAF)));
                potionNum.setText(String.valueOf(map.get(Resource.POTION)));
                scrollNum.setText(String.valueOf(map.get(Resource.SCROLL)));
                plumeNum.setText(String.valueOf(map.get(Resource.PLUME)));
        }

       // prima scelta della carta da giocare con drag and drop (to do), si manda al server, si riceve matrice aggiornata e si proietta
        public void placeCard(Card cardToPlace, Player player){
                Card c;
                ImageView ivVoid = new ImageView(new Image("immagine carta vuota")); // da cercare
                Card[][] matrix = player.getPlayerGround().getGround();
                for(int i = 0; i<84; i++){
                        for(int j = 0; j<84; j++){
                                c = matrix[i][j];
                                if(c==cardToPlace){
                                        ImageView iv = new ImageView(new Image("src/main/resources/CODEX_cards_gold_front/"+String.valueOf(c.getId()) + ".png"));
                                        gridPaneGround.add(iv, i-converter, j-converter);
                                        if(i-converter == 0 || j-converter==0 ){
                                                gridPaneGround.addColumn(0);
                                                gridPaneGround.addRow(0);
                                                converter--;
                                        }
                                        if(i-converter == (gridPaneGround.getColumnCount()-1) || j-converter==(gridPaneGround.getRowCount()-1)){
                                                gridPaneGround.addColumn(gridPaneGround.getColumnCount());
                                                gridPaneGround.addRow(gridPaneGround.getRowCount());
                                        }
                                }
                                if(c.getId()==-1){
                                        gridPaneGround.add(ivVoid, i-converter, j-converter);
                                }
                        }
                }
        }


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