package com.game.logic;

import com.game.pokemons.*;
import java.util.Random;

public class WildEncounterManager {

    private Random random;
    private static final double ENCOUNTER_RATE = 0.20; // 20% chance per step on grass

    public WildEncounterManager() {
        this.random = new Random();
    }

    //=====================================
    // STEP ON GRASS TILE
    //=====================================
    public boolean checkForEncounter() {
        // Roll a random number between 0.0 and 1.0
        double roll = random.nextDouble();
        
        // Return true if roll is less than encounter rate
        return roll < ENCOUNTER_RATE;
    }

    //=====================================
    // GENERATE WILD POKEMON
    //=====================================
    public Pokemon generateWildPokemon() {
        int encounterRoll = random.nextInt(100);
        int level = 3 + random.nextInt(4); // LEVEL 3-6

        // ENCOUNTER TABLE (BASED ON RARITY)
        if (encounterRoll < 50) {
            // 50% CHANCE - RATTATA
            return new Rattata(level);
        } else {
            // 40% CHANCE - PIDGEY
            return new Pidgey(level);
        }
    }
}
