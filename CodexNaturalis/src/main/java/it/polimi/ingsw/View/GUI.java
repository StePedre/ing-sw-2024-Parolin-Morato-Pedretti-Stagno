package it.polimi.ingsw.View;

import it.polimi.ingsw.Model.Hand;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public class GUI extends Application {

    @Override
    public void start(Stage Stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/insertNick.fxml"));
        Stage.setTitle("Codex Naturalis");
        Stage.setScene(new Scene(root, 350, 300));
        Stage.setFullScreen(true);
        Stage.show();
        // prima volta che si carica il playground:
        Parent root2 = FXMLLoader.load(getClass().getResource("/playerground.fxml"));
        // aggiungere la hand
        Stage.setTitle("Codex Naturalis");
        Stage.setScene(new Scene(root2, 350, 300));
        Stage.setFullScreen(true);
        Stage.show();
    }

    public static void main(String[] args){
        GUI.launch();
    }

    public void loadHand(Hand hand){
        HBox handZone = new HBox(37.5);  // distanza tra le carte
       // handZone.setPadding(new Insets(20));
       // handZone.getChildren().addAll(handCardleft, buttonL, handCardCenter, button2, handCardRight, button3);
    }

}

