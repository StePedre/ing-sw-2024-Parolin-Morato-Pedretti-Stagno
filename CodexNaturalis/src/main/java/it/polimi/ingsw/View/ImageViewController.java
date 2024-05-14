//package it.polimi.ingsw.View;
//
//import it.polimi.ingsw.Model.Corner;
//import it.polimi.ingsw.Model.ObjectiveCard;
//import it.polimi.ingsw.Model.Resource;
//import it.polimi.ingsw.Model.ScoreRules.FlatRule;
//import it.polimi.ingsw.Model.StarterCard;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.Background;
//import javafx.scene.layout.BackgroundImage;
//import javafx.scene.layout.Pane;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Objects;
//import java.util.ResourceBundle;
//
//public class ImageViewController implements Initializable {
//    private final String imagesFrontPath = "C:\\Users\\Ste\\Desktop\\Stefano\\UNI\\ANNO III\\INGEGNERIA DEL SOFTWARE\\PROGETTO_IDS\\ing-sw-2024-Parolin-Morato-Pedretti-Stagno\\CodexNaturalis\\src\\main\\resources\\CODEX_cards_gold_front\\";
//    private final String imagesBackPath = "C:\\Users\\Ste\\Desktop\\Stefano\\UNI\\ANNO III\\INGEGNERIA DEL SOFTWARE\\PROGETTO_IDS\\ing-sw-2024-Parolin-Morato-Pedretti-Stagno\\CodexNaturalis\\src\\main\\resources\\CODEX_cards_gold_back\\";
//    @FXML
//    private ImageView frontStarterCard;
//    @FXML
//    private ImageView backStarterCard;
//
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        frontStarterCard.setImage(new Image("file:" + imagesFrontPath + "1.png"));
//        backStarterCard.setImage(new Image("file:" + imagesBackPath + "1.png"));
//    }
//
//    public void getLeftSecretObj (MouseEvent event) throws IOException {
//        ObjectiveCard secretObj;
//        //server.sendSecretObj (secretObj);
//        System.out.println("Chosen left secret objective");
//        Scene scene = ((Node) event.getSource()).getScene();
//        Stage stage = (Stage) scene.getWindow();
//        Controller ctrl = new Controller();
//        ctrl.switchToWaitingStart(stage);
//    }
//
//    public void getRightSecretObj (MouseEvent event) throws IOException {
//        ObjectiveCard secretObj;
//        // associazione immagine-carta
//        //server.sendSecretObj (secretObj);
//        System.out.println("Chosen right secret objective");
//        Scene scene = ((Node) event.getSource()).getScene();
//        Stage stage = (Stage) scene.getWindow();
//        Controller ctrl = new Controller();
//        ctrl.switchToWaitingStart(stage);
//    }
//
//    public void switchToStarterChoice (Stage stage) throws IOException {
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/startingCardChoice.fxml")));
//        // client manda starter card...
//        ArrayList<Resource> backRes = new ArrayList<>();
//        StarterCard card = new StarterCard(86, new FlatRule(0), new Corner[4], new Corner[4], Resource.BLANK, backRes);
//        addStarterImages(card);
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//
///*
//    public void switchToSelectSecretObj (MouseEvent event) throws IOException {
//        // add method that returns the chosen side of the card!!!
//        Scene scene = ((Node) event.getSource()).getScene();
//        Stage stage = (Stage) scene.getWindow();
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/selectSecretObjs.fxml")));
//        // client manda secret objectives
//        ObjectiveCard card1 = new ObjectiveCard(93, new FlatRule(0));
//        ObjectiveCard card2 = new ObjectiveCard(91, new FlatRule(0));
//        ObjectiveCard[] objs = {card1, card2};
//        addSecretObjImages(objs);
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
//
// */
//
//    public void addStarterImages(StarterCard card) throws IOException {
//        frontStarterCard.setImage(new Image("file:" + imagesFrontPath + card.getId() + ".png"));
////        backStarterCard.setImage(new Image("file:" + imagesBackPath + card.getId() + ".png"));
//    }
//
//    public void addSecretObjImages(ObjectiveCard[] secretObjs) {
////        secretObjLeft.setImage(new Image(imagesFrontPath + secretObjs[0].getId() + ".png"));
////        secretObjRight.setImage(new Image(imagesFrontPath + secretObjs[1].getId() + ".png"));
//    }
//
//}
