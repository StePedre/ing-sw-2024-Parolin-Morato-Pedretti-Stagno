package it.polimi.ingsw.Model;

/**
 * MissingResourceException class is an exception thrown when there is an attempt
 * to place a card that has non-null requirements and those requirements are not
 * satisfied. Extends well-known class Exception.
 */

public class MissingResourcesException extends Exception{

    /**
     * Exception constructor.
     * @param s is the message shown when the exception is thrown.
     */
    public MissingResourcesException(String s){
        super(s);
    }
}
