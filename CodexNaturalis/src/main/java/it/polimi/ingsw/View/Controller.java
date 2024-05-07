package it.polimi.ingsw.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private Label labelNick;
    @FXML
    private TextField textFieldNick, textFieldPlayersNo;
    @FXML
    private Button buttonNick;

    //insert nickname and return to server
    public String submitNick(ActionEvent event) {
        return textFieldNick.getText();
    }

    // if first player:
    public int askPlayerNo(ActionEvent event) {
        int num = 0;
        do{
            System.out.println("How many players do you want? Insert a number between 2 and 4.");
            num = Integer.parseInt(textFieldPlayersNo.getText());
        } while (!(num>=2 && num<=4));
        return num;
    }
}
