package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.CS.ServerRMIInterface;
import it.polimi.ingsw.Controller.PlayerController;
import it.polimi.ingsw.Model.*;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Set;

public class GUIrmi extends Application {

    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();
    private ImageView background;

    private ServerRMIInterface server;

    private PlayerController playerController;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            server = (ServerRMIInterface) Naming.lookup("rmi://localhost/ServerRMI");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/loadingScene.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getBackgroundIV().fitWidthProperty().bind(controller.getHboxStart().widthProperty());
        controller.getBackgroundIV().fitHeightProperty().bind(controller.getHboxStart().heightProperty());
        background = controller.getBackgroundIV();
        stage.setTitle("Codex Naturalis");
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(false);
        stage.show();
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
        controller.getAnchor2().getChildren().addFirst(background);
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
        ArrayList<Room> rooms = server.getRooms().getRooms();
        for (Room r : rooms) {
            Label elem = new Label(r.getName());
            controller.getMenu().getItems().add(elem);
        }
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
                                String roomName = controller.getTfRoom().getText();
                                if (!server.addRoom(roomName)) {
                                    controller.getTfRoom().setText("");
                                    controller.getLabelRoom().setText("Too late! Someone else has just created a room with this name! Please input another one:");
                                } else {
                                    switchToLogin(stage, roomName);
                                }
                            }
                        } catch (IOException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (newValue.equals(buttonL)) {
                try {
                    controller.addRoomsMenu();
                    controller.getConfirmRoom().setOnAction(e2 -> {
                        try {
                            Room roomJoined = server.getRooms().getRoom(controller.getMenu().getSelectionModel().getSelectedItem().getText());
                            if (roomJoined.isFull()) {
                                controller.getTfRoom().setText("");
                                controller.getLabelRoom().setText("Too late! The room is full!");
                            } else {
                                switchToLogin(stage, roomJoined.getName());
                            }
                        } catch (IOException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void switchToLogin(Stage stage, String roomName) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insertNick.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getNickAnchor().getChildren().addFirst(background);
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
        Button nickButton = controller.getNickButton();
        nickButton.setOnAction(actionEvent -> {
            try {

                if (server.addNewPlayer(controller.getNickname(), roomName) == null) {
                    controller.getNickLabel().setPrefWidth(800);    // eventualmente aggiungere un'altra label
                    controller.getNickLabel().setStyle("-fx-text-fill: #b20b0b");
                    controller.getNickLabel().setText("This name is already taken, choose another one:");
                    controller.getNickTextField().setText("");
                } else {
                    if (server.getRooms().getRoom(roomName).getGame().getNumPlayer() == 1) {
                        switchToNumberPlayers(stage, roomName);
                    } else {
                        // switchToColorChoice(stage);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /*  public void switchToColorChoice(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/colorChoice.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        controller.getAnchor10().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        String[] choice = new String[1];
        String[] choiceCurr = new String[1];
        choiceCurr[0] = "";
        Set<String> colors = client.receiveColorsFromServer();
        showColor(colors, controller);
        controller.getRed().getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                controller.getConfirmColor().setVisible(true);
                controller.getConfirmColor().setDisable(false);
                if (newValue.equals(controller.getRed())) {
                    choice[0] = "red";
                }
                if (newValue.equals(controller.getBlue())) {
                    choice[0] = "blue";
                }
                if (newValue.equals(controller.getGreen())) {
                    choice[0] = "green";
                }
                if (newValue.equals(controller.getYellow())) {
                    choice[0] = "yellow";
                }
                boolean[] flag = {false};
                controller.getConfirmColor().setOnAction(e->{
                    try {
                        client.sendToServer(choice[0]);
                        if (!client.receiveBooleanFromServer()) {
                            Set<String> colors2 = client.receiveColorsFromServer();
                            showColor(colors2, controller);
                            controller.getColorValidLabel().setVisible(true);
                        } else {
                            flag[0] = true;
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(flag[0]) {
                        try {
                         //   switchToWaitingStart(stage);
                        } catch (IOException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        });
    }

    public void showColor(Set<String> colors, Controller controller){
        for (String s : colors) {
            if (s.equals("red")) {
                controller.getRed().setDisable(false);
                controller.getRed().setVisible(true);
            }
            if (s.equals("blue")) {
                controller.getBlue().setDisable(false);
                controller.getBlue().setVisible(true);
            }
            if (s.equals("green")) {
                controller.getGreen().setDisable(false);
                controller.getGreen().setVisible(true);
            }
            if (s.equals("yellow")) {
                controller.getYellow().setDisable(false);
                controller.getYellow().setVisible(true);
            }
        }
    }*/

    public void switchToNumberPlayers(Stage stage, String roomName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/requestNoPlayers.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getAnchor3().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Button requestButton = controller.getRequestButton();
        requestButton.setOnAction(event -> {
            try {
                server.getRooms().getRoom(roomName).getGame().setExpPlayers(controller.getNumberPlayers());
                // switchToColorChoice(stage);  to fix for rmi
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

  /*  public void switchToStarterChoice(Stage stage, Player player) throws IOException, ClassNotFoundException, InvalidPositionException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startingCardChoice.fxml"));
        playerController = new PlayerController(player.getPlayerGround(), player.getHand());
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getAnchor6().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        StarterCard startCard = playerController.pickCard(player.getGame());
        controller.addStarterImages(startCard);
        ImageView frontStarterCard = controller.getFrontStarterCard();
        frontStarterCard.setOnMouseClicked(mouseEvent -> {
            try {
                playerController.setFirstCard(startCard);
            } catch (InvalidPositionException e) {
                throw new RuntimeException(e);
            }
        });
        ImageView backStarterCard = controller.getBackStarterCard();
        backStarterCard.setOnMouseClicked(mouseEvent -> {
            startCard.flipCard();
            try {
                playerController.setFirstCard(startCard);
            } catch (InvalidPositionException e) {
                throw new RuntimeException(e);
            }
        });
    }*/

  /*  public void switchToSelectSecretObj(Stage stage, StarterCard starterCard, Player player) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/selectSecretObjs.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getAnchor7().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ObjectiveCard[] obj = playerController.pickObjCard(player.getGame());
        controller.addSecretObjImages(obj);
        ImageView secretObjLeft = controller.getSecretObjLeft();
        secretObjLeft.setOnMouseClicked(mouseEvent -> {
            playerController.setObjSecret(obj[0]);
            //initializePlayground(stage);
        });
        ImageView secretObjRight = controller.getSecretObjRight();
        secretObjRight.setOnMouseClicked(mouseEvent -> {
            playerController.setObjSecret(obj[1]);
            //initializePlayground(stage);
        });
    }

    public static void startGUI() {
        launch();
    }
}*/


  /*    public void switchToColorChoice(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/colorChoice.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        controller.getAnchor3().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        String[] choice = new String[1];
        String[] choiceCurr = new String[1];
        choiceCurr[0] = "";
        controller.getRed().getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                controller.getConfirmColor().setVisible(true);
                controller.getConfirmColor().setDisable(false);
                if (newValue.equals(controller.getRed())) {
                    choice[0] = "red";
                }
                if (newValue.equals(controller.getBlue())) {
                    choice[0] = "blue";
                }
                if (newValue.equals(controller.getGreen())) {
                    choice[0] = "green";
                }
                if (newValue.equals(controller.getYellow())) {
                    choice[0] = "yellow";
                }
                boolean[] flag = new boolean[1];
                controller.getConfirmColor().setOnAction(e->{
                    while(!flag[0]){
                        try {
                            if(!choice[0].equals(choiceCurr[0])){
                                client.sendToServer(choice[0]);
                                if(client.receiveBooleanFromServer()){
                                    controller.getColorValidLabel().setVisible(true);
                                    choiceCurr[0] = choice[0];
                                }
                                else{
                                    flag[0] = true;
                                }
                            }
                        } catch (IOException | ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    try {
                        switchToWaitingStart(stage);
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        });
  }


    public void initializePlayerGround(Stage stage, StarterCard sc) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/playground.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getPlayGroundHBox().getChildren().addFirst(background);
        controller.setStreams(client);
        Game g = client.receiveGameFromServer();
        Player p = client.receivePlayerFromServer();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        controller.placeFirstCard(sc, p);
        showUpdatedPlayerGround(stage, g, p, controller);
    }

    public void showUpdatedPlayerGround(Stage stage, Game g, Player p, Controller controller) throws IOException, ClassNotFoundException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/playground.fxml"));
//        Parent root = loader.load();
//        Controller controller = loader.getController();
        controller.getPlayGroundAnchor().getChildren().addFirst(background);
        controller.setStreams(client);
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
        stage.show();
        boolean flag;
        flag = controller.addGround(g, p);
        if(flag){ // se è il suo turno
            if(!client.receiveBooleanFromServer()){
                switchToYourTurn(stage);
            }
            else{ // ultimo turno
                switchToLastTurn(stage);
            }
        }
        else{
            // ...
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
                    if(client.receiveBooleanFromServer()){
//                        showUpdatedPlayerGround(stage);
                        System.out.println("Card placed");
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
            // showUpdatedPlayerGround(stage);
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
        controller.getAnchor4().getChildren().addFirst(background);
        Player player = client.receivePlayerFromServer();
        controller.getStartLabel().setText("Please "+player.getNickname()+", wait for other players to join.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Task<Integer> task = new Task<>(){
            @Override
            protected Integer call() throws Exception {
                if(!client.receiveBooleanFromServer()){
                    if(client.receiveBooleanFromServer()){
                     //   controller.getStartLabel().setText("Every player has logged in!");
                     //   controller.getStartLabel2().setText("Press the button to continue.");
                        controller.getWaitingBar().setVisible(false);
                        controller.getButtonStart().setDisable(false);
                        controller.getButtonStart().setVisible(true);
                    }
                }
                return null;
            }
        };
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
        controller.getButtonStart().setOnAction(e->{
            try {
                switchToStarterChoice(stage);
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    public void switchToWaitingFinish(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/waitingFinish.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        controller.getAnchor5().getChildren().addFirst(background);
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
        controller.getAnchor9().getChildren().addFirst(background);
        String x = "";
        for(Player p: winners){
            if(winners.getLast().getNickname().equals(p.getNickname())) {
                x = x.concat(p.getNickname());
            }
            else{
                x = x.concat(p.getNickname() + ", ");
            }
        }
        controller.getWinnersNames().setText("The winners are "+x + "!");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void showSingleWinner(Stage stage, ArrayList<Player> winners) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/winner.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        controller.getAnchor8().getChildren().addFirst(background);
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
        // pulsing label animation
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(500), controller.getLabel20p());
        scaleUp.setToX(1.2);
        scaleUp.setToY(1.2);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(500), controller.getLabel20p());
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        SequentialTransition pulse = new SequentialTransition(scaleUp, scaleDown);
        pulse.setCycleCount(SequentialTransition.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();
        stage2.showAndWait();
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
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            double xOffset = (Math.random() - 0.5) * 10;
            double yOffset = (Math.random() - 0.5) * 10;
            double xOffset2 = (Math.random() - 0.5) * 10;
            double yOffset2 = (Math.random() - 0.5) * 10;
            controller.getSadFace().setTranslateX(xOffset);
            controller.getSadFace().setTranslateY(yOffset);
            controller.getSadFace2().setTranslateX(xOffset2);
            controller.getSadFace2().setTranslateY(yOffset2);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Make the timeline run indefinitely
        timeline.play();
        stage2.showAndWait();
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
                if(client.receiveBooleanFromServer()){
                    switchToWaitingFinish(stage);
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
  */