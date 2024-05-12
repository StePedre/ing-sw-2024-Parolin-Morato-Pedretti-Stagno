package it.polimi.ingsw.View;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StarterCard;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GUI extends Application{
    private Stage stage;
    private Screen screen = Screen.getPrimary();
    private double screenHeight = screen.getBounds().getHeight();
    private double screenWidth = screen.getBounds().getWidth();
    private final int height = 400;
    private final int width = 600;

    GroundController gc = new GroundController();

    DrawController dc = new DrawController();
    @FXML
    private Label loadingLabel;

//    public GUI(Stage stage) {
//        this.stage = stage;
//    }

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/loadingScene.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        List<Node> nodes = root.getChildrenUnmodifiable();
//        for (Node node : nodes) {
//            if (node instanceof Label) {
//                adjustLabel((Label) node, screenHeight, screenWidth);
//            } else if (node instanceof Button) {
//                adjustButton((Button) node, screenHeight, screenWidth);
//            } else if (node instanceof ProgressBar) {
//                adjustProgressBar((ProgressBar) node, screenHeight, screenWidth);
//            } else if (node instanceof VBox) {
//                VBox vbox = (VBox) node;
//                double size1 = vbox.getHeight();
//                double size2 = vbox.getWidth();
//                adjustLayout((VBox) node, screenHeight, screenWidth, size1, size2);
//            }
//        }
        stage.setTitle("Codex Naturalis");
        Scene scene = new Scene(root, screenWidth, screenHeight);

        controller.animationLoadingBar();

        controller.getLoadingBar().progressProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(newValue, 1.0)) {
                controller.getLoadingLabel().setVisible(true);
                scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent keyEvent) {
                        try {
                            controller.switchToLogin(stage);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(false);
        stage.show();


        /*
        // prima volta che si carica il playground:
        Parent root2 = FXMLLoader.load(getClass().getResource("/playerground.fxml"));
        // aggiungere la hand
        Stage.setTitle("Codex Naturalis");
        Stage.setScene(new Scene(root2, 350, 300));
        Stage.setFullScreen(true);
        Stage.show();

         */

    }

    public static void main (String[] args) {
        launch();
    }

    /*
    public static void main(String[] args) throws IOException {
        Stage stage = new Stage();
        GUI gui = new GUI(stage);
        gui.showLoadingScreen();
    }

    public void initializeScreen(Stage stage){
        stage.setTitle("Codex Naturalis");
        stage.setFullScreen(true);
    }
     */

    public void showLoadingScreen () throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/loadingScreen.fxml"));
        stage.setScene(new Scene(root, 350, 300));
        stage.show();
    }

    public void InitializePlayerGround(Player player, StarterCard startercard) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/playground.fxml")));
        gc.setNickname(player.getNickname());
        gc.addImages(player.getHand());
        gc.addSecretObj(player.getHand().getObjCard());
        gc.addCommonObj(player.getGame().getCommonObj());
        gc.setScore(player.getPlayerGround().getPlayerScore());
        gc.setTotalResource(player.getPlayerGround().getTotalResources());
        gc.placeFirstCard(startercard);
        gc.showAvailablePos(player.getPlayerGround());
        stage.setScene(new Scene(root, 350, 300));
        stage.show();  // finchè non è il suo turno
    }

    // non va qui, probabilmente sul controller, va chiamato dal client/server
    public void yourTurnBanner(Player player, Game game) throws IOException {
        Stage yourTurnStage = new Stage();
        yourTurnStage.initModality(Modality.WINDOW_MODAL);
        yourTurnStage.initOwner(stage);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/yourTurn.fxml")));
        stage.setScene(new Scene(root, 600, 200));
        stage.show();
    }

    public void showUpdatedPlayerGround(Player player) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/playground.fxml")));
        gc.setNickname(player.getNickname());
        gc.addImages(player.getHand());
        gc.addSecretObj(player.getHand().getObjCard());
        gc.addCommonObj(player.getGame().getCommonObj());
        gc.setScore(player.getPlayerGround().getPlayerScore());
        gc.setTotalResource(player.getPlayerGround().getTotalResources());
        stage.setScene(new Scene(root, 350, 300));
        stage.show();
    }


    public void adjustLayout (Node node, double screenHeight, double screenWidth, double nodeHeight, double nodeWidth) {
        double newAnchor = AnchorPane.getTopAnchor(node) * screenHeight / height;
        AnchorPane.setTopAnchor(node, newAnchor);
        newAnchor = AnchorPane.getBottomAnchor(node) * screenHeight / height;
        AnchorPane.setBottomAnchor(node, newAnchor);
        newAnchor = AnchorPane.getLeftAnchor(node) * screenWidth / width;
        AnchorPane.setLeftAnchor(node, newAnchor);
        newAnchor = AnchorPane.getRightAnchor(node) * screenWidth / width;
        AnchorPane.setRightAnchor(node, newAnchor);

        double newSize = nodeHeight * screenHeight / height;
        node.prefHeight(newSize);
        newSize = nodeWidth * screenWidth / width;
        node.prefHeight(newSize);
    }

    public void adjustLabel(Label label, double screenHeight, double screenWidth){
        double nodeHeight = label.getPrefHeight();
        double nodeWidth = label.getPrefWidth();
        adjustLayout(label, screenHeight, screenWidth, nodeHeight, nodeWidth);
        double newFontSize = calculateSizeFont(screenHeight, screenWidth, label.getFont().getSize());
        setFontSizeLabel(label, newFontSize);
    }

    public void adjustButton(Button button, double screenHeight, double screenWidth){
        double nodeHeight = button.getPrefHeight();
        double nodeWidth = button.getPrefWidth();
        adjustLayout(button, screenHeight, screenWidth, nodeHeight, nodeWidth);
        double newFontSize = calculateSizeFont(screenHeight, screenWidth, button.getFont().getSize());
        setFontSizeButton(button, newFontSize);
    }

    public void adjustProgressBar(ProgressBar pb, double screenHeight, double screenWidth){
        double nodeHeight = pb.getPrefHeight();
        double nodeWidth = pb.getPrefWidth();
        adjustLayout(pb, screenHeight, screenWidth, nodeHeight, nodeWidth);
    }

    public double calculateSizeFont (double screenHeight, double screenWidth, double fontSize) {
        return 0.3 * fontSize * (screenHeight * screenWidth) / (height * width);
    }

    public void setFontSizeLabel (Label label, double size) {
        Font originalFont = label.getFont();
        Font newFont = new Font(originalFont.getFamily(), size);
        label.setFont(newFont);
    }

    public void setFontSizeTextField (TextField tf, double size) {
        Font originalFont = tf.getFont();
        Font newFont = new Font(originalFont.getFamily(), size);
        tf.setFont(newFont);
    }

    public void setFontSizeButton (Button button, double size) {
        Font originalFont = button.getFont();
        Font newFont = new Font(originalFont.getFamily(), size);
        button.setFont(newFont);
    }

}

