package it.polimi.ingsw.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Controller {
    @FXML
    private ProgressBar loadingBar;
    @FXML
    private Label nickLabel, loadingLabel;
    @FXML
    private TextField nickTextField;
    @FXML
    private Button nickButton, loadingButton;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Screen screen = Screen.getPrimary();
    private double screenHeight = screen.getBounds().getHeight();
    private double screenWidth = screen.getBounds().getWidth();
    private final int height = 400;
    private final int width = 600;

    public void initialize () {
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
    }

    public void switchToLogin (Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/insertNick.fxml"));
        scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setMaximized(true);
        //stage.setFullScreen(true);
        stage.show();
    }

    //insert nickname and return to server


    // if first player:




}

