package it.polimi.ingsw.Model;

/**
 * InvalidPositionException class is an exception thrown when there is an attempt
 * to place a card in a position that is not available.
 * Extends well-known class Exception.
 */

public class InvalidPositionException extends Exception{

    /**
     * Exception constructor.
     * @param s is the message shown when the exception is thrown.
     */
    public InvalidPositionException(String s){
        super(s);
    }
}
