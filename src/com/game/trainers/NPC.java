package com.game.trainers;

import com.game.pokemons.Pokemon;
import java.util.*;

public class NPC {

    private String name;
    private String welcomeMessage;
    private ArrayList<Pokemon> party = new ArrayList<>();

    public NPC(String name, String msg) {
        this.name = name;
        this.welcomeMessage = msg;
    }

    public void addPokemon(Pokemon p) {
        party.add(p);
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return welcomeMessage;
    }

    public Pokemon getFirstPokemon() {
        return party.get(0);
    }
}
