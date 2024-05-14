package it.polimi.ingsw.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUI extends Application{
    private final Screen screen = Screen.getPrimary();
    private final double screenHeight = screen.getBounds().getHeight();
    private final double screenWidth = screen.getBounds().getWidth();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/loadingScene.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        stage.setTitle("Codex Naturalis");
        Scene scene = new Scene(root, screenWidth, screenHeight);

        controller.animationLoadingBar();

        controller.getLoadingBar().progressProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(newValue, 1.0)) {
                controller.getLoadingLabel().setVisible(true);
                scene.setOnKeyReleased(event -> {
                    try {
                        controller.switchToLogin(stage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(false);
        stage.show();
    }


    public static void startGUI(){
        launch();
    }

    public static void main(String[] args) {
        launch();
    }
}

