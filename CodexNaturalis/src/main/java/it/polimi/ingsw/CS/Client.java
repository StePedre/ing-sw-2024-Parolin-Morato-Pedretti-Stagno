package it.polimi.ingsw.CS;

import it.polimi.ingsw.Model.InvalidPositionException;
import it.polimi.ingsw.Model.MissingResourcesException;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try{//chiedere Gui/Tui e tipo di connesione
            System.out.println("Do you want to use a GUI or a TUI?");
            Scanner s = new Scanner(System.in);
            String choice;
            boolean inter = false,flag = false;
            do {
                choice = s.nextLine();
                if (choice.equalsIgnoreCase("TUI")) {
                    inter = true;
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
                    ClientRMIInterface myClientRMI = new MyClientRMI("rmi://localhost/ServerRMI",inter);
                    myClientRMI.runClient();
                    flag=true;
                } else if(choice.equalsIgnoreCase("socket")){
                    MyClientSocket myClientSocket = new MyClientSocket(59090, "127.0.0.1",inter);
                    myClientSocket.runClient();
                    flag=true;
                }else{
                    System.out.println("Please, choose between Socket and RMI");
                }
            }while(!flag);
        }
        catch (ConnectException e){
            System.out.println("Max number of player reached or no server listening");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){
            //aggiugni ecc
        } catch (MissingResourcesException e) {
            throw new RuntimeException(e);
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }
    }
}
