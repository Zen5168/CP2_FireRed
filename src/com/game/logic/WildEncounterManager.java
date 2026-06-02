package com.game.logic;

import com.game.pokemons.*;
import java.util.Random;

public class WildEncounterManager {

    private Random random;
    private static final double ENCOUNTER_RATE = 0.10; // 20% CHANCE PER STEP ON GRASS

    public WildEncounterManager() {
        this.random = new Random();
    }

    //=====================================
    // STEP ON GRASS TILE
    //=====================================
    public boolean checkForEncounter() {
        // ROLL A RANDOM NUMBER BETWEEN 0.0 AND 1.0
        double roll = random.nextDouble();
        
        // RETURN TRUE IF ROLL IS LESS THAN ENCOUNTER RATE
        return roll < ENCOUNTER_RATE;
    }

    //=====================================
    // GENERATE WILD POKEMON
    //=====================================
    public Pokemon generateWildPokemon() {
        int encounterRoll = random.nextInt(100);
        int level = 11 + random.nextInt(5); // LEVEL 3-6

        // ENCOUNTER TABLE (BASED ON RARITY)
        if (encounterRoll < 50) {
            // 50% CHANCE - RATTATA
            return new Rattata(level);
        } else {
            // 5% CHANCE - PIDGEY
            return new Pidgey(level);
        }
    }
}
