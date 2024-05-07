package it.polimi.ingsw.View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GUITest extends Application{

    @Test
    void ciccio(){
        GUI gui = new GUI();
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Label label = new Label("Insert your nickname, please:");
        Scene scene = new Scene(label, 400, 300);
        stage.setScene(scene);
        stage.showAndWait();
    }
}