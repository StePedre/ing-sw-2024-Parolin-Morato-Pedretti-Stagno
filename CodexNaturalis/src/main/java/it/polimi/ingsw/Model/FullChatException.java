package it.polimi.ingsw.Model;

/**
 * FullChatException class is an exception thrown when an object of class Chat is
 * already "full", in the sense that the maximum capacity of participants has been
 * reached, but there's an attempt to add another participant.
 * Extends well-known class Exception.
 */
public class FullChatException extends Exception{

    /**
     * Exception constructor.
     * @param s is the message shown when the exception is thrown.
     */
    public FullChatException(String s){
        super(s);
    }
}
