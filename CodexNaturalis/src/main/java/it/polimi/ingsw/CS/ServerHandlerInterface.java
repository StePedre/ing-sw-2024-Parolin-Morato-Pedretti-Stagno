package it.polimi.ingsw.CS;

import java.io.IOException;

public interface ServerHandlerInterface extends Runnable {
    @Override
    void run();
    void sendData() throws IOException, ClassNotFoundException;
    void drawCard();
    void playCard() throws IOException, ClassNotFoundException;
}
