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
        int level = 11 + random.nextInt(5); // LEVEL 11-15

        // ENCOUNTER TABLE (COMMON & RARE)
        if (encounterRoll < 70) {
            // 70% CHANCE - COMMON POKEMON
            int commonRoll = random.nextInt(100);

            if (commonRoll < 20) {
                // 20% OF COMMON (14% OVERALL) - RATTATA
                return new Rattata(level);
            } 
            else if (commonRoll < 40) {
                // 20% OF COMMON (14% OVERALL) - PIDGEY
                return new Pidgey(level);
            } 
            else if (commonRoll < 60) {
                // 20% OF COMMON (14% OVERALL) - PIKACHU
                return new Pikachu(level);
            } 
            else if (commonRoll < 80) {
                // 20% OF COMMON (14% OVERALL) - CUBONE
                return new Cubone(level);
            } 
            else {
                // 20% OF COMMON (14% OVERALL) - PSYDUCK
                return new Psyduck(level);
            }
        } 
        
        else {
            // 30% CHANCE - RARE POKEMON
            int rareRoll = random.nextInt(100);

            if (rareRoll < 20) {
                // 20% OF RARE (6% OVERALL) - MACHOP
                return new Machop(level);
            } 
            else if (rareRoll < 40) {
                // 20% OF RARE (6% OVERALL) - GROWLITHE
                return new Growlithe(level);
            } 
            else if (rareRoll < 60) {
                // 20% OF RARE (6% OVERALL) - FARFETCHD
                return new Farfetchd(level);
            } 
            else if (rareRoll < 75) {
                // 15% OF RARE (4.5% OVERALL) - ELECTABUZZ
                return new Electabuzz(level);
            } 
            else if (rareRoll < 88) {
                // 13% OF RARE (3.9% OVERALL) - MAGMAR
                return new Magmar(level);
            } 
            else if (rareRoll < 95) {
                // 7% OF RARE (2.1% OVERALL) - SCYTHER
                return new Scyther(level);
            } 
            else if (rareRoll < 98) {
                // 3% OF RARE (0.9% OVERALL) - JYNX
                return new Jynx(level);
            } 
            else {
                // 2% OF RARE (0.6% OVERALL) - KANGASKHAN
                return new Kangaskhan(level);
            }
        }
    }
}
