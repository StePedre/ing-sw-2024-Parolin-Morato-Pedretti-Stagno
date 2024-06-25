package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.MyClientRMI;
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
import javafx.scene.input.KeyCode;
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

/**
 * The class GUIrmi manages all the scenes to display when using the GUI and the RMI connection.
 * It has a Screen and two double in order to get the height and the width of the primary screen of the user.
 * It also has a ServerRMIInterface in order to get an instance of the RMI server, thw String used to store the name
 * of a room and the nickname of a player.
 * It also has four ImageView, one used to set the background Image of every scene, and the other three that represents
 * the cards in hand of a player.
 * This class extends Application class.
 * Every scene is made with SceneBuilder.
 */

public class GUIrmi extends Application {
    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();
    private ImageView background,ivl,ivc,ivr;
    String roomName = "";
    String nickname = "";
    private ServerRMIInterface guiServerRMI;

    /**
     * This method displays the initialize player ground scene, where the starter card is placed in the center of the
     * board and the names of the other players are added.
     * Depending on whether it is the turn of the player, the next method called is yourTurn.
     * Otherwise, the notYourTurn one.
     *
     * @param stage is the original stage on which the application is displayed.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public void initializePlayerGround(Stage stage)throws ClassNotFoundException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/playground.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();
            Game g = guiServerRMI.getRoomController().getRoom(roomName).getGame();
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

    /**
     * This method manages the player when it is not their turn to play.
     * Once the server sends back the boolean that tells it is now the turn to play, it calls yourTurn method.
     * If any problem occurs, an error banner is displayed.
     *
     * @param stage is the original stage on which the application is displayed.
     * @param g is the instance of the game.
     * @param p is the instance of the player.
     * @param controller is the controller of the application.
     */
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

    /**
     * This method links the appropriate player ground to the correct player.
     * When clicked on the name of another player, their player ground is displayed in another window.
     * If any problem occurs, an error banner is displayed.
     *
     * @param g is the instance of the game.
     * @param controller is the controller of the application.
     * @param s is the original stage on which the application is displayed.
     */
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

    /**
     * This method sets visible the available colors.
     *
     * @param colors is the list of color to be displayed.
     * @param controller is the controller of the application.
     */
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

    /**
     * This method shows an error banner every time an error occurs.
     *
     * @param stage is the original stage on which the application is displayed.
     */
    public void showError(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/errorMessageBanner.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();
            Stage stage2 = new Stage();
            stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
            stage2.initOwner(stage);
            Scene scene = new Scene(root, 600, 200);
            stage2.setScene(scene);
            stage2.show();
            controller.getErrorButton().setOnAction(e->{
                stage2.close();
                stage.close();
            });
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method shows the player ground of other players.
     *
     * @param pg is the player whose player ground is displayed.
     * @param stage is the original stage on which the application is displayed.
     * @throws IOException if there has been problems regarding input or output.
     */
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

    /**
     * This method shows a banner that tells the player if it is their last turn or not.
     *
     * @param stage is the original stage on which the application is displayed.
     * @throws IOException if there has been problems regarding input or output.
     */
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

    /**
     * This method shows a banner that tells the player it is not their turn to play.
     *
     * @param stage is the original stage on which the application is displayed.
     * @throws IOException if there has been problems regarding input or output.
     */
    public void showNotYourTurn(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/notYourTurn.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Stage stage2 = new Stage();
        stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
        stage2.initOwner(stage);
        Scene scene = new Scene(root, 600, 200);
        stage2.setScene(scene);
        stage2.show();
        controller.getNotYourTurnButton().setOnAction(e -> {
            stage2.close();
        });
    }

    /**
     * This method shows the player that has won the game.
     *
     * @param stage is the original stage on which the application is displayed.
     * @param winners is the list of players that have won the game.
     * @throws IOException if there has been problems regarding input or output.
     */
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

    /**
     * This method shows the players that have won the game.
     *
     * @param stage is the original stage on which the application is displayed.
     * @param winners is the list of players that have won the game.
     * @throws IOException if there has been problems regarding input or output.
     */
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

    /**
     * This method shows a banner that tells the player it is their turn to play.
     *
     * @param stage is the original stage on which the application is displayed.
     * @throws IOException if there has been problems regarding input or output.
     */
    public void showYourTurn(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/yourTurn.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Stage stage2 = new Stage();
        stage2.initModality(Modality.WINDOW_MODAL);  // finestra bloccante
        stage2.initOwner(stage);
        Scene scene = new Scene(root, 600, 200);
        stage2.setScene(scene);
        stage2.show();
        controller.getYourTurnButton().setOnAction(e -> {
            stage2.close();
        });
    }

    /**
     * This method shows the banner that tells the decks has no more cards left.
     * Once the banner is closed, the next scene displayed is the waiting finish one.
     *
     * @param stage is the original stage on which the application is displayed.
     * @throws IOException if there has been problems regarding input or output.
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

    /**
     * This method displays the loading scene.
     * Once the user click any key, the switchToIpInput method is called.
     *
     * @param stage is the original stage on which the application is displayed.
     * @throws IOException if there has been problems regarding input or output.
     */
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

    /**
     * This method launches the application.
     */
    public static void startGUI(){
        launch();
    }

    /**
     * This method displays the color choice scene, where the user needs to pick a color to associate with their player.
     * Once a color is picked, it is removed from the available ones, and it is not displayed anymore.
     * The next scene displayed is the waiting start one.
     *
     * @param stage is the original stage on which the application is displayed.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
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

                        if (!guiServerRMI.isValidColor(choice[0],roomName)) {
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
                            guiServerRMI.addNewPlayer(nickname,roomName);
                            guiServerRMI.setPlayerColor(choice[0], nickname, roomName);
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        switchToWaitingStart(stage);
                    }
                });
            }
        });
    }

    /**
     * This method displays the draw scene, where the player needs to pick a card to draw.
     * After the card is picked, the notYourTurn method is called, as drawing a card is the last action a player can do
     * in their turn.
     * If any problem occurs, an error banner is displayed.
     *
     * @param stage is the original stage on which the application is displayed.
     * @param playgroundController is the controller of the application.
     */
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
            Game game = guiServerRMI.getRoomController().getRoom(roomName).getGame();
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
                        Game g = guiServerRMI.getRoomController().getRoom(roomName).getGame();
                        Player p = g.getPlayer(nickname);
                        notYourTurn(stage, g, p, playgroundController);
                    } catch (IOException ex) {
                        showError(stage);
                    }
                }
            });
            controller.getResUp2().setOnMouseClicked(e -> {
                if (game.getDecks()[0].getCards().get(1) != null) {
                    try {
                        guiServerRMI.drawCard(0,1,roomName, nickname);
                        stage2.close();
                        Game g = guiServerRMI.getRoomController().getRoom(roomName).getGame();
                        Player p = g.getPlayer(nickname);
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
                        Game g = guiServerRMI.getRoomController().getRoom(roomName).getGame();
                        Player p = g.getPlayer(nickname);
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
                        Game g = guiServerRMI.getRoomController().getRoom(roomName).getGame();
                        Player p = g.getPlayer(nickname);
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
                        Game g = guiServerRMI.getRoomController().getRoom(roomName).getGame();
                        Player p = g.getPlayer(nickname);
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
                        Game g = guiServerRMI.getRoomController().getRoom(roomName).getGame();
                        Player p = g.getPlayer(nickname);
                        notYourTurn(stage, g, p, playgroundController);
                    } catch (IOException ex) {
                        showError(stage);
                    }
                }
            });
        }
        catch (IOException e){
            showError(stage);
        }}

    /**
     * This method displays the IP input scene, where the user needs to insert the IP address of the server they want to
     * connect. Once the submit button is pressed, the next scene displays is the room choice one.
     * If any problem occurs, an error banner is displayed.
     *
     * @param stage is the original stage on which the application is displayed.
     * @throws IOException if there has been problems regarding input or output.
     */
    public void switchToIpInput(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ipInputScene.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getAnchor10().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        Runnable switchThings = () -> {
            try {
                String ip = controller.getIpField().getText();
                try {
                    guiServerRMI = (ServerRMIInterface) Naming.lookup("rmi://" + ip + "/ServerRMI");
                    System.out.println("Connected to RMI server.");
                } catch (Exception e) {
                    System.err.println("Client exception: " + e.toString());
                }
                switchToRoomChoice(stage);
            } catch (IOException | ClassNotFoundException ex) {
                try {
                    ex.printStackTrace();
                    switchToIpInput(stage);
                } catch (IOException exc) {
                }
            }
        };
        controller.getIpButton().setOnAction(e -> {
            switchThings.run();
        });
        scene.setOnKeyPressed(e-> {
            if(e.getCode()== KeyCode.ENTER){
                if(!controller.getIpField().getText().isEmpty()){
                    switchThings.run();
                }
            }
        });
    }

    /**
     * This method displays the login scene, where the user inserts the nickname of their player.
     * It checks if the name is already taken. If so, an error message is shown.
     * Depending on whether the player is the first one in the room or not, the next scene displayed is the number of
     * players one. Otherwise, the color choice scene is displayed.
     *
     * @param stage is the original stage on which the application is displayed.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public void switchToLogin(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/insertNick.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getNickAnchor().getChildren().addFirst(background);
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
        Runnable switchThings = () -> {
            try {
                nickname = controller.getNickname();
                System.out.println(nickname);
                if (guiServerRMI.getRoomController().alreadyInGame(roomName, nickname)) {
                    controller.getNickLabel().setPrefWidth(800);
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
        };
        controller.getNickButton().setOnAction(actionEvent -> {
            switchThings.run();
        });
        scene.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER){
                if(!controller.getNickTextField().getText().isEmpty()){
                    switchThings.run();
                }
            }
        });
    }

    /**
     * This method displays the number of players scene, where the user chooses the number of expected players that needs
     * to join the room before the game starts. This scene is displayed only to the first player in a room.
     * The next scene displayed is the color choice one.
     *
     * @param stage is the original stage on which the application is displayed.
     * @throws IOException if there has been problems regarding input or output.
     */
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

    /**
     * This method displays the room choice scene, where the user can create a new room or join an already existing one.
     * In the first case, it checks if the name of the room is already taken and shows an error message.
     * In the second case, it shows a list of every room available and the user can choose one from that list.
     * The next scene displayed is the login one.
     *
     * @param stage is the original stage on which the application is displayed.
     * @throws IOException if there has been problems regarding input or output.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
    public void switchToRoomChoice(Stage stage) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/roomChoice.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getAnchor2().getChildren().addFirst(background);
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
        ArrayList<Room> rooms = guiServerRMI.getRoomController().getRooms();
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
                            if (guiServerRMI.getRoomController().getRoom(roomName).isFull()) {
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

    /**
     * This method displays the select secret objective scene, where the user needs to pick one between the two ObjectiveCards
     * displayed as their secret objective.
     * Either choice made, the next scene displayed is the initialize player ground one.
     * If any problem occurs, an error banner is displayed.
     *
     * @param stage is the original stage on which the application is displayed.
     */
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

    /**
     * This method displays the starter choice scene, where the user needs to pick a side of the StarterCard displayed to
     * be placed in their player ground.
     * Either choice made, the next scene displayed is the select secret objective one.
     * If any problem occurs, an error banner is displayed.
     *
     * @param stage is the original stage on which the application is displayed.
     */
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

    /**
     * This method displays the waiting finish scene, where the player needs to wait until all the players have finished
     * their last turns. After that, depending on how many players have won, the single winner or the multi winner method
     * is called.
     * If any problem occurs, an error banner is displayed.
     *
     * @param stage is the original stage on which the application is displayed.
     * @throws ClassNotFoundException if there has been problems regarding the cast of an Object.
     */
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
                    while(!guiServerRMI.isGameOver(roomName)){
                    }
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

    private void checkForDisconnection(Stage stage){
        Task<Integer> task = new Task<>(){
            @Override
            protected Integer call() throws Exception {
                while(!guiServerRMI.isTerminating(roomName)){

                }
                showError(stage);
                return null;
            }
        };
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    /**
     * This method displays the waiting start scene, where the player needs to wait until all the players have joined.
     * After that, the starter choice scene is displayed.
     * If any problem occurs, an error banner is displayed.
     *
     * @param stage is the original stage on which the application is displayed.
     */
    public void switchToWaitingStart(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/waitingStart.fxml"));
            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.getAnchor4().getChildren().addFirst(background);
            Player player = guiServerRMI.getRoomController().getRoom(roomName).getGame().getPlayer(nickname);
            controller.getStartLabel().setText("Please " + player.getNickname() + ", wait for other players to join.");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            Task<Integer> task = new Task<>() {
                @Override
                protected Integer call() throws Exception {
                    boolean waitingForPlayers = true;
                    while (waitingForPlayers) {
                        if(guiServerRMI.getRoomController().getRoom(roomName).isFull()) {
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
                //checkForDisconnection(stage);
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

    /**
     * This method manages all the actions the player can do during their turn.
     * Firstly it checks if it is the last turn. If not, it displays a banner that tells the player they need to play.
     * Then, manages the drag and drop mechanism used to place a card from the hand in one of the available positions,
     * shown in the player ground.
     * If it is not the last turn, after the card has been successfully placed, the next scene displayed is the draw one.
     * Otherwise, the next scene displayed is the waiting finish one.
     * The method also checks if the decks have zero cards left. If it is the case, a banner telling that is displayed,
     * and the waiting finish scene is loaded.
     * If any other problem occurs, an error banner is displayed.
     *
     * @param stage is the original stage on which the application is displayed.
     * @param controller is the controller of the application.
     */
    public void yourTurn(Stage stage, Controller controller){
        try {
            Game[] game = new Game[1];
            Player[] player = new Player[1];
            game[0] = guiServerRMI.getRoomController().getRoom(roomName).getGame();
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
                        controller.updateAfterPlay(guiServerRMI.getRoomController().getRoom(roomName).getGame().getPlayer(nickname));
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
                                guiServerRMI.nextRound(roomName);
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
                        controller.updateAfterPlay(guiServerRMI.getRoomController().getRoom(roomName).getGame().getPlayer(nickname));
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
                                guiServerRMI.nextRound(roomName);
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
                        controller.updateAfterPlay(guiServerRMI.getRoomController().getRoom(roomName).getGame().getPlayer(nickname));
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
                                guiServerRMI.nextRound(roomName);
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

}



