package it.polimi.ingsw.View;

import it.polimi.ingsw.CS.MyClientRMI;
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
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Set;

import static javafx.application.Application.launch;

public class GUIrmi extends Application {

    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();
    private ImageView background;
    private PlayerController playerController;
    private GUIClientRMI guiClientRMI;

    public static void startGUI() {
        launch();
    }

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
        stage.setFullScreen(false);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/IpInputScene.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.getAnchor10().getChildren().addFirst(background);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        controller.getIpButton().setOnAction(e -> {
            try {
                String ip = controller.getIpField().getText();
                guiClientRMI = new GUIClientRMI("rmi://" + ip + "/ServerRMI");
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
        ArrayList<Room> rooms = guiClientRMI.getRooms();
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
                                if (!guiClientRMI.addRoom(roomName)) {    // chiedere se ok al server
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
                            Room roomJoined = guiClientRMI.getRoom(controller.getMenu().getSelectionModel().getSelectedItem().getText());
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
                if (guiClientRMI.addPlayer(controller.getNickname(), roomName) == null) {
                    controller.getNickLabel().setPrefWidth(800);    // eventualmente aggiungere un'altra label
                    controller.getNickLabel().setStyle("-fx-text-fill: #b20b0b");
                    controller.getNickLabel().setText("This name is already taken, choose another one:");
                    controller.getNickTextField().setText("");
                } else {
                    if (guiClientRMI.isFirst(roomName)) {
                        // switchToNumberPlayers(stage, roomName);
                    } else {
                        //switchToColorChoice(stage);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}