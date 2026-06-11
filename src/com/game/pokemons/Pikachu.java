package com.game.pokemons;

import com.game.logic.*;

public class Pikachu extends Pokemon {

    public Pikachu(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Pikachu", Type.ELECTRIC, Type.NONE, level, 35, 55, 40, 50, 50, 90);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Quick Attack");
        moveLevelUpTable.put(2, "Thunder Shock");
        moveLevelUpTable.put(12, "Nuzzle");
        moveLevelUpTable.put(16, "Spark");
        moveLevelUpTable.put(25, "Thunderbolt");
        moveLevelUpTable.put(30, "Thunder");
        moveLevelUpTable.put(40, "Volt Tackle");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}
