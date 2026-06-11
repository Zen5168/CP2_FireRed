package com.game.pokemons;

import com.game.logic.*;

public class Squirtle extends Pokemon {

    public Squirtle(int level) {
        super("Squirtle", Type.WATER, Type.NONE, level, 44, 48, 65, 50, 64, 43);

        // EVOLUTION SETUP
        this.evolutionLevel = 16;
        this.evolutionName = "com.game.pokemons.Wartortle";

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Tackle");
        moveLevelUpTable.put(3, "Water Whip");
        moveLevelUpTable.put(12, "Bite");
        moveLevelUpTable.put(15, "Bubble Beam");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
