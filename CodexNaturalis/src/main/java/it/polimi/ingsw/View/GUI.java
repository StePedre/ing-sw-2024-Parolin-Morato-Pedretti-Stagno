package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.Model.*;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.animation.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class GUI extends Application{
    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();
    private GUIClientSocket client;
    private ImageView background;

    @Override
    public void start(Stage stage) throws IOException {
        Socket socket = new Socket("127.0.0.1", 59090);//modificare socket con inserimento ip server
        client = new GUIClientSocket(socket.getInputStream(),socket.getOutputStream());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/loadingScene.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
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
        controller.setStreams(client);
        controller.getAnchor2().getChildren().addFirst(background);
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
        ArrayList<Room> rooms = client.receiveRoomsFromServer();
        for (Room r: rooms){
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
                    controller.addRoomsMenu();
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
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void switchToLogin(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insertNick.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStreams(client);
        controller.getNickAnchor().getChildren().addFirst(background);
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
        Button nickButton = controller.getNickButton();
        nickButton.setOnAction(actionEvent -> {
            try {
                client.sendToServer(controller.getNickname());
                if(client.receiveBooleanFromServer()){
                    controller.getNickLabel().setPrefWidth(800);    // eventualmente aggiungere un'altra label
                    controller.getNickLabel().setStyle("-fx-text-fill: #b20b0b");
                    controller.getNickLabel().setText("This name is already taken, choose another one:");
                    controller.getNickTextField().setText("");
                }
                else{
                    if(client.receiveBooleanFromServer()){
                        switchToNumberPlayers(stage);
                    }
                    else{
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
        controller.getAnchor3().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        final boolean[] flag = {false};
        Button requestButton = controller.getRequestButton();
        requestButton.setOnAction(event -> {
            try {
                flag[0] = controller.getNumberPlayers();
                if (flag[0]) {
                    switchToWaitingStart(stage);
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
        controller.getAnchor7().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        controller.addSecretObjImages();
        ImageView secretObjLeft = controller.getSecretObjLeft();
        secretObjLeft.setOnMouseClicked(mouseEvent -> {
            try {
                client.sendToServer((int)1);
                switchToStarterChoice(stage);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        ImageView secretObjRight = controller.getSecretObjRight();
        secretObjRight.setOnMouseClicked(mouseEvent -> {
            try {
                client.sendToServer((int)1);
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
        controller.getAnchor6().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        controller.addStarterImages();
        ImageView frontStarterCard = controller.getFrontStarterCard();
        frontStarterCard.setOnMouseClicked(mouseEvent -> {
            try {
                controller.sendIfStarterFlipped(false);
               // showUpdatedPlayerGround(stage);
                simulateEnd(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ImageView backStarterCard = controller.getBackStarterCard();
        backStarterCard.setOnMouseClicked(mouseEvent -> {
            try {
                controller.sendIfStarterFlipped(true);
               // showUpdatedPlayerGround(stage);
                 simulateEnd(stage);
            } catch (IOException e) {
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
     //   stage.show();
        boolean flag;
        flag = controller.addGround();
        if(flag){ // se è il suo turno
            if(!client.receiveBooleanFromServer()){
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
                    if(client.receiveBooleanFromServer()){
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
        controller.getAnchor4().getChildren().addFirst(background);
        Player player = client.receivePlayerFromServer();
        controller.getStartLabel().setText("Please "+player.getNickname()+", wait for other players to join.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Task<Integer> task = new Task<Integer>(){
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
                switchToSelectSecretObj(stage);
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
public static void startGUI(){
        launch();
    }
public void simulateEnd(Stage stage) throws IOException {
    boolean flag = true;
    if(flag){ // se è il suo turno
        if(!flag){
            switchToYourTurn(stage);
        }
        else{ // ultimo turno
            showTwentyPoints(stage);
          //  showZeroCards(stage);
        }
    }
    ArrayList<Player> winners = new ArrayList<>();
    winners.add(new Player("silvia"));
  //  showSingleWinner(stage, winners);
    winners.add(new Player("matteo"));
    showWinners(stage, winners);
}

}

