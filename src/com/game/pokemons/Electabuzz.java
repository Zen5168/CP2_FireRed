package com.game.pokemons;

import com.game.logic.*;

public class Electabuzz extends Pokemon {

    public Electabuzz(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Electabuzz", Type.ELECTRIC, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Quick Attack");
        moveLevelUpTable.put(1, "Thunder Shock");
        moveLevelUpTable.put(12, "Swift");
        moveLevelUpTable.put(16, "Shock Wave");
        moveLevelUpTable.put(28, "Thunder Punch");
        moveLevelUpTable.put(34, "Discharge");
        moveLevelUpTable.put(46, "Thunderbolt");
        moveLevelUpTable.put(58, "Thunder");
        moveLevelUpTable.put(64, "Giga Impact");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

