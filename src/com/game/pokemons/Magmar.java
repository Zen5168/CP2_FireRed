package com.game.pokemons;

import com.game.logic.*;

public class Magmar extends Pokemon {

    public Magmar(int level) {
        // name, type1, type2, level, Hp, Atk, Def, SpAtk, SpDef, Speed
        super("Magmar", Type.FIRE, Type.NONE, level, 45, 49, 65, 49, 65, 45);

        // MOVE TABLE (LEVEL, "MOVE NAME")
        moveLevelUpTable.put(1, "Ember");
        moveLevelUpTable.put(1, "Smog");
        moveLevelUpTable.put(12, "Clear Smog");
        moveLevelUpTable.put(16, "Flame Wheel");
        moveLevelUpTable.put(28, "Fire Punch");
        moveLevelUpTable.put(34, "Lava Plume");
        moveLevelUpTable.put(46, "Flamethrower");
        moveLevelUpTable.put(58, "Fire Blast");
        moveLevelUpTable.put(64, "Hyper Beam");

        // INITIALIZE MOVES (GET 4 MOST RECENT MOVES BASED ON LEVEL)
        initializeMoves();
    }
}

