package com.game.trainers;

import com.game.pokemons.Pokemon;
import java.util.ArrayList;

public class Trainer {

    private String name;
    private int x, y;
    private ArrayList<Pokemon> party;

    public Trainer(String name, int startX, int startY) {
        this.name = name;
        this.x = startX;
        this.y = startY;
        this.party = new ArrayList<>();
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    //================================
    // GETTERS AND SETTERS
    //================================
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

    public void addPokemon(Pokemon p) {
        if (party.size() < 6) {
            party.add(p);
        }
    }

    public ArrayList<Pokemon> getParty() {
        return party;
    }
}
