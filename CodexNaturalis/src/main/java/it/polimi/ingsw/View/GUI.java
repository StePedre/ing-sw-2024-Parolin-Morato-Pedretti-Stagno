package it.polimi.ingsw.View;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public class GUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/insertNickname.fxml"));
        stage.setTitle("Codex Naturalis");
        stage.setScene(new Scene(root, 350, 300));
        stage.show();
    }

    public static void main(String[] args){
        GUI.launch();
    }



}

