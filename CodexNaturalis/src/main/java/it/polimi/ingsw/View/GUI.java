package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.Corner;
import it.polimi.ingsw.Model.ObjectiveCard;
import it.polimi.ingsw.Model.Resource;
import it.polimi.ingsw.Model.ScoreRules.FlatRule;
import it.polimi.ingsw.Model.StarterCard;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GUI extends Application{
    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/loadingScene.fxml"));
        Parent root = loader.load();
        stage.setTitle("Codex Naturalis");
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(false);
        stage.show();

        scene.setOnKeyReleased(keyEvent -> {
            try {
                switchToRoomChoice(stage);
                // switchToLogin(stage);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void switchToRoomChoice(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/roomChoice.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        VBox vboxRoom = controller.getVboxRoom();
        adjustLayout(vboxRoom, screenHeight, screenWidth, 1280, 720);
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
        ArrayList<Room> rooms = controller.getRooms();
        RadioButton buttonL = controller.getButtonL();
        RadioButton buttonR = controller.getButtonR();
        buttonL.getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return; //no button selected
            }
            if (newValue.equals(buttonL)) {
                try {
                    if(controller.addRoomsMenu(rooms)){
                        switchToLogin(stage);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (newValue.equals(buttonR)) {
                try {
                    if (controller.addRoomCreationInput(rooms)) {
                        switchToLogin(stage);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void switchToLogin(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insertNick.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        VBox vboxNick = controller.getVboxNick();
        adjustLayout(vboxNick, screenHeight, screenWidth, 1366, 768);
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
        Button nickButton = controller.getNickButton();
        final boolean[] isFirst = {false};
        nickButton.setOnAction(actionEvent -> {
            try {
                isFirst[0] = controller.sendNickname();
                if(isFirst[0]){
                    switchToNumberPlayers(stage);
                }
                else{
                    switchToSelectSecretObj(stage);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void switchToNumberPlayers(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/requestNoPlayers.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        VBox vboxNoPlayers = controller.getVboxNoPlayers();
        adjustLayout(vboxNoPlayers, screenHeight, screenWidth, 1366, 768);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Button requestButton = controller.getRequestButton();
        requestButton.setOnAction(event -> {
            int numberPlayers = controller.getNumberPlayers();
            try {
                if (numberPlayers >= 2 && numberPlayers <= 4) {
                    switchToSelectSecretObj(stage);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void switchToSelectSecretObj(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/selectSecretObjs.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //ObjectiveCard card1 = new ObjectiveCard(93, new FlatRule(0)); just for test
        // ObjectiveCard card2 = new ObjectiveCard(91, new FlatRule(0));
        // ObjectiveCard[] objs = {card1, card2};
        controller.addSecretObjImages();
        ImageView secretObjLeft = controller.getSecretObjLeft();
        secretObjLeft.setOnMouseClicked(mouseEvent -> {
            try {
                controller.sendSecret(1);
                switchToStarterChoice(stage);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        ImageView secretObjRight = controller.getSecretObjRight();
        secretObjRight.setOnMouseClicked(mouseEvent -> {
            try {
                controller.sendSecret(2);
                switchToStarterChoice(stage);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void switchToStarterChoice(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startingCardChoice.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
      // for testing  StarterCard card = new StarterCard(86, new FlatRule(0), new Corner[4], new Corner[4], Resource.BLANK, null);
        controller.addStarterImages();
        ImageView frontStarterCard = controller.getFrontStarterCard();
        frontStarterCard.setOnMouseClicked(mouseEvent -> {
            try {
                controller.sendIfStarterFlipped(false);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ImageView backStarterCard = controller.getBackStarterCard();
        backStarterCard.setOnMouseClicked(mouseEvent -> {
            try {
                controller.sendIfStarterFlipped(true);
                showUpdatedPlayerGround(stage);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // usarlo durante while vuoto del server
    public void switchToWaitingStart(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/waitingStart.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /*

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
    }*/

    public void showUpdatedPlayerGround(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/playground.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        controller.addGround();
    }

    public static void startGUI(){
        launch();
    }

    public static void main(String[] args) {
        launch();
    }

    public void adjustLayout (Node node, double screenHeight, double screenWidth, int sceneBuilderHeight, int sceneBuilderWidth) {
        double nodeHeight, nodeWidth;
        if (node instanceof VBox nodeVBox) {
            nodeHeight = nodeVBox.getPrefHeight();
            nodeWidth = nodeVBox.getPrefWidth();
        } else if (node instanceof HBox nodeHBox) {
            nodeHeight = nodeHBox.getPrefHeight();
            nodeWidth = nodeHBox.getPrefWidth();
        } else {
            System.out.println("Error in resizing...");
        }
        double newAnchor = AnchorPane.getTopAnchor(node) * screenHeight / sceneBuilderHeight;
        AnchorPane.setTopAnchor(node, newAnchor);
        newAnchor = AnchorPane.getBottomAnchor(node) * screenHeight / sceneBuilderHeight;
        AnchorPane.setBottomAnchor(node, newAnchor);
        newAnchor = AnchorPane.getLeftAnchor(node) * screenWidth / sceneBuilderWidth;
        AnchorPane.setLeftAnchor(node, newAnchor);
        newAnchor = AnchorPane.getRightAnchor(node) * screenWidth / sceneBuilderWidth;
        AnchorPane.setRightAnchor(node, newAnchor);

    }

}

