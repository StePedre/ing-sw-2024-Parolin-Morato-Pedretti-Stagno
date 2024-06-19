package it.polimi.ingsw.CS;

import it.polimi.ingsw.Model.InvalidPositionException;
import it.polimi.ingsw.Model.MissingResourcesException;
import it.polimi.ingsw.View.GUIrmi;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * The class Client only contains the main method which is called whenever a user wants to play the game.
 */

public class Client {

    /**
     * This method asks the user through which type of interface they want to play the game, and which type of connection
     * between socket and RMI. If the TUI is chosen, it also asks the user to insert the IP address pf the server. Then
     * it calls the chosen interface start methods.
     *
     * @param args are the arguments passed from the command line
     */
    public static void main(String[] args) {
        try{//chiedere Gui/Tui e tipo di connesione
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            System.out.println("Do you want to use a GUI or a TUI?");
            Scanner s = new Scanner(System.in);
            String choice;
            boolean chooseInterface = false,flag = false;
            do {
                choice = s.nextLine();
                if (choice.equalsIgnoreCase("TUI")) {
                    chooseInterface = true;
                    flag=true;
                } else if(choice.equalsIgnoreCase("GUI")){
                    flag=true;
                }
                else{
                    System.out.println("Please, choose between GUI and TUI");
                }
            }while(!flag);
            flag=false;
            System.out.println("Do you want to use a Socket or a RMI connection?");
            do {
                choice = s.nextLine();
                if (choice.equalsIgnoreCase("RMI")) {
                    if(!chooseInterface){
                        GUIrmi.startGUI();
                    }
                    else{
                        System.out.println("Insert the correct ip address of the server you want to connect to:");
                        Scanner scanner = new Scanner(System.in);
                        String ip = scanner.nextLine();
                        ClientRMIInterface myClientRMI = new MyClientRMI("rmi://"+ip+"/ServerRMI");
                        myClientRMI.runClient();
                    }
                    flag=true;
                } else if(choice.equalsIgnoreCase("socket")){
                    MyClientSocket myClientSocket = new MyClientSocket(chooseInterface);
                    myClientSocket.runClient();
                    flag = true;

                }else{
                    System.out.println("Please, choose between Socket and RMI");
                }
            }while(!flag);
        }
        catch (ConnectException e){
            System.out.println("Max number of player reached or no server listening");
        }
        catch(IOException | InvalidPositionException | MissingResourcesException e){
            e.printStackTrace();
        }
    }
}
