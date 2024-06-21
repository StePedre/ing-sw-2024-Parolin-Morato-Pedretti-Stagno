package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.Room;
import it.polimi.ingsw.CS.ServerRMIInterface;
import it.polimi.ingsw.Model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

import static javafx.application.Application.launch;

public class GUIrmi extends Application {

    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();
    private ImageView background,ivl,ivc,ivr;

    String roomName = "";

    String nickname = "";

    private ServerRMIInterface guiServerRMI;


    @Override
    public void start(Stage stage) throws Exception {
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
        stage.show();
        scene.setOnKeyReleased(keyEvent -> {
            try {
                switchToIpInput(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void switchToIpInput(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ipInputScene.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getAnchor10().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        controller.getIpButton().setOnAction(e -> {
            try {
                String ip = controller.getIpField().getText();
                try {
                    guiServerRMI = (ServerRMIInterface) Naming.lookup("rmi://" + ip + "/ServerRMI");
                    System.out.println("Connected to RMI server.");
                } catch (Exception f) {
                    System.err.println("Client exception: " + e.toString());
                }
                switchToRoomChoice(stage);
            } catch (IOException | ClassNotFoundException ex) {
                try {
                    ex.printStackTrace();
                    switchToIpInput(stage);   // dovrebbe essere sostituito con valid label e permesso di reinserire l'input
                } catch (IOException exc) {
                   /* try {
                        showError(stage);
                    } catch (IOException exce) {
                        throw new RuntimeException(exce);
                    }
                    throw new RuntimeException(exc);*/
                }
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
        ArrayList<Room> rooms = guiServerRMI.getRooms().getRooms();
        for (Room r : rooms) {
            Label elem = new Label(r.getName());
            elem.setFont(Font.font("Bookman Old Style"));
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
                    controller.addRoomCreationInput();
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
                                roomName = controller.getTfRoom().getText();
                                if (!guiServerRMI.addRoom(roomName)) {    // chiedere se ok al server
                                    controller.getTfRoom().setText("");
                                    controller.getLabelRoom().setText("Too late! Someone else has just created a room with this name! Please input another one:");
                                } else {
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
            } else if (newValue.equals(buttonL)) {
                try {
                    controller.addRoomsMenu();
                    controller.getConfirmRoom().setOnAction(e2 -> {
                        try {
                            roomName = controller.getMenu().getSelectionModel().getSelectedItem().getText();
                            if (guiServerRMI.getRooms().getRoom(roomName).isFull()) {
                                controller.getTfRoom().setText("");
                                controller.getLabelRoom().setText("Too late! The room is full!");
                            } else {
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
        controller.getNickAnchor().getChildren().addFirst(background);
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
        Button nickButton = controller.getNickButton();
        nickButton.setOnAction(actionEvent -> {
            try {
                nickname = controller.getNickname();
                System.out.println(nickname);
                if (guiServerRMI.getRooms().alreadyInGame(roomName, nickname)) {
                    controller.getNickLabel().setPrefWidth(800);    // eventualmente aggiungere un'altra label
                    controller.getNickLabel().setStyle("-fx-text-fill: #b20b0b");
                    controller.getNickLabel().setText("This name is already taken, choose another one:");
                    controller.getNickTextField().setText("");
                } else {
                    if (guiServerRMI.isFirstPlayer(roomName)) {
                        switchToNumberPlayers(stage);
                    } else {
                        switchToColorChoice(stage);
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
        controller.getAnchor3().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Button requestButton = controller.getRequestButton();
        requestButton.setOnAction(event -> {
            try {
                guiServerRMI.setPlayerNumber(controller.getNumberPlayers(), roomName);
                switchToColorChoice(stage);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void switchToColorChoice(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/colorChoice.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getAnchor10().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        String[] choice = new String[1];
        String[] choiceCurr = new String[1];
        choiceCurr[0] = "";
        Glow highlight = new Glow(0.5);
        Set<String> colors = guiServerRMI.getRemainingColors(roomName);
        showColor(colors, controller);
        controller.getRed().getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null || newValue==oldValue) {
                if(oldValue!=null){
                    ((ToggleButton)oldValue).setEffect(null);
                }
                controller.getConfirmColor().setVisible(true);
                controller.getConfirmColor().setDisable(false);
                if (newValue.equals(controller.getRed())) {
                    ((ToggleButton)newValue).setEffect(highlight);
                    choice[0] = "red";
                }
                if (newValue.equals(controller.getBlue())) {
                    ((ToggleButton)newValue).setEffect(highlight);
                    choice[0] = "blue";
                }
                if (newValue.equals(controller.getGreen())) {
                    ((ToggleButton)newValue).setEffect(highlight);
                    choice[0] = "green";
                }
                if (newValue.equals(controller.getYellow())) {
                    ((ToggleButton)newValue).setEffect(highlight);
                    choice[0] = "yellow";
                }
                boolean[] flag = {false};
                controller.getConfirmColor().setOnAction(e->{
                    try {
                        guiServerRMI.addNewPlayer(nickname,roomName);
                        if (guiServerRMI.setPlayerColor(choice[0], nickname, roomName)) {
                            Set<String> colors2 = guiServerRMI.getRemainingColors(roomName);
                            showColor(colors2, controller);
                            controller.getColorValidLabel().setVisible(true);
                        } else {
                            flag[0] = true;
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(flag[0]) {
                        try {
                            guiServerRMI.addPlayerToRoundController(nickname,roomName);
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        switchToWaitingStart(stage);
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
    }

    public void switchToStarterChoice(Stage stage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/startingCardChoice.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.getAnchor6().getChildren().addFirst(background);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            StarterCard card = guiServerRMI.getFirstCard(roomName);
            controller.addStarterImages(card);
            stage.show();
            ImageView frontStarterCard = controller.getFrontStarterCard();
            frontStarterCard.setOnMouseClicked(mouseEvent -> {
                try {
                    guiServerRMI.setFirstCard(card, nickname, roomName);
                    switchToSelectSecretObj(stage);
                    //simulateEnd(stage);
                } catch (IOException | InvalidPositionException e) {
                    showError(stage);
                }
            });
            ImageView backStarterCard = controller.getBackStarterCard();
            backStarterCard.setOnMouseClicked(mouseEvent -> {
                try {
                    card.flipCard();
                    guiServerRMI.setFirstCard(card, nickname, roomName);
                    switchToSelectSecretObj(stage);
                    //simulateEnd(stage);
                } catch (IOException e) {
                    showError(stage);
                } catch (InvalidPositionException e) {
                    throw new RuntimeException(e);
                }
            });
        }catch (IOException e){
            showError(stage);
        }
    }

    public void switchToSelectSecretObj(Stage stage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/selectSecretObjs.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.getAnchor7().getChildren().addFirst(background);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            ObjectiveCard[] objs = guiServerRMI.getObjCards(roomName);
            controller.addSecretObjImages(objs);
            ImageView secretObjLeft = controller.getSecretObjLeft();
            secretObjLeft.setOnMouseClicked(mouseEvent -> {
                try {
                    guiServerRMI.setObjSecret(objs[0], nickname, roomName);
                    initializePlayerGround(stage);
                } catch (IOException | ClassNotFoundException e) {
                    showError(stage);
                }
            });
            ImageView secretObjRight = controller.getSecretObjRight();
            secretObjRight.setOnMouseClicked(mouseEvent -> {
                try {
                    guiServerRMI.setObjSecret(objs[1], nickname, roomName);
                    initializePlayerGround(stage);
                } catch (IOException | ClassNotFoundException e) {
                    showError(stage);
                }
            });
        }
        catch (IOException e){
            showError(stage);
        }
    }

    public void initializePlayerGround(Stage stage)throws ClassNotFoundException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/playground.fxml"));
            System.out.println("Help");
            Parent root = loader.load();
            Controller controller = loader.getController();
            Game g = guiServerRMI.getRooms().getRoom(roomName).getGame();
            Player p = g.getPlayer(nickname);
            controller.addNames(g, p);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            controller.placeFirstCard(p);

            if (guiServerRMI.isCurrentPlayer(roomName,nickname)) {
                yourTurn(stage, controller);
            } else {
                notYourTurn(stage, g, p, controller);
            }
        }
        catch (IOException | RuntimeException e){
            e.printStackTrace();
            showError(stage);
        }


    }


    public void yourTurn(Stage stage, Controller controller){
        try {
            Game[] game = new Game[1];
            Player[] player = new Player[1];
            game[0] = guiServerRMI.getRooms().getRoom(roomName).getGame();
            player[0] = game[0].getPlayer(nickname);
            controller.showAvailablePos(player[0].getPlayerGround().getAvailablePositions());
            controller.addGround(game[0], player[0]);
            for (ImageView zone : controller.getImageViewList()) {
                controller.setDropZones(zone);
            }
            setLinkToPlayerGrounds(game[0], controller, stage);
            if (!guiServerRMI.isGameOver(roomName)) {    // if not last turn
                showYourTurn(stage);
            } else { // ultimo turno
                showLastTurn(stage);
            }
            controller.getFlipButton().setOnAction(e -> {
                controller.flipCard(player[0].getHand());
            });
            ivl = controller.getHandCardLeft();
            ivc = controller.getHandCardCenter();
            ivr = controller.getHandCardRight();
            ivl.setOnDragDetected(event -> {
                if (player[0].getPlayerGround().checkRequirements(controller.getCurrentHandLeft()) || controller.getCurrentHandLeft().getFlip()) {
                    Dragboard db = ivl.startDragAndDrop(TransferMode.MOVE);
                    Image dragMiniature = new Image(ivl.getImage().getUrl(), 150, 100, true, true);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(dragMiniature);
                    db.setContent(content);
                    event.consume();
                    controller.setCardPlayed(controller.getCurrentHandLeft());
                }
            });
            ivc.setOnDragDetected(event -> {
                if (player[0].getPlayerGround().checkRequirements(controller.getCurrentHandCenter()) || controller.getCurrentHandCenter().getFlip()) {
                    Dragboard db = ivc.startDragAndDrop(TransferMode.MOVE);
                    Image dragMiniature = new Image(ivc.getImage().getUrl(), 150, 100, true, true);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(dragMiniature);
                    db.setContent(content);
                    event.consume();
                    controller.setCardPlayed(controller.getCurrentHandCenter());
                }
            });
            ivr.setOnDragDetected(event -> {
                if (player[0].getPlayerGround().checkRequirements(controller.getCurrentHandRight()) || controller.getCurrentHandRight().getFlip()) {
                    Dragboard db = ivr.startDragAndDrop(TransferMode.MOVE);
                    Image dragMiniature = new Image(ivr.getImage().getUrl(), 150, 100, true, true);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(dragMiniature);
                    db.setContent(content);
                    event.consume();
                    controller.setCardPlayed(controller.getCurrentHandRight());
                }
            });
            ivl.setOnDragDone(event -> {
                try {
                    if (controller.getPosPlayed() != null) {
                        guiServerRMI.placeCard(controller.getCardPlayed(),controller.reconvertPosition(controller.getPosPlayed()),roomName, nickname);
                        controller.updateAfterPlay(guiServerRMI.getRooms().getRoom(roomName).getGame().getPlayer(nickname));
                        controller.setPosPlayedNull();// works
                        if (guiServerRMI.isLastTurn(roomName)) {
                            switchToWaitingFinish(stage);
                        } else {
                            if (!guiServerRMI.isDeckEmpty(roomName)) {
                                event.consume();
                                switchToDraw(stage, controller);
                            } else {
                                event.consume();
                                showZeroCards(stage);
                                notYourTurn(stage, game[0], player[0], controller);
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException | MissingResourcesException | InvalidPositionException e) {
                    showError(stage);
                }
            });
            ivc.setOnDragDone(event -> {
                try {
                    if (controller.getPosPlayed() != null) {
                        guiServerRMI.placeCard(controller.getCardPlayed(),controller.reconvertPosition(controller.getPosPlayed()),roomName, nickname);
                        controller.updateAfterPlay(guiServerRMI.getRooms().getRoom(roomName).getGame().getPlayer(nickname));
                        controller.setPosPlayedNull();
                        if (guiServerRMI.isLastTurn(roomName)) {
                            switchToWaitingFinish(stage);
                        } else {
                            if (!guiServerRMI.isDeckEmpty(roomName)) {
                                event.consume();
                                switchToDraw(stage, controller);
                            } else {
                                event.consume();
                                showZeroCards(stage);
                                notYourTurn(stage, game[0], player[0], controller);
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    showError(stage);
                } catch (MissingResourcesException | InvalidPositionException e) {
                    throw new RuntimeException(e);
                }
            });
            ivr.setOnDragDone(event -> {
                try {
                    if (controller.getPosPlayed() != null) {
                        guiServerRMI.placeCard(controller.getCardPlayed(),controller.reconvertPosition(controller.getPosPlayed()),roomName, nickname);
                        controller.updateAfterPlay(guiServerRMI.getRooms().getRoom(roomName).getGame().getPlayer(nickname));
                        controller.setPosPlayedNull();
                        if (guiServerRMI.isLastTurn(roomName)) {
                            switchToWaitingFinish(stage);
                        } else {
                            if (guiServerRMI.isDeckEmpty(roomName)) {
                                event.consume();
                                switchToDraw(stage, controller);
                            } else {
                                event.consume();
                                showZeroCards(stage);
                                notYourTurn(stage, game[0], player[0], controller);
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    showError(stage);
                } catch (MissingResourcesException | InvalidPositionException e) {
                    throw new RuntimeException(e);
                }
            });
        }catch (IOException e) {
            showError(stage);
        }
    }


    public void setLinkToPlayerGrounds(Game g, Controller controller, Stage s) {
        if (controller.getLabelPoints2().isVisible()) {
            controller.getLabelPoints2().setOnMouseClicked(e->{
                try {
                    showGround(g.getPlayer(controller.getNick2()), s);
                } catch (IOException ex) {
                    showError(s);
                }
            });
        }
        if (controller.getLabelPoints3().isVisible()) {
            controller.getLabelPoints3().setOnMouseClicked(e->{
                try {
                    showGround(g.getPlayer(controller.getNick3()), s);
                } catch (IOException ex) {
                    showError(s);
                }
            });
        }
        if (controller.getLabelPoints4().isVisible()) {
            controller.getLabelPoints4().setOnMouseClicked(e->{
                try {
                    showGround(g.getPlayer(controller.getNick4()), s);
                } catch (IOException ex) {
                    showError(s);
                }
            });
        }
    }


    public void notYourTurn(Stage stage, Game g, Player p, Controller controller){ //problemi con il msg di errore
        try {
            controller.removeAvailablePos();
            controller.addGround(g, p);
            showNotYourTurn(stage);
            Task<Integer> task = new Task<>() {
                @Override
                protected Integer call() throws Exception {
                    int i = 0;
                    System.out.println("not your turn");
                    while(!guiServerRMI.isCurrentPlayer(roomName, nickname)){
                        i++;
                    }
                    return null;
                }
            };
            Thread t = new Thread(task);
            t.setDaemon(true);
            t.start();
            task.setOnSucceeded(event -> {
                yourTurn(stage, controller);
            });
            task.setOnFailed(event ->{
                showError(stage);
            });
        }catch (IOException e){
            showError(stage);
        }
    }

    public void showError(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/errorMessageBanner.fxml"));
            Parent root = loader.load();
            Stage stage2 = new Stage();
            stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
            stage2.initOwner(stage);
            Scene scene = new Scene(root, 600, 200);
            stage2.setScene(scene);
            stage2.showAndWait();
            stage.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void showYourTurn(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/yourTurn.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Stage stage2 = new Stage();
        stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
        stage2.initOwner(stage);
        Scene scene = new Scene(root, 600, 200);
        stage2.setScene(scene);
        stage2.showAndWait();
        controller.getYourTurnButton().setOnAction(e -> {
            stage2.close();
        });
    }


    public void showNotYourTurn(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/notYourTurn.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Stage stage2 = new Stage();
        stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
        stage2.initOwner(stage);
        Scene scene = new Scene(root, 600, 200);
        stage2.setScene(scene);
        stage2.showAndWait();
        controller.getNotYourTurnButton().setOnAction(e -> {
            stage2.close();
        });
    }


    public void switchToDraw(Stage stage, Controller playgroundController){
        try {
            playgroundController.removePlayedPos();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/drawpanel.fxml"));
            Parent root = loader.load();
            Stage stage2 = new Stage();
            stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
            stage2.initOwner(stage);
            Scene scene = new Scene(root);
            stage2.setScene(scene);
            Controller controller = loader.getController();
            Game game = guiServerRMI.getRooms().getRoom(roomName).getGame();
            controller.addCards(game.getDecks());
            stage2.show();
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(5);
            InnerShadow innerShadow = new InnerShadow();
            innerShadow.setBlurType(BlurType.THREE_PASS_BOX);
            innerShadow.setChoke(0.1);
            innerShadow.setWidth(30.0);
            innerShadow.setHeight(30.0);
            innerShadow.setRadius(14.5);
            dropShadow.setColor(Color.BLUE);
            controller.getResUp1().setOnMouseEntered(e -> controller.getResUp1().setEffect(dropShadow));
            controller.getResUp2().setOnMouseEntered(e -> controller.getResUp2().setEffect(dropShadow));
            controller.getResDeck().setOnMouseEntered(e -> controller.getResDeck().setEffect(dropShadow));
            controller.getGoldUp1().setOnMouseEntered(e -> controller.getGoldUp1().setEffect(dropShadow));
            controller.getGoldUp2().setOnMouseEntered(e -> controller.getGoldUp2().setEffect(dropShadow));
            controller.getGoldDeck().setOnMouseEntered(e -> controller.getGoldDeck().setEffect(dropShadow));
            controller.getResUp1().setOnMouseExited(e -> controller.getResUp1().setEffect(innerShadow));
            controller.getResUp2().setOnMouseExited(e -> controller.getResUp2().setEffect(innerShadow));
            controller.getResDeck().setOnMouseExited(e -> controller.getResDeck().setEffect(innerShadow));
            controller.getGoldUp1().setOnMouseExited(e -> controller.getGoldUp1().setEffect(innerShadow));
            controller.getGoldUp2().setOnMouseExited(e -> controller.getGoldUp2().setEffect(innerShadow));
            controller.getGoldDeck().setOnMouseExited(e -> controller.getGoldDeck().setEffect(innerShadow));

            controller.getResUp1().setOnMouseClicked(e -> {
                if (game.getDecks()[0].getCards().getFirst() != null) {
                    try {
                        guiServerRMI.drawCard(0,0,roomName, nickname);
                        stage2.close();
                        Game g = guiServerRMI.getRooms().getRoom(roomName).getGame();
                        Player p = g.getPlayer(nickname);
                        guiServerRMI.nextRound(roomName);
                        notYourTurn(stage, g, p, playgroundController);
                    } catch (IOException ex) {
                        showError(stage);
                    }
                }
                // else valid label?
            });
            controller.getResUp2().setOnMouseClicked(e -> {
                if (game.getDecks()[0].getCards().get(1) != null) {
                    try {
                        guiServerRMI.drawCard(0,1,roomName, nickname);
                        stage2.close();
                        Game g = guiServerRMI.getRooms().getRoom(roomName).getGame();
                        Player p = g.getPlayer(nickname);
                        guiServerRMI.nextRound(roomName);
                        notYourTurn(stage, g, p, playgroundController);
                    } catch (IOException ex) {
                        showError(stage);
                    }
                }
            });
            controller.getResDeck().setOnMouseClicked(e -> {
                if (game.getDecks()[0].getCards().get(2) != null) {
                    try {
                        guiServerRMI.drawCard(0,2,roomName, nickname);
                        stage2.close();
                        Game g = guiServerRMI.getRooms().getRoom(roomName).getGame();
                        Player p = g.getPlayer(nickname);
                        guiServerRMI.nextRound(roomName);
                        notYourTurn(stage, g, p, playgroundController);

                    } catch (IOException ex) {
                        showError(stage);
                    }
                }
            });
            controller.getGoldUp1().setOnMouseClicked(e -> {
                if (game.getDecks()[1].getCards().getFirst() != null) {
                    try {
                        guiServerRMI.drawCard(1,0,roomName, nickname);
                        stage2.close();
                        Game g = guiServerRMI.getRooms().getRoom(roomName).getGame();
                        Player p = g.getPlayer(nickname);
                        guiServerRMI.nextRound(roomName);
                        notYourTurn(stage, g, p, playgroundController);
                    } catch (IOException ex) {
                        showError(stage);
                    }
                }
            });
            controller.getGoldUp2().setOnMouseClicked(e -> {
                if (game.getDecks()[1].getCards().get(1) != null) {
                    try {
                        guiServerRMI.drawCard(1,1,roomName, nickname);
                        stage2.close();
                        Game g = guiServerRMI.getRooms().getRoom(roomName).getGame();
                        Player p = g.getPlayer(nickname);
                        guiServerRMI.nextRound(roomName);
                        notYourTurn(stage, g, p, playgroundController);
                    } catch (IOException ex) {
                        showError(stage);
                    }
                }
            });
            controller.getGoldDeck().setOnMouseClicked(e -> {
                if (game.getDecks()[1].getCards().get(2) != null) {
                    try {
                        guiServerRMI.drawCard(1,2,roomName, nickname);
                        stage2.close();
                        Game g = guiServerRMI.getRooms().getRoom(roomName).getGame();
                        Player p = g.getPlayer(nickname);
                        guiServerRMI.nextRound(roomName);
                        notYourTurn(stage, g, p, playgroundController);
                    } catch (IOException ex) {
                        showError(stage);
                    }
                }
            });
        }
        catch (IOException e){
            System.out.println("wellla");
            showError(stage);
        }}

    public void switchToWaitingStart(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/waitingStart.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.getAnchor4().getChildren().addFirst(background);
            Player player = guiServerRMI.getRooms().getRoom(roomName).getGame().getPlayer(nickname);
            controller.getStartLabel().setText("Please " + player.getNickname() + ", wait for other players to join.");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            Task<Integer> task = new Task<>() {
                @Override
                protected Integer call() throws Exception {
                    boolean waitingForPlayers = true;
                    while (waitingForPlayers) {
                        if(guiServerRMI.getRooms().getRoom(roomName).isFull()) {
                            waitingForPlayers = false;
                        }
                    }
                    return null;
                }
            };
            Thread t = new Thread(task);
            t.setDaemon(true);
            t.start();
            task.setOnSucceeded(event -> {
                switchToStarterChoice(stage);
            });
            task.setOnFailed(event ->{
                showError(stage);
            });
        }
        catch (IOException e){
            showError(stage);
        }
    }

    public void switchToWaitingFinish(Stage stage) throws ClassNotFoundException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/waitingFinish.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.getAnchor5().getChildren().addFirst(background);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            ArrayList<Player>[] winners = new ArrayList[1];
            Task<Integer> task = new Task<>(){
                @Override
                protected Integer call() throws Exception {
                    winners[0] = guiServerRMI.getMultiWinners(roomName);
                    return null;
                }
            };
            Thread t = new Thread(task);
            t.setDaemon(true);
            t.start();
            task.setOnSucceeded(event ->{
                if(winners[0].size()==1){
                    try {
                        showSingleWinner(stage, winners[0]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else{
                    try {
                        showWinners(stage, winners[0]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void showWinners(Stage stage, ArrayList<Player> winners) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/winners.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
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
        controller.getAnchor8().getChildren().addFirst(background);
        controller.getWinnerName().setText("The winner is "+winners.getFirst().getNickname()+" !");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /*animazione che pulsa
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
          */

    public void showZeroCards(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/zeroCards.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();
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
            controller.getFinishButton2().setOnAction(e -> {
                try {
                    switchToWaitingFinish(stage);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch(IOException exc){
            showError(stage);
        }
    }

    public void showLastTurn(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lastTurn.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Stage stage2 = new Stage();
        stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
        stage2.initOwner(stage);
        Scene scene = new Scene(root);
        stage2.setScene(scene);
        stage2.showAndWait();
    }
    public static void startGUI(){
        launch();
    }

    public void showGround(Player pg, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/otherPlayground.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Stage stage2 = new Stage();
        stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
        stage2.initOwner(stage);
        Scene scene = new Scene(root);
        stage2.setScene(scene);
        controller.addOtherPlayerground(pg);
        stage2.showAndWait();
    }
}



