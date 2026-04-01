package com.game.world;

public class Tile {

    private String symbol;
    private boolean walkable;
    private boolean hasEncounter;
    private String name;

    //================================
    // TILE CONSTRUCTOR
    //================================
    public Tile(String symbol, boolean walkable, boolean hasEncounter, String name) {
        this.symbol = symbol;
        this.walkable = walkable;
        this.hasEncounter = hasEncounter;
        this.name = name;
    }

    //================================
    // GETTERS
    //================================
    public String getSymbol() {
        return symbol;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public boolean hasEncounter() {
        return hasEncounter;
    }
}
