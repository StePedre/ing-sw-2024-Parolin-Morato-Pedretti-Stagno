package it.polimi.ingsw.View;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Hand;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.Player;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.Objects;

public class GUI {
    private Stage stage;
    private Screen screen = Screen.getPrimary();
    private double screenHeight = screen.getBounds().getHeight();
    private double screenWidth = screen.getBounds().getWidth();
    private final int height = 400;
    private final int width = 600;

    /*
    @Override
    public void start(Stage Stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/insertNick.fxml"));
        Stage.setTitle("Codex Naturalis");
        Stage.setScene(new Scene(root, 350, 300));
        Stage.setFullScreen(true);
        Stage.show();
        // prima volta che si carica il playground:
       /* Parent root2 = FXMLLoader.load(getClass().getResource("/playerground.fxml"));
        // aggiungere la hand
        Stage.setTitle("Codex Naturalis");
        Stage.setScene(new Scene(root2, 350, 300));
        Stage.setFullScreen(true);
        Stage.show();


    }

    public static void main(String[] args){
        GUI.launch();
    }

     */

    public GUI (Stage stage) {
        this.stage = stage;
    }

    public void showLoadingScreen (Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/loadingScreen.fxml"));
        stage.setTitle("Codex Naturalis");
        stage.setScene(new Scene(root, 350, 300));
        stage.setFullScreen(true);
        stage.show();
    }

    public void loadPlayerGround(Stage stage, Player player, Game game) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/playground.fxml"));
        GroundController.setNickname(player.getNickname());
        GroundController.addImages(player.getHand());
        GroundController.addSecretObj(player.getHand().getObjCard());
        GroundController.addCommonObj(player.getGame().getCommonObj());
        GroundController.setScore(player.getPlayerGround().getPlayerScore());
        GroundController.setTotalResource(player.getPlayerGround().getTotalResources());
        stage.setTitle("Codex Naturalis");
        stage.setScene(new Scene(root, 350, 300));
        stage.setFullScreen(true);
        stage.show();
    }

/*
    public void switchToLogin (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/insertNick.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    String submitNick(ActionEvent event) {
        System.out.println(nickTextField.getText());
        return nickTextField.getText();
    }

    public int askPlayerNo(ActionEvent event) {
        int num = 0;
        do{
            System.out.println("How many players do you want? Insert a number between 2 and 4.");
            num = Integer.parseInt(textFieldPlayersNo.getText());
        } while (!(num>=2 && num<=4));
        return num;
    }

 */


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

}

