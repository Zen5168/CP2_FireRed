package com.game.logic;

import com.game.pokemons.*;
import java.util.Random;

public class WildEncounterManager {

    private Random random;
    private int stepCounter;
    private static final int MIN_STEPS_BETWEEN_ENCOUNTERS = 3;
    private static final int MAX_STEPS_BETWEEN_ENCOUNTERS = 8;
    private int stepsUntilNextEncounter;

    public WildEncounterManager() {
        this.random = new Random();
        this.stepCounter = 0;
        this.stepsUntilNextEncounter = getRandomStepCount();
    }

    //=====================================
    // STEP ON GRASS TILE
    //=====================================
    public boolean checkForEncounter() {
        stepCounter++;

        if (stepCounter >= stepsUntilNextEncounter) {
            stepCounter = 0;
            stepsUntilNextEncounter = getRandomStepCount();
            return true; // TRIGGER ENCOUNTER
        }
        return false;
    }

    //=====================================
    // RANDOM STEP COUNT
    //=====================================
    private int getRandomStepCount() {
        return MIN_STEPS_BETWEEN_ENCOUNTERS + random.nextInt(MAX_STEPS_BETWEEN_ENCOUNTERS - MIN_STEPS_BETWEEN_ENCOUNTERS + 1);
    }

    //=====================================
    // GENERATE WILD POKEMON
    //=====================================
    public Pokemon generateWildPokemon() {
        int encounterRoll = random.nextInt(100);
        int level = 3 + random.nextInt(4); // LEVEL 3-6

        // ENCOUNTER TABLE (BASED ON RARITY)
        if (encounterRoll < 40) {
            // 40% CHANCE - RATTATA
            return new Rattata(level);
        } else if (encounterRoll < 80) {
            // 40% CHANCE - PIDGEY
            return new Pidgey(level);
        } else if (encounterRoll < 90) {
            // 10% CHANCE - BULBASAUR
            return new Bulbasaur(level);
        } else if (encounterRoll < 95) {
            // 5% CHANCE - CHARMANDER
            return new Charmander(level);
        } else {
            // 5% CHANCE - SQUIRTLE
            return new Squirtle(level);
        }
    }

    //=====================================
    // RESET COUNTER (AFTER BATTLE OR LEAVING GRASS)
    //=====================================
    public void resetCounter() {
        stepCounter = 0;
        stepsUntilNextEncounter = getRandomStepCount();
    }
}
