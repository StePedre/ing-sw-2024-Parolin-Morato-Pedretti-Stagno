package it.polimi.ingsw.View;
import com.sun.javafx.scene.ImageViewHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import javax.swing.text.html.ImageView;

public class GroundController {
        @FXML
        private AnchorPane stdAnchorPane;
        @FXML
        private Label labelSecret, labelCommon, labelNick, labelPoints, labelRes;
        @FXML
        private Label mushroomNum, bugNum, leafNum, foxNum, potionNum, scrollNum, plumeNum;
        @FXML
        private ImageView handCardLeft, handCardCenter, handCardRight, secretObj, commonObj1, commonObj2;
        @FXML
        private ImageView mushroomRes, bugRes, leafRes, foxRes, potionRes, scrollRes, plumeRes;

        private final int height = 400;
        private final int width = 600;

        public void initialize() {
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
        }


}