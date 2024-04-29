package it.polimi.ingsw.Model;

/**
 * The Resource enumeration specifies the only seven types of resources possible.
 * Each resource is associated with a color: three of them (plume, potion, scroll)
 * are associated with gold, fox matches blue, mushroom matches red, leaf matches
 * green, bug matches violet.
 */

public enum Resource {
    PLUME("gold"),POTION("gold"),SCROLL("gold"),FOX("blue"),MUSHROOM("red"),LEAF("green"),BUG("violet"), BLANK("blank"), NOTVISIBLE("null");
    private String color;

    /**
     * Class constructor.
     *
     * @param color is associated with the type of resource.
     */
    Resource(String color){
        this.color=color;
    }

    /**
     * The method gets the color of the Resource, according to the matches.
     *
     * @return the color
     */
    public String getColor(){
        return color;
    }

}
