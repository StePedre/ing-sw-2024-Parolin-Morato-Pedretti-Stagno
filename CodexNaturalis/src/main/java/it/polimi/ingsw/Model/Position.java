package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class Position is used in every PlayerGround to identify the position of each card.
 * It consists in two numeric coordinates x and y, so it is provided of get and set
 * methods for these two attributes. In addition, there is a method to check
 * if another object of the same class has the same exact position, so they are equivalent.
 */

public class Position implements Serializable {
    private static final long serialVersionUID = 3L;
        private int x;
        private int y;

    /**
     * Class constructor.
     *
     * @param x is one coordinate.
     * @param y is the other coordinate.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * The method compares an object to the caller, to see if they are equivalent.
     *
     * @param obj is the object to be compared.
     * @return false if the object is null or from a different class,
     *         true if they have both the same coordinates.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * The method gets the numeric value of the x coordinate.
     *
     * @return x value.
     */
    public int getX() {
        return x;
    }

    /**
     * The method gets the numeric value of the y coordinate.
     *
     * @return y value.
     */
    public int getY() {
        return y;
    }

    /**
     * The method sets the numeric value of the x coordinate.
     *
     * @param x is the desired value for coordinate x.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * The method sets the numeric value of the y coordinate.
     *
     * @param y is the desired value for coordinate y.
     */
    public void setY(int y) {
        this.y = y;
    }

}
