package it.polimi.ingsw.View;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.ScoreRules.FlatRule;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.management.MemoryUsage;
import java.util.Objects;

public class GUI extends Application{
    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/loadingScene.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        stage.setTitle("Codex Naturalis");
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(false);
        stage.show();

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                try {
                    switchToLogin(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    public void switchToLogin(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insertNick.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();

        Button nickButton = controller.getNickButton();
        nickButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                controller.getNickname();
                try {
                    switchToNumberPlayers(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }



    public void switchToNumberPlayers(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/requestNoPlayers.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Button requestButton = controller.getRequestButton();
        requestButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int numberPlayers = controller.getNumberPlayers();
                try {
                    if (numberPlayers >= 2 && numberPlayers <= 4) {
                        switchToStarterChoice(stage);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


/*
    public void switchToWaitingStart(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/waitingStart.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
*/

    public void switchToStarterChoice(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/startingCardChoice.fxml")));
        // client manda starter card...
        //StarterCard card = new StarterCard(86, new FlatRule(0), new Corner[4], new Corner[4], Resource.BLANK, null);
        //addStarterImages(card);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

/*
    public void switchToSelectSecretObj(MouseEvent event) throws IOException {
        // add method that returns the chosen side of the card!!!
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/selectSecretObjs.fxml")));
        // client manda secret objectives
        ObjectiveCard card1 = new ObjectiveCard(93, new FlatRule(0));
        ObjectiveCard card2 = new ObjectiveCard(91, new FlatRule(0));
        ObjectiveCard[] objs = {card1, card2};
        addSecretObjImages(objs);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void yourTurnDraw(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/drawpanel.fxml")));
        stage.setScene(new Scene(root, 350, 300));
        stage.show();
        int chosen = chooseCard();
        client.sendToServer(chosen);
    }

    public void yourTurnBanner(Player player, Game game, Stage stage) throws IOException {
        Stage yourTurnStage = new Stage();
        yourTurnStage.initModality(Modality.WINDOW_MODAL);
        yourTurnStage.initOwner(stage);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/yourTurn.fxml")));
        stage.setScene(new Scene(root, 600, 200));
        stage.show();
    }

    public void InitializePlayerGround(Player player, StarterCard startercard, Stage stage) throws IOException, ClassNotFoundException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/playground.fxml")));
        setNickname(player.getNickname());
        addImages(player.getHand());
        addSecretObj(player.getHand().getObjCard());
        addCommonObj(player.getGame().getCommonObj());
        setScore(player.getPlayerGround().getPlayerScore());
        setTotalResource(player.getPlayerGround().getTotalResources());
        placeFirstCard(startercard);
        showAvailablePos(player.getPlayerGround());
        stage.setScene(new Scene(root, 350, 300));
        stage.show();  // finchè non è il suo turno
    }

    public void showUpdatedPlayerGround(Player player, Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/playground.fxml")));
        setNickname(player.getNickname());
        addImages(player.getHand());
        addSecretObj(player.getHand().getObjCard());
        addCommonObj(player.getGame().getCommonObj());
        setScore(player.getPlayerGround().getPlayerScore());
        setTotalResource(player.getPlayerGround().getTotalResources());
        stage.setScene(new Scene(root, 350, 300));
        stage.show();
    }

     */

    public static void startGUI(){
        launch();
    }

    public static void main(String[] args) {
        launch();
    }

}

