package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.ScoreRules.FlatRule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class GUI extends Application{
    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();
    private GUIClientSocket client;

    @Override
    public void start(Stage stage) throws IOException {
        Socket socket = new Socket("127.0.0.1", 59090);
        client = new GUIClientSocket(socket.getInputStream(),socket.getOutputStream());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/loadingScene.fxml"));
        Parent root = loader.load();
        stage.setTitle("Codex Naturalis");
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(false);
        stage.show();
        Controller controller = loader.getController();
        controller.setStreams(client);
        scene.setOnKeyReleased(keyEvent -> {
            try {
                switchToRoomChoice(stage);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void switchToRoomChoice(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/roomChoice.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        VBox vboxRoom = controller.getVboxRoom();
        adjustLayout(vboxRoom, screenHeight, screenWidth, 720, 1280);
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
        ArrayList<Room> rooms = client.receiveRoomsFromServer();
        RadioButton buttonL = controller.getButtonL();
        RadioButton buttonR = controller.getButtonR();
        buttonL.getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return; //no button selected
            }
            if (newValue.equals(buttonR)) {
                try {
                    controller.addRoomCreationInput(rooms);
                    controller.getConfirmRoom().setOnAction(e -> {
                        boolean found, flag;
                        try {
                                found = false;
                                for (Room r : rooms) {
                                    if (r.getName().equals(controller.getTfRoom().getText())) {
                                        found = true;
                                        controller.getTfRoom().setText("");
                                        controller.getLabelRoom().setPrefWidth(250);
                                        controller.getLabelRoom().setStyle("-fx-text-fill: #b31010");
                                        controller.getLabelRoom().setText("This name is already taken, choose a new one:");
                                        break;

                                    }
                                }
                                if (!found) {
                                    client.sendToServer(true);
                                    client.sendToServer(controller.getTfRoom().getText());
                                    flag = client.receiveBooleanFromServer();
                                    if(flag){
                                        controller.getTfRoom().setText("");
                                        controller.getLabelRoom().setText("Too late! Someone else has just created a room with this name! Please input another one:");
                                    }
                                    else{
                                        switchToLogin(stage);
                                    }
                                }
                        } catch (IOException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }else if (newValue.equals(buttonL)) {
                try{
                    controller.addRoomsMenu(rooms);
                    controller.getMenu().setOnAction(e ->{
                        vboxRoom.getChildren().add(controller.getConfirmRoom());
                        VBox.setMargin(controller.getConfirmRoom(), new Insets(0, 0, 50, 400));
                        controller.getConfirmRoom().setVisible(true);
                        controller.getConfirmRoom().setOnAction(e2->{
                            boolean flag;
                            try {
                                client.sendToServer(false); // comunica al server che vuole joinare una stanza
                                client.sendToServer(controller.getMenu().getSelectionModel().getSelectedItem().getText()); // comunica al server nome stanza
                                flag = client.receiveBooleanFromServer();
                                if(flag){
                                    controller.getTfRoom().setText("");
                                    controller.getLabelRoom().setText("Too late! Someone else has just created a room with this name! Please input another one:");
                                }
                                else{
                                    switchToLogin(stage);
                                }
                            } catch (IOException | ClassNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                    });
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
        controller.setStreams(client);
        VBox vboxNick = controller.getVboxNick();
        adjustLayout(vboxNick, screenHeight, screenWidth, 768, 1366);
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
                    if(!controller.getWaiting()) {
                        switchToWaitingStart(stage);
                    }
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
        controller.setStreams(client);
        VBox vboxNoPlayers = controller.getVboxNoPlayers();
        adjustLayout(vboxNoPlayers, screenHeight, screenWidth, 768, 1366);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Button requestButton = controller.getRequestButton();
        requestButton.setOnAction(event -> {
            int numberPlayers = controller.getNumberPlayers();
            try {
                if (numberPlayers >= 2 && numberPlayers <= 4) {
                    if(controller.getWaiting()){
                        switchToWaitingStart(stage);
                    }
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
        controller.setStreams(client);
        VBox vboxSecretObjs = controller.getVboxSecretObjs();
        adjustLayout(vboxSecretObjs, screenHeight, screenWidth, 768, 1366);
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
        controller.setStreams(client);
        VBox vboxStartCard = controller.getVboxStartCard();
        adjustLayout(vboxStartCard, screenHeight, screenWidth, 768, 1366);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
      // for testing  StarterCard card = new StarterCard(86, new FlatRule(0), new Corner[4], new Corner[4], Resource.BLANK, null);
        controller.addStarterImages();
        ImageView frontStarterCard = controller.getFrontStarterCard();
        frontStarterCard.setOnMouseClicked(mouseEvent -> {
            try {
                controller.sendIfStarterFlipped(false);
                showUpdatedPlayerGround(stage);
            } catch (IOException | ClassNotFoundException e) {
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

    public void showUpdatedPlayerGround(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/playground.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        boolean flag;
        flag = controller.addGround();
        if(flag){ // se è il suo turno
            if(!controller.checkIfLast()){
                switchToYourTurn(stage);
            }
            else{ // ultimo turno
                switchToLastTurn(stage);
            }
        }
        else{
            // wait for turn to do
        }
    }

    public void switchToYourTurn(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/yourTurn.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        Stage stage2 = new Stage();
        stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
        stage2.initOwner(stage);
        Scene scene = new Scene(root, 600, 200);
        stage2.setScene(scene);
        stage2.showAndWait();
        controller.getYourTurnButton().setOnAction(e ->{
            stage2.close();
            try {
                if(controller.playCard()){
                    if(controller.ifDrawable()){
                        switchToDraw(stage);
                    }
                }
                else{
                    showTwentyPoints(stage);
                }
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void switchToDraw(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/drawpanel.fxml"));
        Parent root = loader.load();
        Stage stage2 = new Stage();
        stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
        stage2.initOwner(stage);
        Scene scene = new Scene(root);
        stage2.setScene(scene);
        Controller controller = loader.getController();
        controller.setStreams(client);
        stage2.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            try {
                if (!controller.yourTurnDraw()) {
                    event.consume(); // Previene la chiusura della finestra in teoria
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        if(!controller.checkIfOver()){
            showUpdatedPlayerGround(stage);
        }
        else{
            showZeroCards(stage);
        }
    }

    public void switchToWaitingStart(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/waitingStart.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        VBox vboxWaitStart = controller.getVboxWaitStart();
        adjustLayout(vboxWaitStart, screenHeight, screenWidth, 768, 1366);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        if(controller.getWaiting()){
            switchToSelectSecretObj(stage);
        }
    }

    public void switchToWaitingFinish(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/waitingFinish.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        VBox vboxWaitStart = controller.getVboxWaitStart();
        adjustLayout(vboxWaitStart, screenHeight, screenWidth, 768, 1366);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ArrayList<Player> winners = controller.getWinners();
            if(winners.size()==1){
                showSingleWinner(stage, winners);
            }
            else{
                showWinners(stage, winners);
            }
    }

    public void showWinners(Stage stage, ArrayList<Player> winners) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/winners.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        VBox vboxWinners = controller.getVboxWinners();
        adjustLayout(vboxWinners, screenHeight, screenWidth, 720, 1280);
        String x = "";
        for(Player p: winners){
            if(winners.getLast().getNickname().equals(p.getNickname())){
                x = x.concat(p.getNickname()+" ");
            }
            x = x.concat(p.getNickname()+", ");
        }
        controller.getWinnerName().setText("The winners are "+x + "!");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void showSingleWinner(Stage stage, ArrayList<Player> winners) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/winner.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        VBox vboxWinner = controller.getVboxWinner();
        adjustLayout(vboxWinner, screenHeight, screenWidth, 720, 1280);
        controller.getWinnerName().setText("The winner is "+winners.getFirst().getNickname()+" !");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    // messaggio che annuncia il raggiungimento di 20 punti -> poi va a schermata waiting
    public void showTwentyPoints(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/20points.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        Stage stage2 = new Stage();
        stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
        stage2.initOwner(stage);
        Scene scene = new Scene(root);
        stage2.setScene(scene);
        controller.getFinishButton().setOnAction(e ->{
            try {
                switchToWaitingFinish(stage);
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    public void showZeroCards(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/zeroCards.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        Stage stage2 = new Stage();
        stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
        stage2.initOwner(stage);
        Scene scene = new Scene(root);
        stage2.setScene(scene);
        controller.getFinishButton2().setOnAction(e ->{
            try {
                switchToWaitingFinish(stage);
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void switchToLastTurn(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lastTurn.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        Stage stage2 = new Stage();
        stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
        stage2.initOwner(stage);
        Scene scene = new Scene(root);
        stage2.setScene(scene);
        stage2.showAndWait();
        try {
            if(controller.playCard()){
                if(controller.ifDrawable()){
                    switchToWaitingFinish(stage);
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void startGUI(){
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

