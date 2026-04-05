package com.game.trainers;

import com.game.pokemons.Pokemon;
import com.game.items.*;
import java.util.ArrayList;

public class Player {

    private String name;
    private int x, y;
    private ArrayList<Pokemon> party;
    private Bag bag;

    public Player(String name, int startX, int startY) {
        this.name = name;
        this.x = startX;
        this.y = startY;
        this.party = new ArrayList<>();
        this.bag = new Bag();
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void addPokemon(Pokemon p) {
        if (party.size() < 6) {
            party.add(p);
            System.out.println(p.getName() + " was added to the party!");
        } else {
            System.out.println("The party is full! (PC Storage not yet implemented)");
        }
    }

    //================================
    // GETTERS AND SETTERS
    //================================
    public String getName() {
        return this.name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ArrayList<Pokemon> getParty() {
        return party;
    }
    
    public Bag getBag(){
        return bag;
    }
}
