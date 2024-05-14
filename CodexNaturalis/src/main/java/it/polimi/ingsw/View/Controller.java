package it.polimi.ingsw.View;


import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.ScoreRules.FlatRule;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller  implements  Initializable{

    @FXML
    private ImageView frontStarterCard, backStarterCard;
    @FXML
    private Label starterlabel;
    @FXML
    private AnchorPane pane;
    @FXML
    private ProgressBar loadingBar;
    @FXML
    private Button nickButton;
    @FXML
    private TextField nickTextField;
    @FXML
    private ImageView secretObjLeft, secretObjRight;

    @FXML
    private TextField numberPlayersTF;
    @FXML
    private Label validLabel;
    @FXML
    private Label loadingLabel;
    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();

    private GUIClientSocket client;
    private final String imagesFrontPath = "/CodexNaturalis/src/main/resources/CODEX_cards_gold_back";
    private final String imagesBackPath = "/CodexNaturalis/src/main/java/it/polimi/ingsw/Resources/CODEX_cards_gold_back/";

    public ProgressBar getLoadingBar() {
        return loadingBar;
    }

    public Label getLoadingLabel() {
        return loadingLabel;
    }

    public void switchToLogin(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/insertNick.fxml")));
        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
    }

    public void animationLoadingBar() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(loadingBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(3), e -> {
                }, new KeyValue(loadingBar.progressProperty(), 1))
        );
        timeline.play();

    }

    public void getNickname(ActionEvent event) throws IOException {
        String nickname;
        do {
            nickname = nickTextField.getText();
            if (!nickname.isEmpty()) {
                nickTextField.clear();
            }
        }while()
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        switchToNumberPlayers(stage);
    }



    public void switchToNumberPlayers(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/requestNoPlayers.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void getNumberPlayers(ActionEvent event) throws IOException {
        int numberOfPlayers;
        String inputText = numberPlayersTF.getText();
        if (!inputText.isEmpty()) {
            try {
                numberOfPlayers = Integer.parseInt(inputText);
                if (numberOfPlayers < 2 || numberOfPlayers > 4) {
                    validLabel.setVisible(true);
                    numberPlayersTF.clear();
                } else {
                    System.out.println(numberOfPlayers);
                    Scene scene = ((Node) event.getSource()).getScene();
                    Stage stage = (Stage) scene.getWindow();
                    switchToStarterChoice(stage);
                }
            } catch (NumberFormatException e) {
                validLabel.setVisible(true);
                numberPlayersTF.clear();
            }
        }

    }

    public void getSideStarterCard(ActionEvent event) throws IOException {

    }

    public void getLeftSecretObj(MouseEvent event) throws IOException {
        ObjectiveCard secretObj;
        //server.sendSecretObj (secretObj);
        System.out.println("Chosen left secret objective");
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        switchToWaitingStart(stage);
    }

    public void getRightSecretObj(MouseEvent event) throws IOException {
        ObjectiveCard secretObj;
        // associazione immagine-carta
        //server.sendSecretObj (secretObj);
        System.out.println("Chosen right secret objective");
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        switchToWaitingStart(stage);
    }

    public void switchToWaitingStart(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/waitingStart.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToStarterChoice(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/startingCardChoice.fxml")));
        // client manda starter card...
        StarterCard card = new StarterCard(86, new FlatRule(0), new Corner[4], new Corner[4], Resource.BLANK, null);
        addStarterImages(card);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

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

    public void addStarterImages(StarterCard card) throws IOException {
        Image image1 = new Image("file:" + imagesFrontPath + card.getId() + ".png");
        Image image2 = new Image("file:" + imagesBackPath + card.getId() + ".png");
        frontStarterCard.setImage(image1);
        backStarterCard.setImage(image2);
    }

    public void addSecretObjImages(ObjectiveCard[] secretObjs) {
        secretObjLeft.setImage(new Image(imagesFrontPath + secretObjs[0].getId() + ".png"));
        secretObjRight.setImage(new Image(imagesFrontPath + secretObjs[1].getId() + ".png"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client =
    }
}


