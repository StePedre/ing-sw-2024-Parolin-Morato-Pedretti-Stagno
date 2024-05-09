package it.polimi.ingsw.View;

import it.polimi.ingsw.Model.Deck;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.effect.*;
import javafx.stage.Stage;

public class DrawController {

     @FXML
     private ImageView goldFaceDown, resFaceDown, goldFaceUp1, goldFaceUp2, resFaceUp1, resFaceUp2;
    private Button buttonSubDraw;
    public void addCards(Deck[] decks) {
        buttonSubDraw.setDisable(true);
        resFaceUp1.setImage(new Image("src/main/resources/CODEX_cards_gold_front/" + decks[0].getCards().get(0).getId()));
        resFaceUp1.setImage(new Image("src/main/resources/CODEX_cards_gold_front/" + decks[0].getCards().get(1).getId()));
        resFaceDown.setImage(new Image("src/main/resources/CODEX_cards_gold_back/" + decks[0].getCards().get(2).getId()));
        goldFaceUp1.setImage(new Image("src/main/resources/CODEX_cards_gold_back/" + decks[1].getCards().get(0).getId()));
        goldFaceUp2.setImage(new Image("src/main/resources/CODEX_cards_gold_back/" + decks[1].getCards().get(1).getId()));
        goldFaceDown.setImage(new Image("src/main/resources/CODEX_cards_gold_back/" + decks[1].getCards().get(2).getId()));
    }
    public int chooseCard(){
        int choice = -1;
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5);
        dropShadow.setColor(javafx.scene.paint.Color.BLUE);

        resFaceDown.setOnMouseClicked(event -> {
            choice = 0;
            resFaceDown.setEffect(dropShadow);
        });
        resFaceUp1.setOnMouseClicked(event -> {
            choice = 1;
            resFaceUp1.setEffect(dropShadow);
            buttonSubDraw.setDisable(false);
        });
        resFaceUp2.setOnMouseClicked(event ->{
            choice = 2;
            resFaceUp2.setEffect(dropShadow);
            buttonSubDraw.setDisable(false);
        });
        goldFaceDown.setOnMouseClicked(event ->{
            choice = 3;
            goldFaceDown.setEffect(dropShadow);
            buttonSubDraw.setDisable(false);
        });
        goldFaceUp1.setOnMouseClicked(event -> {
            choice = 4;
            goldFaceUp1.setEffect(dropShadow);
            buttonSubDraw.setDisable(false);
        });
        goldFaceUp2.setOnMouseClicked(event-> {
            choice = 5;
            goldFaceUp2.setEffect(dropShadow);
            buttonSubDraw.setDisable(false);
        });

        buttonSubDraw.setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
        return choice;
    }
}

