package com.game.pokemons;

import com.game.logic.*;

public class Golduck extends Pokemon {

    public Golduck(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Golduck", Type.WATER, Type.NONE, level, 80, 82, 78, 95, 80, 85);

        this.evolutionLevel = 33;
        this.evolutionName = "com.game.pokemons.Golduch";

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Scratch");
        moveLevelUpTable.put(3, "Water Gun");
        moveLevelUpTable.put(6, "Confusion");
        moveLevelUpTable.put(9, "Fury Swipes");
        moveLevelUpTable.put(12, "Water Pulse");
        moveLevelUpTable.put(18, "Zen Headbutt");
        moveLevelUpTable.put(24, "Aqua Tail");
        moveLevelUpTable.put(40, "Hydro Pump");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
