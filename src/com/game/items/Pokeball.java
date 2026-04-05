package com.game.items;

import com.game.pokemons.Pokemon;
import java.util.*;

public class Pokeball extends Item {

    private double catchRateMultiplier;

    public Pokeball() {
        super("Pokeball");
        this.catchRateMultiplier = 1.0;
    }

    @Override
    public void use(Pokemon target) {
    }

    // ======================================
    // TRY TO CATCH A POKEMON
    // ======================================
    public boolean tryCatch(Pokemon target) {
        Random rand = new Random();

        // SIMPLE CATCH FORMULA: (MAXHP * 3 - CURRENTHP * 2) * RATE / (MAXHP * 3)
        double chance = (((3 * target.getMaxHp()) - (2 * target.getHp())) * catchRateMultiplier) / (3 * target.getMaxHp());

        // RANDOMNESS
        return rand.nextDouble() < chance;
    }
}
