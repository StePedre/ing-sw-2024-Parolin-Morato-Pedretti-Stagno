package it.polimi.ingsw.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import java.awt.*;

public class Controller {
    @FXML
    private AnchorPane nickAnchorPane;
    @FXML
    private Label nickLabel;
    @FXML
    private TextField nickTextField;
    @FXML
    private Button nickButton;
    private final int height = 400;
    private final int width = 600;

    public void initialize () {
        Screen screen = Screen.getPrimary();
        double screenHeight = screen.getBounds().getHeight();
        double screenWidth = screen.getBounds().getWidth();
        double nodeHeight = nickLabel.getPrefHeight();
        double nodeWidth = nickLabel.getPrefWidth();
        adjustLayout(nickLabel, screenHeight, screenWidth, nodeHeight, nodeWidth);
        double newFontSize = calculateSizeFont(screenHeight, screenWidth, nickLabel.getFont().getSize());
        setFontSizeLabel(nickLabel, newFontSize);

        nodeHeight = nickTextField.getPrefHeight();
        nodeWidth = nickTextField.getPrefWidth();
        adjustLayout(nickTextField, screenHeight, screenWidth, nodeHeight, nodeWidth);
        newFontSize = calculateSizeFont(screenHeight, screenWidth, nickTextField.getFont().getSize());
        setFontSizeTextField(nickTextField, newFontSize);

        nodeHeight = nickButton.getPrefHeight();
        nodeWidth = nickButton.getPrefWidth();
        adjustLayout(nickButton, screenHeight, screenWidth, nodeHeight, nodeWidth);
        newFontSize = calculateSizeFont(screenHeight, screenWidth, nickButton.getFont().getSize());
        setFontSizeButton(nickButton, newFontSize);
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

    public double calculateSizeFont (double screenHeight, double screenWidth, double fontSize) {
        return 0.5 * fontSize * (screenHeight * screenWidth) / (height * width);
    }

    public void setFontSizeLabel (Label label, double size) {
        Font originalFont = label.getFont();
        Font newFont = new Font(originalFont.getFamily(), size);
        label.setFont(newFont);
    }

    public void setFontSizeTextField (TextField tf, double size) {
        Font originalFont = tf.getFont();
        Font newFont = new Font(originalFont.getFamily(), size);
        tf.setFont(newFont);
    }

    public void setFontSizeButton (Button button, double size) {
        Font originalFont = button.getFont();
        Font newFont = new Font(originalFont.getFamily(), size);
        button.setFont(newFont);
    }

    //insert nickname and return to server
    String submitNick(ActionEvent event) {
        System.out.println(nickTextField.getText());
        return nickTextField.getText();
    }

    // if first player:
    /*
    public int askPlayerNo(ActionEvent event) {
        int num = 0;
        do{
            System.out.println("How many players do you want? Insert a number between 2 and 4.");
            num = Integer.parseInt(textFieldPlayersNo.getText());
        } while (!(num>=2 && num<=4));
        return num;
    }

     */
}

