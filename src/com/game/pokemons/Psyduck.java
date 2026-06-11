package com.game.pokemons;

import com.game.logic.*;

public class Psyduck extends Pokemon {

    public Psyduck(int level) {
        super("Psyduck", Type.WATER, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Scratch");
        moveLevelUpTable.put(3, "Water Gun");
        moveLevelUpTable.put(6, "Confusion");
        moveLevelUpTable.put(9, "Fury Swipes");
        moveLevelUpTable.put(12, "Water Pulse");
        moveLevelUpTable.put(18, "Zen Headbutt");
        moveLevelUpTable.put(24, "Aqua Tail");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

