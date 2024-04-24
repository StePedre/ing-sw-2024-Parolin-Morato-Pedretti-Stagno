package it.polimi.ingsw.Model;

/**
 * NotExistingChatException class is an exception thrown when there is an attempt
 * to get a chat between two players that doesn't exist.
 * satisfied. Extends well-known class Exception.
 */

public class NotExistingChatException extends Exception{

    /**
     * Exception constructor.
     * @param s is the message shown when the exception is thrown.
     */
    public NotExistingChatException(String s){
        super(s);
    }
}
