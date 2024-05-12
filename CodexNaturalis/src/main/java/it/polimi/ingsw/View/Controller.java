package it.polimi.ingsw.View;

import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.ObjectiveCard;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.function.UnaryOperator;

public class Controller {
    @FXML
    private ProgressBar loadingBar;
    @FXML
    private Button nickButton;
    @FXML
    private TextField nickTextField;
    @FXML
    private ImageView secretObjLeft;
    @FXML
    private ImageView secretObjRight;
    @FXML
    private TextField numberPlayersTF;
    @FXML
    private Label validLabel;
    @FXML
    private Label loadingLabel;
    private Screen screen = Screen.getPrimary();
    private double screenHeight = screen.getBounds().getHeight();
    private double screenWidth = screen.getBounds().getWidth();

    //public void initialize () {
        /*
        double nodeHeight = nickLabel.getPrefHeight();
        double nodeWidth = nickLabel.getPrefWidth();
        adjustLayout(nickLabel, screenHeight, screenWidth, nodeHeight, nodeWidth);
        double newFontSize = calculateSizeFont(screenHeight, screenWidth, nickLabel.getFont().getSize());
        setFontSizeLabel(nickLabel, newFontSize);

        nodeHeight = loadingLabel.getPrefHeight();
        nodeWidth = loadingLabel.getPrefWidth();
        adjustLayout(loadingLabel, screenHeight, screenWidth, nodeHeight, nodeWidth);
        newFontSize = calculateSizeFont(screenHeight, screenWidth, loadingLabel.getFont().getSize());
        setFontSizeLabel(loadingLabel, newFontSize);

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

        nodeHeight = loadingButton.getPrefHeight();
        nodeWidth = loadingButton.getPrefWidth();
        adjustLayout(loadingButton, screenHeight, screenWidth, nodeHeight, nodeWidth);
        newFontSize = calculateSizeFont(screenHeight, screenWidth, loadingButton.getFont().getSize());
        setFontSizeButton(loadingButton, newFontSize);

        nodeHeight = loadingBar.getPrefHeight();
        nodeWidth = loadingBar.getPrefWidth();
        adjustLayout(loadingBar, screenHeight, screenWidth, nodeHeight, nodeWidth);

         */
    //}

    public ProgressBar getLoadingBar () {
        return loadingBar;
    }

    public Label getLoadingLabel () {
        return loadingLabel;
    }

    public void switchToLogin (Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/insertNick.fxml"));
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
    }

    public void animationLoadingBar () {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(loadingBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(3), e-> {
                }, new KeyValue(loadingBar.progressProperty(), 1))
        );
        timeline.play();

    }

    public void getNickname (ActionEvent event) throws IOException {
        String nickname = nickTextField.getText();
        if (!nickname.isEmpty()) {
            System.out.println(nickname);
            //server.sendNickname (nickname);
            nickTextField.clear();
        }
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        switchToNumberPlayers(stage);
    }

    public void switchToNumberPlayers (Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/requestNoPlayers.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void getNumberPlayers (ActionEvent event) throws IOException {
        int numberOfPlayers = 0;
        String inputText = numberPlayersTF.getText();
        if (!inputText.isEmpty()) {
            try {
                numberOfPlayers = Integer.parseInt(inputText);
                if (numberOfPlayers < 2 || numberOfPlayers > 4) {
                    validLabel.setVisible(true);
                    numberPlayersTF.clear();
                }
                else {
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

    public void getSideStarterCard (ActionEvent event) throws  IOException {

    }

    public void getLeftSecretObj (MouseEvent event) throws IOException {
        ObjectiveCard secretObj;
        // associazione immagine-carta
        //server.sendSecretObj (secretObj);
        System.out.println("Chosen left secret objective");
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        switchToWaitingStart(stage);
    }

    public void getRightSecretObj (MouseEvent event) throws IOException {
        ObjectiveCard secretObj;
        // associazione immagine-carta
        //server.sendSecretObj (secretObj);
        System.out.println("Chosen right secret objective");
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        switchToWaitingStart(stage);
    }

    public void switchToWaitingStart (Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/waitingStart.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToStarterChoice (Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/startingCardChoice.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSelectSecretObj (MouseEvent event) throws IOException {
        // add method that returns the chosen side of the card!!!
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/selectSecretObjs.fxml"));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}

