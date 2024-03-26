package it.polimi.ingsw.Model;

public enum Resource {
    PLUME("gold"),POTION("gold"),SCROLL("gold"),FOX("blue"),MUSHROOM("red"),LEAF("green"),BUG("violet");
    private String color;
    Resource(String color){
        this.color=color;
    }
    public String getColor(){
        return color;
    }

}
